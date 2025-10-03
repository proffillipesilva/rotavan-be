package br.edu.fiec.RotaVan.features.user.controllers;

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
public class ImageController {

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename, @RequestParam(value = "thumbnail", defaultValue = "false") boolean thumbnail) {
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