package br.edu.fiec.RotaVan.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class ImageUtils {

    // Define os diretórios onde as imagens serão salvas na raiz do seu projeto
    public static final String UPLOAD_DIR = "uploads";
    public static final String THUMBNAIL_DIR = "thumbnails";
    public static final int THUMBNAIL_SIZE = 150; // Tamanho da miniatura em pixels

    /**
     * Salva uma imagem enviada, cria uma miniatura e retorna o nome do ficheiro gerado.
     * @param file O ficheiro de imagem recebido via MultipartFile.
     * @return O nome único do ficheiro salvo (sem o caminho).
     */
    public static String saveImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("O ficheiro não pode estar vazio.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Tipo de ficheiro inválido. Apenas imagens são permitidas.");
        }

        try {
            // Garante que os diretórios de upload existem
            createDirectoryIfNotExists(UPLOAD_DIR);
            createDirectoryIfNotExists(THUMBNAIL_DIR);

            String originalFileName = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFileName);

            // Gera um nome de ficheiro único para evitar conflitos
            String newFileName = UUID.randomUUID().toString() + "." + fileExtension;
            Path filePath = Paths.get(UPLOAD_DIR, newFileName);

            // Salva o ficheiro original
            Files.copy(file.getInputStream(), filePath);

            // Gera a miniatura (thumbnail)
            String thumbnailFileName = "thumb_" + newFileName;
            generateThumbnail(filePath.toFile(), thumbnailFileName);

            return newFileName;

        } catch (IOException e) {
            throw new RuntimeException("Falha ao processar e salvar a imagem: " + e.getMessage(), e);
        }
    }

    /**
     * Cria uma miniatura de uma imagem.
     */
    private static void generateThumbnail(File originalImageFile, String thumbnailFileName) throws IOException {
        BufferedImage originalImage = ImageIO.read(originalImageFile);
        File thumbnailFile = new File(THUMBNAIL_DIR, thumbnailFileName);
        Thumbnails.of(originalImage)
                .size(THUMBNAIL_SIZE, THUMBNAIL_SIZE)
                .keepAspectRatio(true)
                .toFile(thumbnailFile);
    }

    /**
     * Extrai a extensão de um nome de ficheiro.
     */
    private static String getFileExtension(String fileName) {
        if (fileName == null) return "";
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }

    /**
     * Cria um diretório se ele não existir.
     */
    private static void createDirectoryIfNotExists(String directoryName) {
        File directory = new File(directoryName);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
}