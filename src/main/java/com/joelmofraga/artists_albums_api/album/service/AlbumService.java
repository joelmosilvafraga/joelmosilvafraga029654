package com.joelmofraga.artists_albums_api.album.service;

import com.joelmofraga.artists_albums_api.album.domain.Album;
import com.joelmofraga.artists_albums_api.album.domain.AlbumType;
import com.joelmofraga.artists_albums_api.album.dto.AlbumCreateRequest;
import com.joelmofraga.artists_albums_api.album.dto.AlbumResponse;
import com.joelmofraga.artists_albums_api.album.dto.AlbumUpdateRequest;
import com.joelmofraga.artists_albums_api.album.repository.AlbumRepository;
import com.joelmofraga.artists_albums_api.album.repository.AlbumTypeRepository;
import com.joelmofraga.artists_albums_api.websocket.dto.AlbumCreatedEvent;
import com.joelmofraga.artists_albums_api.websocket.notifier.AlbumWsNotifier;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final AlbumTypeRepository albumTypeRepository;
    private final AlbumWsNotifier notifier;

    public AlbumService(
            AlbumRepository albumRepository,
            AlbumTypeRepository albumTypeRepository,
            AlbumWsNotifier notifier
    ) {
        this.albumRepository = albumRepository;
        this.albumTypeRepository = albumTypeRepository;
        this.notifier = notifier;
    }

    @Transactional
    public AlbumResponse create(AlbumCreateRequest request) {

        AlbumType type = albumTypeRepository.findByCodeIgnoreCase(request.getAlbumTypeCode().trim())
                .filter(AlbumType::getActive)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid albumTypeCode: " + request.getAlbumTypeCode()
                ));

        Album album = new Album();
        album.setTitle(request.getTitle().trim());
        album.setReleaseYear(request.getReleaseYear());
        album.setGenre(request.getGenre() != null ? request.getGenre().trim() : null);
        album.setAlbumType(type);

        Album saved = albumRepository.save(album);


        notifier.notifyAlbumCreated(
                new AlbumCreatedEvent(
                        saved.getId(),
                        saved.getTitle(),
                        saved.getReleaseYear(),
                        saved.getCreatedAt()
                )
        );

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public AlbumResponse getById(Long id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Album not found: " + id));
        return toResponse(album);
    }

    @Transactional(readOnly = true)
    public AlbumResponse getByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "title is required");
        }

        Album album = albumRepository.findFirstByTitleIgnoreCase(title.trim())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Album not found for title: " + title
                ));

        return toResponse(album);
    }

    @Transactional(readOnly = true)
    public List<AlbumResponse> getAlbumsByArtistName(String artistName) {
        if (artistName == null || artistName.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is required");
        }

        var albums = albumRepository.findAlbumsByArtistNameIgnoreCase(artistName.trim());

        return albums.stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public AlbumResponse update(Long id, AlbumUpdateRequest request) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Album not found: " + id));

        AlbumType type = albumTypeRepository.findByCodeIgnoreCase(request.getAlbumTypeCode().trim())
                .filter(AlbumType::getActive)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid albumTypeCode: " + request.getAlbumTypeCode()
                ));

        album.setTitle(request.getTitle().trim());
        album.setReleaseYear(request.getReleaseYear());
        album.setGenre(request.getGenre() != null ? request.getGenre().trim() : null);
        album.setAlbumType(type);

        Album saved = albumRepository.save(album);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<AlbumResponse> list(
            String title,
            String albumTypeCode,
            String artistName,
            Pageable pageable
    ) {
        Specification<Album> spec = (root, query, cb) -> cb.conjunction();

        if (title != null && !title.isBlank()) {
            String t = title.trim().toLowerCase();
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("title")), "%" + t + "%")
            );
        }

        if (albumTypeCode != null && !albumTypeCode.isBlank()) {
            String code = albumTypeCode.trim().toLowerCase();
            spec = spec.and((root, query, cb) ->
                    cb.equal(cb.lower(root.get("albumType").get("code")), code)
            );
        }

        if (artistName != null && !artistName.isBlank()) {
            String a = artistName.trim().toLowerCase();
            spec = spec.and((root, query, cb) -> {
                query.distinct(true);
                var aa = root.join("artistAlbums", JoinType.INNER);
                var artist = aa.join("artist", JoinType.INNER);
                return cb.like(cb.lower(artist.get("name")), "%" + a + "%");
            });
        }

        return albumRepository.findAll(spec, pageable).map(this::toResponse);
    }

    private AlbumResponse toResponse(Album a) {
        return new AlbumResponse(
                a.getId(),
                a.getTitle(),
                a.getReleaseYear(),
                a.getGenre(),
                a.getAlbumType().getCode(),
                a.getAlbumType().getDescription(),
                a.getCreatedAt(),
                a.getUpdatedAt()
        );
    }
}
