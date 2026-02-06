package com.joelmofraga.artists_albums_api.media.controller;

import com.joelmofraga.artists_albums_api.media.dto.AlbumCoverUploadResponse;
import com.joelmofraga.artists_albums_api.media.service.AlbumCoverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.joelmofraga.artists_albums_api.config.ApiPaths.MEDIA;

@Tag(
        name = "Album Media",
        description = """
                Gerenciamento de mídias associadas aos álbuns.
                Permite upload de capas e geração de links temporários (pré-assinados) para acesso às imagens armazenadas no S3/MinIO.
                """
)
@RestController
@RequestMapping(MEDIA)
public class AlbumCoverController {

    private final AlbumCoverService service;

    public AlbumCoverController(AlbumCoverService service) {
        this.service = service;
    }

    @Operation(
            summary = "Upload da capa do álbum",
            description = """
                    Realiza o upload de uma imagem de capa para o álbum informado.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Upload realizado com sucesso",
                    content = @Content(schema = @Schema(implementation = AlbumCoverUploadResponse.class))),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "404", description = "Álbum não encontrado")
    })
    @PostMapping(
            value = "/{albumId}/cover",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public AlbumCoverUploadResponse upload(
            @Parameter(description = "ID do álbum", example = "14")
            @PathVariable Long albumId,

            @Parameter(description = "Arquivo da imagem (png, jpg, webp)", required = true)
            @RequestPart("file") MultipartFile file
    ) throws Exception {

        return service.uploadCover(albumId, file);
    }

    @Operation(
            summary = "Obter link pré-assinado da capa",
            description = """
                    Retorna uma URL temporária (pré-assinada) para acesso direto à capa do álbum no S3/MinIO.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "URL gerada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "404", description = "Capa não encontrada para o álbum")
    })
    @GetMapping("/{albumId}/cover")
    public AlbumCoverService.PresignedUrlResponse getCoverUrl(
            @Parameter(description = "ID do álbum", example = "14")
            @PathVariable Long albumId
    ) {
        return service.getCoverPresignedUrl(albumId);
    }
}
