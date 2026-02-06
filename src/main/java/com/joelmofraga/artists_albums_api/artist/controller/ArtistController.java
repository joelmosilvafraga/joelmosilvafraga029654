package com.joelmofraga.artists_albums_api.artist.controller;

import com.joelmofraga.artists_albums_api.artist.dto.ArtistCreateRequest;
import com.joelmofraga.artists_albums_api.artist.dto.ArtistResponse;
import com.joelmofraga.artists_albums_api.artist.service.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.joelmofraga.artists_albums_api.config.ApiPaths.ARTISTS;

@Tag(name = "Artists", description = "Operação de Cadastro e consulta de artistas")
@RestController
@RequestMapping(ARTISTS)
public class ArtistController {

    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @Operation(summary = "Criar artista", description = "Cria um novo artista.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Criado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ArtistResponse.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content)
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArtistResponse> create(
            @Valid @RequestBody ArtistCreateRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        ArtistResponse created = artistService.create(request);

        URI location = uriBuilder
                .path("/artists/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @Operation(
            summary = "Buscar artistas por nome (asc/desc)",
            description = "Busca por parte do nome (case-insensitive) e ordena alfabeticamente por name."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ArtistResponse.class))),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content)
    })
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ArtistResponse>> searchByName(
            @RequestParam("name") String name,
            @RequestParam(value = "sort", required = false, defaultValue = "asc") String sort
    ) {
        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        if (!"asc".equalsIgnoreCase(sort) && !"desc".equalsIgnoreCase(sort)) {
            return ResponseEntity.badRequest().build();
        }

        List<ArtistResponse> result = artistService.searchByName(name.trim(), sort);
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "Listar artistas paginados (asc/desc)",
            description = "Lista todos os artistas com paginação e ordenação alfabética por nome."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ArtistResponse>> listAll(
            @Parameter(description = "Página (0..N)", example = "0")
            @RequestParam(value = "page", defaultValue = "0") int page,

            @Parameter(description = "Tamanho da página", example = "10")
            @RequestParam(value = "size", defaultValue = "10") int size,

            @Parameter(description = "Ordenação por nome: asc|desc", example = "asc")
            @RequestParam(value = "sort", defaultValue = "asc") String sort
    ) {
        if (page < 0) return ResponseEntity.badRequest().build();
        if (size <= 0 || size > 100) return ResponseEntity.badRequest().build();
        if (!"asc".equalsIgnoreCase(sort) && !"desc".equalsIgnoreCase(sort)) {
            return ResponseEntity.badRequest().build();
        }

        Page<ArtistResponse> result = artistService.listAll(page, size, sort);
        return ResponseEntity.ok(result);
    }


    @Operation(summary = "Buscar artista por id", description = "Retorna um artista pelo id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ArtistResponse.class))),
            @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content)
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArtistResponse> getById(
            @Parameter(description = "ID do artista", example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(artistService.getById(id));
    }

    @Operation(summary = "Atualizar artista", description = "Atualiza as informações de um artista pelo id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualizado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ArtistResponse.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content)
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArtistResponse> update(
            @Parameter(description = "ID do artista", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody ArtistCreateRequest request
    ) {
        return ResponseEntity.ok(artistService.update(id, request));
    }
}
