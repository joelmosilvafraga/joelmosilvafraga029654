package com.joelmofraga.artists_albums_api.track.controller;

import com.joelmofraga.artists_albums_api.track.dto.TrackBatchCreateRequest;
import com.joelmofraga.artists_albums_api.track.dto.TrackResponse;
import com.joelmofraga.artists_albums_api.track.service.TrackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.joelmofraga.artists_albums_api.config.ApiPaths.ALBUMS;

@Tag(
        name = "Tracks",
        description = """
                Gerenciamento de faixas (músicas) de um álbum.
                """
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping(ALBUMS)
public class TrackController {

    private final TrackService service;

    public TrackController(TrackService service) {
        this.service = service;
    }


    @Operation(
            summary = "Adicionar faixas em lote ao álbum",
            description = """
                    Insere todas as músicas do álbum de uma única vez.
                    """
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = @ExampleObject(
                            name = "Exemplo de batch",
                            value = """
                                    {
                                      "tracks": [
                                        { "title": "Intro", "durationSeconds": 60 },
                                        { "title": "Battery", "durationSeconds": 312 },
                                        { "title": "Master of Puppets", "durationSeconds": 515 }
                                      ]
                                    }
                                    """
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Faixas criadas com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Álbum não encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito de regras de negócio", content = @Content)
    })
    @PostMapping(
            value = "/{albumId}/tracks/batch",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> addAllTracks(
            @Parameter(description = "ID do álbum", example = "14")
            @PathVariable Long albumId,

            @RequestBody @Valid TrackBatchCreateRequest request
    ) {
        try {
            List<TrackResponse> created = service.addAllToAlbum(albumId, request);
            return ResponseEntity.status(201).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }


    @Operation(
            summary = "Listar faixas do álbum",
            description = """
                    Retorna todas as músicas associadas ao álbum.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "ID inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Álbum não encontrado", content = @Content)
    })
    @GetMapping(
            value = "/{albumId}/tracks",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> listByAlbum(
            @Parameter(description = "ID do álbum", example = "14")
            @PathVariable Long albumId
    ) {
        try {
            return ResponseEntity.ok(service.listByAlbum(albumId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
