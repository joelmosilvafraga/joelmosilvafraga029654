package com.joelmofraga.artists_albums_api.album.controller;

import com.joelmofraga.artists_albums_api.album.dto.AlbumCreateRequest;
import com.joelmofraga.artists_albums_api.album.dto.AlbumResponse;
import com.joelmofraga.artists_albums_api.album.dto.AlbumUpdateRequest;
import com.joelmofraga.artists_albums_api.album.service.AlbumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

@Tag(
        name = "Albums",
        description = """
                Operações de cadastro e consulta de álbuns.
                      """
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping(ALBUMS)
public class AlbumController {

    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @Operation(
            summary = "Criar álbum",
            description = "Cria um novo álbum e retorna **201** + header **Location** apontando para o recurso criado."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = @ExampleObject(
                            name = "Exemplo de criação",
                            value = """
                                    {
                                      "title": "Master of Puppets",
                                      "releaseDate": "1986-03-03",
                                      "albumTypeCode": "STUDIO",
                                      "artistIds": [1]
                                    }
                                    """
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Álbum criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Payload inválido (validações do request)", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissão (requer ADMIN/MANAGER)", content = @Content)
    })
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AlbumResponse> create(
            @Valid @RequestBody AlbumCreateRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        AlbumResponse created = albumService.create(request);

        URI location = uriBuilder
                .path(ALBUMS + "/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Buscar álbum por ID", description = "Retorna um álbum pelo seu identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Álbum encontrado"),
            @ApiResponse(responseCode = "404", description = "Álbum não encontrado", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content)
    })
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EDITOR')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AlbumResponse> getById(
            @Parameter(description = "ID do álbum", example = "14")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(albumService.getById(id));
    }

    @Operation(
            summary = "Atualizar álbum",
            description = "Atualiza um álbum existente e retorna **200**. Retorna **404** se o álbum não existir."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = @ExampleObject(
                            name = "Exemplo de atualização",
                            value = """
                                    {
                                      "title": "Master of Puppets (Remastered)",
                                      "albumTypeCode": "STUDIO"
                                    }
                                    """
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Álbum atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Payload inválido", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Sem permissão (requer ADMIN/MANAGER)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Álbum não encontrado", content = @Content)
    })
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AlbumResponse> update(
            @Parameter(description = "ID do álbum", example = "14")
            @PathVariable Long id,
            @Valid @RequestBody AlbumUpdateRequest request
    ) {
        return ResponseEntity.ok(albumService.update(id, request));
    }

    @Operation(
            summary = "Listar álbuns (paginado) com filtros",
            description = """
                    Lista álbuns com paginação e filtros opcionais.

                    **Filtros (opcionais)**
                    - `title`: filtra por título
                    - `albumTypeCode`: código do tipo (ex.: STUDIO, LIVE, EP)
                    - `artistName`: filtra por nome do artista

                    **Paginação**
                    - `page` (0-based), `size`, `sort`
                    - Exemplo: `sort=title,asc`
                    """
    )
    @Parameter(name = "page", in = ParameterIn.QUERY, description = "Número da página (0-based)", example = "0")
    @Parameter(name = "size", in = ParameterIn.QUERY, description = "Tamanho da página", example = "10")
    @Parameter(name = "sort", in = ParameterIn.QUERY, description = "Ordenação: campo,(asc|desc). Pode repetir.", example = "title,asc")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista paginada retornada com sucesso")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<AlbumResponse>> list(
            @Parameter(description = "Filtra por título do álbum (opcional)", example = "Master")
            @RequestParam(required = false) String title,

            @Parameter(description = "Código do tipo do álbum (opcional)", example = "STUDIO")
            @RequestParam(required = false) String albumTypeCode,

            @Parameter(description = "Nome do artista (opcional)", example = "Metallica")
            @RequestParam(required = false) String artistName,

            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(albumService.list(title, albumTypeCode, artistName, pageable));
    }

    @Operation(
            summary = "Buscar álbum por título",
            description = "Retorna um álbum pelo título (case-insensitive). Retorna **404** se não existir."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Álbum encontrado"),
            @ApiResponse(responseCode = "404", description = "Álbum não encontrado", content = @Content)
    })
    @GetMapping(value = "/by-title/{title}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AlbumResponse> getByTitle(
            @Parameter(description = "Título do álbum (case-insensitive)", example = "Master of Puppets")
            @PathVariable String title
    ) {
        return ResponseEntity.ok(albumService.getByTitle(title));
    }

    @Operation(
            summary = "Listar álbuns por artista",
            description = "Retorna todos os álbuns associados a um artista pelo nome."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Artista não encontrado (se sua regra for 404)", content = @Content)
    })
    @GetMapping(value = "/by-artist", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AlbumResponse>> getAlbumsByArtist(
            @Parameter(description = "Nome do artista", example = "Metallica")
            @RequestParam("name") String artistName
    ) {
        return ResponseEntity.ok(albumService.getAlbumsByArtistName(artistName));
    }
}
