package com.joelmofraga.artists_albums_api.album.controller;

import com.joelmofraga.artists_albums_api.album.controller.AlbumController;
import com.joelmofraga.artists_albums_api.album.dto.AlbumCreateRequest;
import com.joelmofraga.artists_albums_api.album.dto.AlbumResponse;
import com.joelmofraga.artists_albums_api.album.service.AlbumService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AlbumControllerUnitTest {

    private AlbumService albumService;
    private AlbumController controller;

    @BeforeEach
    void setUp() {
        albumService = mock(AlbumService.class);
        controller = new AlbumController(albumService);
    }

    @Test
    void create_deveRetornar201_eLocationHeader() {
        AlbumCreateRequest req = mock(AlbumCreateRequest.class);

        AlbumResponse created = mock(AlbumResponse.class);
        when(created.getId()).thenReturn(10L);

        when(albumService.create(req)).thenReturn(created);

        UriComponentsBuilder ucb = UriComponentsBuilder.newInstance();

        ResponseEntity<AlbumResponse> resp = controller.create(req, ucb);

        assertThat(resp.getStatusCode().value()).isEqualTo(201);
        assertThat(resp.getBody()).isSameAs(created);

        URI location = resp.getHeaders().getLocation();
        assertThat(location).isNotNull();
        assertThat(location.toString()).isEqualTo("/albums/10");

        verify(albumService).create(req);
        verifyNoMoreInteractions(albumService);
    }

    @Test
    void getById_deveRetornar200() {
        AlbumResponse album = mock(AlbumResponse.class);
        when(albumService.getById(1L)).thenReturn(album);

        ResponseEntity<AlbumResponse> resp = controller.getById(1L);

        assertThat(resp.getStatusCode().value()).isEqualTo(200);
        assertThat(resp.getBody()).isSameAs(album);

        verify(albumService).getById(1L);
        verifyNoMoreInteractions(albumService);
    }
}
