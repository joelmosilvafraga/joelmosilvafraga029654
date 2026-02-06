package com.joelmofraga.artists_albums_api.artist.service;

import com.joelmofraga.artists_albums_api.artist.domain.Artist;
import com.joelmofraga.artists_albums_api.artist.dto.ArtistCreateRequest;
import com.joelmofraga.artists_albums_api.artist.dto.ArtistResponse;
import com.joelmofraga.artists_albums_api.artist.repository.ArtistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ArtistServiceTest {

    private ArtistRepository artistRepository;
    private ArtistService service;

    @BeforeEach
    void setUp() {
        artistRepository = mock(ArtistRepository.class);
        service = new ArtistService(artistRepository);
    }

    @Test
    void create_deveTrimarCampos_eSalvar() {

        ArtistCreateRequest req = new ArtistCreateRequest();
        req.setName("  U2  ");
        req.setCountry("  Ireland  ");
        req.setGenre("  Rock  ");

        ArgumentCaptor<Artist> artistCaptor = ArgumentCaptor.forClass(Artist.class);

        Artist saved = new Artist();
        saved.setId(10L);
        saved.setName("U2");
        saved.setCountry("Ireland");
        saved.setGenre("Rock");

        when(artistRepository.save(artistCaptor.capture())).thenReturn(saved);


        ArtistResponse resp = service.create(req);


        Artist toSave = artistCaptor.getValue();
        assertThat(toSave.getName()).isEqualTo("U2");
        assertThat(toSave.getCountry()).isEqualTo("Ireland");
        assertThat(toSave.getGenre()).isEqualTo("Rock");


        assertThat(resp.getId()).isEqualTo(10L);
        assertThat(resp.getName()).isEqualTo("U2");
        assertThat(resp.getCountry()).isEqualTo("Ireland");
        assertThat(resp.getGenre()).isEqualTo("Rock");

        verify(artistRepository).save(any(Artist.class));
        verifyNoMoreInteractions(artistRepository);
    }

    @Test
    void create_quandoCountryEGenreNulos_deveManterNulo() {
        ArtistCreateRequest req = new ArtistCreateRequest();
        req.setName("  Titãs  ");
        req.setCountry(null);
        req.setGenre(null);

        ArgumentCaptor<Artist> artistCaptor = ArgumentCaptor.forClass(Artist.class);

        Artist saved = new Artist();
        saved.setId(1L);
        saved.setName("Titãs");
        saved.setCountry(null);
        saved.setGenre(null);

        when(artistRepository.save(artistCaptor.capture())).thenReturn(saved);


        ArtistResponse resp = service.create(req);


        Artist toSave = artistCaptor.getValue();
        assertThat(toSave.getName()).isEqualTo("Titãs");
        assertThat(toSave.getCountry()).isNull();
        assertThat(toSave.getGenre()).isNull();

        assertThat(resp.getId()).isEqualTo(1L);
        assertThat(resp.getName()).isEqualTo("Titãs");
        assertThat(resp.getCountry()).isNull();
        assertThat(resp.getGenre()).isNull();
    }

    @Test
    void listAll_sortDesc_deveChamarFindAllComSortDescPorName() {
        Artist a1 = new Artist();
        a1.setId(1L);
        a1.setName("B");

        Artist a2 = new Artist();
        a2.setId(2L);
        a2.setName("A");

        Page<Artist> page = new PageImpl<>(List.of(a1, a2));
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        when(artistRepository.findAll(pageableCaptor.capture())).thenReturn(page);


        Page<ArtistResponse> resp = service.listAll(0, 10, "desc");


        Pageable p = pageableCaptor.getValue();
        assertThat(p.getPageNumber()).isEqualTo(0);
        assertThat(p.getPageSize()).isEqualTo(10);

        Sort.Order order = p.getSort().getOrderFor("name");
        assertThat(order).isNotNull();
        assertThat(order.getDirection()).isEqualTo(Sort.Direction.DESC);


        assertThat(resp.getContent()).hasSize(2);
        assertThat(resp.getContent().get(0).getId()).isEqualTo(1L);
        assertThat(resp.getContent().get(0).getName()).isEqualTo("B");

        verify(artistRepository).findAll(any(Pageable.class));
        verifyNoMoreInteractions(artistRepository);
    }

    @Test
    void searchByName_sortAsc_devePassarSortAscParaRepository() {

        Artist a = new Artist();
        a.setId(1L);
        a.setName("U2");

        ArgumentCaptor<Sort> sortCaptor = ArgumentCaptor.forClass(Sort.class);
        when(artistRepository.findByNameContainingIgnoreCase(eq("U2"), sortCaptor.capture()))
                .thenReturn(List.of(a));


        List<ArtistResponse> resp = service.searchByName("U2", "asc");


        Sort sort = sortCaptor.getValue();
        Sort.Order order = sort.getOrderFor("name");
        assertThat(order).isNotNull();
        assertThat(order.getDirection()).isEqualTo(Sort.Direction.ASC);


        assertThat(resp).hasSize(1);
        assertThat(resp.get(0).getId()).isEqualTo(1L);
        assertThat(resp.get(0).getName()).isEqualTo("U2");
    }

    @Test
    void getById_quandoNaoExiste_deveLancar404() {

        when(artistRepository.findById(99L)).thenReturn(Optional.empty());


        assertThatThrownBy(() -> service.getById(99L))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> {
                    ResponseStatusException rse = (ResponseStatusException) ex;
                    assertThat(rse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                    assertThat(rse.getReason()).contains("Artist not found: 99");
                });

        verify(artistRepository).findById(99L);
        verifyNoMoreInteractions(artistRepository);
    }

    @Test
    void update_quandoNaoExiste_deveLancar404() {

        when(artistRepository.findById(10L)).thenReturn(Optional.empty());

        ArtistCreateRequest req = new ArtistCreateRequest();
        req.setName("X");


        assertThatThrownBy(() -> service.update(10L, req))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> assertThat(((ResponseStatusException) ex).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND));

        verify(artistRepository).findById(10L);
        verifyNoMoreInteractions(artistRepository);
    }

    @Test
    void update_deveTrimarCampos_eSalvar() {

        Artist existing = new Artist();
        existing.setId(10L);
        existing.setName("Old");
        existing.setCountry("X");
        existing.setGenre("Y");

        when(artistRepository.findById(10L)).thenReturn(Optional.of(existing));

        ArtistCreateRequest req = new ArtistCreateRequest();
        req.setName("  New Name  ");
        req.setCountry("  BR  ");
        req.setGenre("  Rock  ");

        ArgumentCaptor<Artist> artistCaptor = ArgumentCaptor.forClass(Artist.class);
        when(artistRepository.save(artistCaptor.capture())).thenAnswer(inv -> inv.getArgument(0));

        ArtistResponse resp = service.update(10L, req);


        Artist saved = artistCaptor.getValue();
        assertThat(saved.getId()).isEqualTo(10L);
        assertThat(saved.getName()).isEqualTo("New Name");
        assertThat(saved.getCountry()).isEqualTo("BR");
        assertThat(saved.getGenre()).isEqualTo("Rock");

        assertThat(resp.getId()).isEqualTo(10L);
        assertThat(resp.getName()).isEqualTo("New Name");
        assertThat(resp.getCountry()).isEqualTo("BR");
        assertThat(resp.getGenre()).isEqualTo("Rock");

        verify(artistRepository).findById(10L);
        verify(artistRepository).save(any(Artist.class));
        verifyNoMoreInteractions(artistRepository);
    }
}
