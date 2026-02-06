package com.joelmofraga.artists_albums_api.album.service;

import com.joelmofraga.artists_albums_api.album.domain.Album;
import com.joelmofraga.artists_albums_api.album.domain.AlbumType;
import com.joelmofraga.artists_albums_api.album.dto.AlbumCreateRequest;
import com.joelmofraga.artists_albums_api.album.dto.AlbumUpdateRequest;
import com.joelmofraga.artists_albums_api.album.repository.AlbumRepository;
import com.joelmofraga.artists_albums_api.album.repository.AlbumTypeRepository;
import com.joelmofraga.artists_albums_api.websocket.dto.AlbumCreatedEvent;
import com.joelmofraga.artists_albums_api.websocket.notifier.AlbumWsNotifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlbumServiceTest {

    @Mock private AlbumRepository albumRepository;
    @Mock private AlbumTypeRepository albumTypeRepository;
    @Mock private AlbumWsNotifier notifier;

    @InjectMocks
    private AlbumService service;

    @Captor
    private ArgumentCaptor<AlbumCreatedEvent> eventCaptor;


    @Test
    void create_quandoAlbumTypeValidoAtivo_salva_notifica_eRetornaResponse() {
        AlbumCreateRequest req = new AlbumCreateRequest();
        req.setAlbumTypeCode(" LP ");
        req.setTitle("  The Joshua Tree  ");
        req.setReleaseYear(1987);
        req.setGenre("  Rock  ");

        AlbumType type = mock(AlbumType.class);
        when(type.getActive()).thenReturn(true);
        when(type.getCode()).thenReturn("LP");
        when(type.getDescription()).thenReturn("Long Play");

        when(albumTypeRepository.findByCodeIgnoreCase("LP")).thenReturn(Optional.of(type));

        Instant createdAt = Instant.parse("2020-01-01T00:00:00Z");
        Instant updatedAt = Instant.parse("2020-01-02T00:00:00Z");

        Album saved = mock(Album.class);
        when(saved.getId()).thenReturn(10L);
        when(saved.getTitle()).thenReturn("The Joshua Tree");
        when(saved.getReleaseYear()).thenReturn(1987);
        when(saved.getGenre()).thenReturn("Rock");
        when(saved.getAlbumType()).thenReturn(type);
        when(saved.getCreatedAt()).thenReturn(createdAt);
        when(saved.getUpdatedAt()).thenReturn(updatedAt);

        when(albumRepository.save(any(Album.class))).thenReturn(saved);

        var resp = service.create(req);

        assertThat(resp).isNotNull();
        assertThat(resp.getId()).isEqualTo(10L);
        assertThat(resp.getTitle()).isEqualTo("The Joshua Tree");
        assertThat(resp.getReleaseYear()).isEqualTo(1987);
        assertThat(resp.getGenre()).isEqualTo("Rock");
        assertThat(resp.getAlbumTypeCode()).isEqualTo("LP");
        assertThat(resp.getAlbumTypeDescription()).isEqualTo("Long Play");
        assertThat(resp.getCreatedAt()).isEqualTo(createdAt);
        assertThat(resp.getUpdatedAt()).isEqualTo(updatedAt);

        verify(notifier).notifyAlbumCreated(eventCaptor.capture());
        AlbumCreatedEvent evt = eventCaptor.getValue();

        assertThat(evt.albumId()).isEqualTo(10L);
        assertThat(evt.title()).isEqualTo("The Joshua Tree");
        assertThat(evt.releaseYear()).isEqualTo(1987);
        assertThat(evt.createdAt()).isEqualTo(createdAt);

        verify(albumTypeRepository).findByCodeIgnoreCase("LP");
        verify(albumRepository).save(any(Album.class));
        verifyNoMoreInteractions(albumTypeRepository, albumRepository, notifier);
    }

    @Test
    void create_quandoAlbumTypeInvalido_retorna400_eNaoSalvaNemNotifica() {
        AlbumCreateRequest req = new AlbumCreateRequest();
        req.setAlbumTypeCode(" XYZ ");
        req.setTitle("Any");
        req.setReleaseYear(2000);

        when(albumTypeRepository.findByCodeIgnoreCase("XYZ")).thenReturn(Optional.empty());

        ResponseStatusException ex = catchThrowableOfType(() -> service.create(req), ResponseStatusException.class);

        assertThat(ex.getStatusCode().value()).isEqualTo(400);
        assertThat(ex.getReason()).contains("Invalid albumTypeCode");

        verify(albumTypeRepository).findByCodeIgnoreCase("XYZ");
        verifyNoInteractions(albumRepository, notifier);
    }

    @Test
    void create_quandoAlbumTypeInativo_retorna400_eNaoSalvaNemNotifica() {
        AlbumCreateRequest req = new AlbumCreateRequest();
        req.setAlbumTypeCode(" LP ");
        req.setTitle("Any");
        req.setReleaseYear(2000);

        AlbumType type = mock(AlbumType.class);
        when(type.getActive()).thenReturn(false);

        when(albumTypeRepository.findByCodeIgnoreCase("LP")).thenReturn(Optional.of(type));

        ResponseStatusException ex = catchThrowableOfType(() -> service.create(req), ResponseStatusException.class);

        assertThat(ex.getStatusCode().value()).isEqualTo(400);
        assertThat(ex.getReason()).contains("Invalid albumTypeCode");

        verify(albumTypeRepository).findByCodeIgnoreCase("LP");
        verifyNoInteractions(albumRepository, notifier);
    }



    @Test
    void getById_quandoNaoExiste_retorna404() {
        when(albumRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseStatusException ex = catchThrowableOfType(() -> service.getById(999L), ResponseStatusException.class);

        assertThat(ex.getStatusCode().value()).isEqualTo(404);
        assertThat(ex.getReason()).contains("Album not found");

        verify(albumRepository).findById(999L);
        verifyNoMoreInteractions(albumRepository);
        verifyNoInteractions(albumTypeRepository, notifier);
    }


    @Test
    void getByTitle_quandoTitleVazio_retorna400() {
        ResponseStatusException ex = catchThrowableOfType(() -> service.getByTitle("   "), ResponseStatusException.class);

        assertThat(ex.getStatusCode().value()).isEqualTo(400);
        assertThat(ex.getReason()).contains("title is required");

        verifyNoInteractions(albumRepository, albumTypeRepository, notifier);
    }



    @Test
    void update_quandoAlbumNaoExiste_retorna404() {
        when(albumRepository.findById(10L)).thenReturn(Optional.empty());

        AlbumUpdateRequest req = new AlbumUpdateRequest();
        req.setAlbumTypeCode("LP");
        req.setTitle("New");
        req.setReleaseYear(2000);

        ResponseStatusException ex = catchThrowableOfType(() -> service.update(10L, req), ResponseStatusException.class);

        assertThat(ex.getStatusCode().value()).isEqualTo(404);

        verify(albumRepository).findById(10L);
        verifyNoMoreInteractions(albumRepository);
        verifyNoInteractions(albumTypeRepository, notifier);
    }



    @Test
    void list_retornaPageMapeada_semAmbiguidadeDeFindAll() {
        Pageable pageable = PageRequest.of(0, 10);

        AlbumType type = mock(AlbumType.class);
        when(type.getCode()).thenReturn("LP");
        when(type.getDescription()).thenReturn("Long Play");

        Album a = mock(Album.class);
        when(a.getId()).thenReturn(1L);
        when(a.getTitle()).thenReturn("War");
        when(a.getReleaseYear()).thenReturn(1983);
        when(a.getGenre()).thenReturn(null);
        when(a.getAlbumType()).thenReturn(type);
        when(a.getCreatedAt()).thenReturn(null);
        when(a.getUpdatedAt()).thenReturn(null);

        Page<Album> albumsPage = new PageImpl<>(List.of(a), pageable, 1);

        doReturn(albumsPage)
                .when(albumRepository)
                .findAll(Mockito.<Specification<Album>>any(), eq(pageable));

        var page = service.list("wa", "LP", "U2", pageable);

        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().get(0).getId()).isEqualTo(1L);
        assertThat(page.getContent().get(0).getTitle()).isEqualTo("War");

        verify(albumRepository).findAll(Mockito.<Specification<Album>>any(), eq(pageable));
        verifyNoMoreInteractions(albumRepository);
        verifyNoInteractions(albumTypeRepository, notifier);
    }
}
