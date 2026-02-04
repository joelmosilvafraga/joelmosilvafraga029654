package com.joelmofraga.artists_albums_api.album.controller;

import com.joelmofraga.artists_albums_api.album.dto.AlbumCreateRequest;
import com.joelmofraga.artists_albums_api.album.dto.AlbumResponse;
import com.joelmofraga.artists_albums_api.album.dto.AlbumUpdateRequest;
import com.joelmofraga.artists_albums_api.album.service.AlbumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.joelmofraga.artists_albums_api.config.ApiPaths.ALBUMS;

@Tag(name = "Albums", description = "Endpoints de álbuns")
@RestController
@RequestMapping(ALBUMS)
public class AlbumController {

    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @Operation(summary = "Criar álbum", description = "Cria um novo álbum.")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AlbumResponse> create(
            @Valid @RequestBody AlbumCreateRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        AlbumResponse created = albumService.create(request);

        URI location = uriBuilder
                .path("/albums/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Buscar álbum por ID")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AlbumResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(albumService.getById(id));
    }

    @Operation(summary = "Atualizar álbum")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AlbumResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody AlbumUpdateRequest request
    ) {
        return ResponseEntity.ok(albumService.update(id, request));
    }

    @Operation(summary = "Listar álbuns (paginado) com filtros")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<AlbumResponse>> list(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String albumTypeCode,
            @RequestParam(required = false) String artistName,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(albumService.list(title, albumTypeCode, artistName, pageable));
    }

    @Operation(summary = "Buscar álbum por título", description = "Retorna um álbum a partir do título (case-insensitive).")
    @GetMapping(value = "/by-title/{title}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AlbumResponse> getByTitle(@PathVariable String title) {
        return ResponseEntity.ok(albumService.getByTitle(title));
    }

    @GetMapping("/by-artist")
    public ResponseEntity<List<AlbumResponse>> getAlbumsByArtist(@RequestParam("name") String artistName) {
        return ResponseEntity.ok(albumService.getAlbumsByArtistName(artistName));
    }
}
