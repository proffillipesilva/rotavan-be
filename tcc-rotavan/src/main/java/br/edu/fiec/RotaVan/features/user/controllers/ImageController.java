package br.edu.fiec.RotaVan.features.user.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static br.edu.fiec.RotaVan.utils.ImageUtils.THUMBNAIL_DIR;
import static br.edu.fiec.RotaVan.utils.ImageUtils.UPLOAD_DIR;

@Slf4j
@RestController
@RequestMapping("/images")
@Tag(name = "Imagens", description = "API para servir arquivos de imagem (uploads, thumbnails)")
public class ImageController {

    @Operation(summary = "Busca uma imagem pelo nome do arquivo (para UPLOAD_DIR ou THUMBNAIL_DIR)",
            description = "Retorna o arquivo de imagem (ou seu thumbnail) com base no nome. " +
                    "Este endpoint retorna a imagem diretamente, que pode ser usada em uma tag <img> no front-end. Requer autenticação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Arquivo de imagem retornado com sucesso.",
                    content = { @Content(mediaType = "image/png"), // Exemplo
                            @Content(mediaType = "image/jpeg") }), // Exemplo

            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "404", description = "Arquivo não encontrado com o nome fornecido (resource.exists() == false)."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor ao tentar ler o arquivo (IOException).")
    })
    @SecurityRequirement(name = "bearerAuth") // Assumindo que precisa de login para ver as fotos
    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getImage(
            @Parameter(description = "Nome do arquivo da imagem.", required = true)
            @PathVariable String filename,

            @Parameter(description = "Se 'true', busca a versão em miniatura (thumbnail) da imagem.", required = false)
            @RequestParam(value = "thumbnail", defaultValue = "false") boolean thumbnail) {
        try {
            String directory = thumbnail ? THUMBNAIL_DIR : UPLOAD_DIR;
            String completeFilename = thumbnail ? "thumb_" + filename : filename;

            Path filePath = Paths.get(directory, completeFilename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (IOException e) {
            log.error("Erro ao ler o ficheiro da imagem: " + filename, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}