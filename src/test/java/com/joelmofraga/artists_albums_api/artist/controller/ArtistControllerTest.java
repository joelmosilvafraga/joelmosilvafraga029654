package com.joelmofraga.artists_albums_api.artist.controller;

import com.joelmofraga.artists_albums_api.artist.dto.ArtistCreateRequest;
import com.joelmofraga.artists_albums_api.artist.dto.ArtistResponse;
import com.joelmofraga.artists_albums_api.artist.service.ArtistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ArtistControllerTest {

    private ArtistService artistService;
    private ArtistController controller;

    @BeforeEach
    void setUp() {
        artistService = mock(ArtistService.class);
        controller = new ArtistController(artistService);
    }

    @Test
    void create_deveRetornar201_comLocationEBody() {

        ArtistCreateRequest req = new ArtistCreateRequest();
        req.setName("U2");
        req.setCountry("Ireland");
        req.setGenre("Rock");

        ArtistResponse created = new ArtistResponse(
                10L, "U2", "Ireland", "Rock",
                Instant.parse("2020-01-01T00:00:00Z"),
                Instant.parse("2020-01-02T00:00:00Z")
        );

        when(artistService.create(req)).thenReturn(created);

        UriComponentsBuilder ucb = UriComponentsBuilder.newInstance();


        ResponseEntity<ArtistResponse> resp = controller.create(req, ucb);

        assertThat(resp.getStatusCode().value()).isEqualTo(201);
        assertThat(resp.getBody()).isSameAs(created);

        URI location = resp.getHeaders().getLocation();
        assertThat(location).isNotNull();
        assertThat(location.toString()).isEqualTo("/artists/10");
        verify(artistService).create(req);
        verifyNoMoreInteractions(artistService);
    }

    @Test
    void searchByName_quandoNameVazio_deveRetornar400_semChamarService() {
        ResponseEntity<List<ArtistResponse>> resp = controller.searchByName("   ", "asc");

        assertThat(resp.getStatusCode().value()).isEqualTo(400);
        verifyNoInteractions(artistService);
    }

    @Test
    void searchByName_quandoSortInvalido_deveRetornar400_semChamarService() {
        ResponseEntity<List<ArtistResponse>> resp = controller.searchByName("U2", "invalid");

        assertThat(resp.getStatusCode().value()).isEqualTo(400);
        verifyNoInteractions(artistService);
    }

    @Test
    void searchByName_quandoValido_deveRetornar200_eChamarServiceComTrim() {
        List<ArtistResponse> result = List.of(
                new ArtistResponse(1L, "U2", "Ireland", "Rock", null, null)
        );

        when(artistService.searchByName("U2", "desc")).thenReturn(result);

        ResponseEntity<List<ArtistResponse>> resp = controller.searchByName("  U2  ", "desc");

        assertThat(resp.getStatusCode().value()).isEqualTo(200);
        assertThat(resp.getBody()).isSameAs(result);

        verify(artistService).searchByName("U2", "desc");
        verifyNoMoreInteractions(artistService);
    }

    @Test
    void listAll_quandoPageInvalida_deveRetornar400_semChamarService() {
        ResponseEntity<Page<ArtistResponse>> resp = controller.listAll(-1, 10, "asc");

        assertThat(resp.getStatusCode().value()).isEqualTo(400);
        verifyNoInteractions(artistService);
    }

    @Test
    void listAll_quandoSizeInvalido_deveRetornar400_semChamarService() {
        ResponseEntity<Page<ArtistResponse>> resp1 = controller.listAll(0, 0, "asc");
        ResponseEntity<Page<ArtistResponse>> resp2 = controller.listAll(0, 101, "asc");

        assertThat(resp1.getStatusCode().value()).isEqualTo(400);
        assertThat(resp2.getStatusCode().value()).isEqualTo(400);

        verifyNoInteractions(artistService);
    }

    @Test
    void listAll_quandoSortInvalido_deveRetornar400_semChamarService() {
        ResponseEntity<Page<ArtistResponse>> resp = controller.listAll(0, 10, "xpto");

        assertThat(resp.getStatusCode().value()).isEqualTo(400);
        verifyNoInteractions(artistService);
    }

    @Test
    void listAll_quandoValido_deveRetornar200() {
        @SuppressWarnings("unchecked")
        Page<ArtistResponse> page = mock(Page.class);

        when(artistService.listAll(0, 10, "asc")).thenReturn(page);

        ResponseEntity<Page<ArtistResponse>> resp = controller.listAll(0, 10, "asc");

        assertThat(resp.getStatusCode().value()).isEqualTo(200);
        assertThat(resp.getBody()).isSameAs(page);

        verify(artistService).listAll(0, 10, "asc");
        verifyNoMoreInteractions(artistService);
    }

    @Test
    void getById_deveRetornar200() {
        ArtistResponse artist = new ArtistResponse(1L, "Titãs", "Brasil", "Rock", null, null);
        when(artistService.getById(1L)).thenReturn(artist);

        ResponseEntity<ArtistResponse> resp = controller.getById(1L);

        assertThat(resp.getStatusCode().value()).isEqualTo(200);
        assertThat(resp.getBody()).isSameAs(artist);

        verify(artistService).getById(1L);
        verifyNoMoreInteractions(artistService);
    }

    @Test
    void update_deveRetornar200() {
        ArtistCreateRequest req = new ArtistCreateRequest();
        req.setName("Novo Nome");
        req.setCountry("BR");
        req.setGenre("Rock");

        ArtistResponse updated = new ArtistResponse(1L, "Novo Nome", "BR", "Rock", null, null);
        when(artistService.update(1L, req)).thenReturn(updated);

        ResponseEntity<ArtistResponse> resp = controller.update(1L, req);

        assertThat(resp.getStatusCode().value()).isEqualTo(200);
        assertThat(resp.getBody()).isSameAs(updated);

        verify(artistService).update(1L, req);
        verifyNoMoreInteractions(artistService);
    }
}
