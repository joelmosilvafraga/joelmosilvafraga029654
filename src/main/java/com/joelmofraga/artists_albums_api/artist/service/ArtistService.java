package com.joelmofraga.artists_albums_api.artist.service;

import com.joelmofraga.artists_albums_api.artist.domain.Artist;
import com.joelmofraga.artists_albums_api.artist.dto.ArtistCreateRequest;
import com.joelmofraga.artists_albums_api.artist.dto.ArtistResponse;
import com.joelmofraga.artists_albums_api.artist.repository.ArtistRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
public class ArtistService {

    private final ArtistRepository artistRepository;

    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Transactional
    public ArtistResponse create(ArtistCreateRequest request) {
        Artist artist = new Artist();
        artist.setName(request.getName().trim());
        artist.setCountry(request.getCountry() != null ? request.getCountry().trim() : null);
        artist.setGenre(request.getGenre() != null ? request.getGenre().trim() : null);

        Artist saved = artistRepository.save(artist);

        return new ArtistResponse(
                saved.getId(),
                saved.getName(),
                saved.getCountry(),
                saved.getGenre(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }

    public Page<ArtistResponse> listAll(int page, int size, String sortDir) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        PageRequest pageable = PageRequest.of(page, size, Sort.by(direction, "name"));

        return artistRepository.findAll(pageable)
                .map(a -> new ArtistResponse(
                        a.getId(),
                        a.getName(),
                        a.getCountry(),
                        a.getGenre(),
                        a.getCreatedAt(),
                        a.getUpdatedAt()
                ));
    }


    public List<ArtistResponse> searchByName(String name, String sortDir) {
        Sort.Direction direction = parseDirection(sortDir);
        Sort sort = Sort.by(direction, "name");

        List<Artist> artists = artistRepository.findByNameContainingIgnoreCase(name, sort);

        return artists.stream()
                .map(a -> new ArtistResponse(
                        a.getId(),
                        a.getName(),
                        a.getCountry(),
                        a.getGenre(),
                        a.getCreatedAt(),
                        a.getUpdatedAt()
                ))
                .toList();
    }

    private Sort.Direction parseDirection(String sortDir) {
        if (sortDir == null) return Sort.Direction.ASC;
        if ("desc".equalsIgnoreCase(sortDir)) return Sort.Direction.DESC;
        return Sort.Direction.ASC;
    }

    public ArtistResponse getById(Long id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found: " + id));
        return toResponse(artist);
    }

    @Transactional
    public ArtistResponse update(Long id, ArtistCreateRequest request) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found: " + id));

        artist.setName(request.getName().trim());
        artist.setCountry(request.getCountry() != null ? request.getCountry().trim() : null);
        artist.setGenre(request.getGenre() != null ? request.getGenre().trim() : null);

        Artist saved = artistRepository.save(artist); // @PreUpdate ajusta updatedAt
        return toResponse(saved);
    }


    private ArtistResponse toResponse(Artist a) {
        return new ArtistResponse(
                a.getId(),
                a.getName(),
                a.getCountry(),
                a.getGenre(),
                a.getCreatedAt(),
                a.getUpdatedAt()
        );
    }

}
