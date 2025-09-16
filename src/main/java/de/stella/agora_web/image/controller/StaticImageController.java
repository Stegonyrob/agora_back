package de.stella.agora_web.image.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * Controlador para servir imágenes estáticas desde el directorio temp_images
 * Maneja las URLs como: /api/v1/images/temp_images/{filename}
 */
@Slf4j
@RestController
@RequestMapping("${api-endpoint}/images")
public class StaticImageController {

    @Value("${app.images.static-path:temp_images}")
    private String staticImagesPath;

    /**
     * Sirve imágenes estáticas desde el directorio temp_images
     *
     * @param filename nombre del archivo de imagen
     * @return ResponseEntity con los datos de la imagen
     */
    @GetMapping("/temp_images/{filename:.+}")
    public ResponseEntity<Resource> getStaticImage(@PathVariable String filename) {
        try {
            // Construir la ruta al archivo
            Path imagePath = Paths.get(staticImagesPath, filename);

            // Verificar que el archivo existe
            if (!Files.exists(imagePath)) {
                log.warn("Imagen no encontrada: {}", imagePath);
                return ResponseEntity.notFound().build();
            }

            // Cargar el archivo como recurso
            Resource resource = new FileSystemResource(imagePath);

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            // Determinar el tipo de contenido basado en la extensión
            String contentType = determineContentType(filename);

            // Crear headers de respuesta
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, contentType);
            headers.add(HttpHeaders.CACHE_CONTROL, "public, max-age=31536000"); // Cache por 1 año
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);

        } catch (Exception e) {
            log.error("Error al servir imagen estática: {}", filename, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Determina el tipo de contenido basado en la extensión del archivo
     */
    private String determineContentType(String filename) {
        String extension = filename.toLowerCase();

        if (extension.endsWith(".jpg") || extension.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG_VALUE;
        } else if (extension.endsWith(".png")) {
            return MediaType.IMAGE_PNG_VALUE;
        } else if (extension.endsWith(".gif")) {
            return MediaType.IMAGE_GIF_VALUE;
        } else if (extension.endsWith(".webp")) {
            return "image/webp";
        } else if (extension.endsWith(".svg")) {
            return "image/svg+xml";
        }

        return MediaType.APPLICATION_OCTET_STREAM_VALUE; // Fallback
    }
}
