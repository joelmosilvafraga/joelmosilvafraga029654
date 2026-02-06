package com.joelmofraga.artists_albums_api.track.service;

import com.joelmofraga.artists_albums_api.album.domain.Album;
import com.joelmofraga.artists_albums_api.album.repository.AlbumRepository;
import com.joelmofraga.artists_albums_api.track.domain.Track;
import com.joelmofraga.artists_albums_api.track.dto.TrackBatchCreateRequest;
import com.joelmofraga.artists_albums_api.track.dto.TrackCreateRequest;
import com.joelmofraga.artists_albums_api.track.dto.TrackResponse;
import com.joelmofraga.artists_albums_api.track.repository.TrackRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class TrackService {

    private final TrackRepository trackRepository;
    private final AlbumRepository albumRepository;

    public TrackService(TrackRepository trackRepository, AlbumRepository albumRepository) {
        this.trackRepository = trackRepository;
        this.albumRepository = albumRepository;
    }


    @Transactional(readOnly = true)
    public List<TrackResponse> listByAlbum(Long albumId) {

        if (!albumRepository.existsById(albumId)) {
            throw new IllegalArgumentException("Álbum não encontrado: " + albumId);
        }

        return trackRepository.findAllByAlbumIdOrderByTrackNumberAsc(albumId)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    @Transactional
    public List<TrackResponse> addAllToAlbum(Long albumId, TrackBatchCreateRequest request) {

        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new IllegalArgumentException("Álbum não encontrado: " + albumId));

        validateBatchRequest(request);

        int nextNumber = trackRepository.findMaxTrackNumberByAlbumId(albumId) + 1;

        List<Track> toSave = new ArrayList<>(request.tracks().size());
        for (TrackCreateRequest dto : request.tracks()) {
            Track t = new Track();
            t.setAlbum(album);
            t.setTrackNumber(nextNumber++);
            t.setTitle(dto.title().trim());
            t.setDurationSeconds(dto.durationSeconds());
            toSave.add(t);
        }

        try {
            List<Track> saved = trackRepository.saveAll(toSave);
            saved.sort(Comparator.comparing(Track::getTrackNumber));
            return saved.stream().map(this::toResponse).toList();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Conflito ao inserir faixas (numeração já utilizada no álbum).");
        }
    }

    private void validateBatchRequest(TrackBatchCreateRequest request) {
        if (request == null || request.tracks() == null || request.tracks().isEmpty()) {
            throw new IllegalArgumentException("Lista de tracks é obrigatória e não pode ser vazia.");
        }

        Set<String> seen = new HashSet<>();
        for (TrackCreateRequest dto : request.tracks()) {
            if (dto == null) throw new IllegalArgumentException("Item de track inválido (null).");

            String title = dto.title() == null ? "" : dto.title().trim();
            if (title.isBlank()) {
                throw new IllegalArgumentException("title é obrigatório em todas as tracks.");
            }

            String normalized = title.toLowerCase(Locale.ROOT);
            if (!seen.add(normalized)) {
                throw new IllegalArgumentException("Títulos duplicados no request: '" + title + "'");
            }

            if (dto.durationSeconds() != null && dto.durationSeconds() < 0) {
                throw new IllegalArgumentException("durationSeconds deve ser >= 0.");
            }
        }
    }

    private TrackResponse toResponse(Track t) {
        return new TrackResponse(
                t.getId(),
                t.getAlbum().getId(),
                t.getTrackNumber(),
                t.getTitle(),
                t.getDurationSeconds(),
                t.getCreatedAt()
        );
    }
}
