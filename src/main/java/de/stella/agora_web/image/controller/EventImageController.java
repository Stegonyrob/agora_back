package de.stella.agora_web.image.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import de.stella.agora_web.image.controller.dto.EventImageDTO;
import de.stella.agora_web.image.controller.dto.ImageIdListDTO;
import de.stella.agora_web.image.service.IEventImageService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api-endpoint}/event-images")
@RequiredArgsConstructor
public class EventImageController {

    private final IEventImageService eventImageService;

    // ========== ENDPOINTS PÚBLICOS (Eventos son públicos) ==========
    /**
     * Obtiene todas las imágenes de un evento - PÚBLICO SRP: Responsabilidad
     * única de listar imágenes por evento
     */
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<EventImageDTO>> getImagesByEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventImageService.getImagesByEventId(eventId));
    }

    /**
     * Obtiene información de una imagen específica - PÚBLICO SRP:
     * Responsabilidad única de obtener metadata de imagen
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventImageDTO> getEventImage(@PathVariable Long id) {
        return ResponseEntity.ok(eventImageService.getEventImageById(id));
    }

    /**
     * Sirve los datos binarios de una imagen - PÚBLICO SRP: Responsabilidad
     * única de servir contenido binario OCP: Extensible para diferentes tipos
     * de imagen
     */
    @GetMapping("/{id}/data")
    public ResponseEntity<byte[]> getEventImageData(@PathVariable Long id) {
        try {
            EventImageDTO image = eventImageService.getEventImageById(id);

            // Cargar datos binarios desde la ruta de la imagen
            byte[] imageData = eventImageService.getEventImageData(id);

            // Determinar content type basado en extensión
            String contentType = determineContentType(image.getImageName());

            return ResponseEntity.ok()
                    .header("Content-Type", contentType)
                    .header("Content-Disposition", "inline; filename=\"" + image.getImageName() + "\"")
                    .body(imageData);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ========== ENDPOINTS ADMINISTRATIVOS ==========
    /**
     * Crea una imagen para un evento - SOLO ADMIN SRP: Responsabilidad única de
     * crear imagen ISP: Interfaz segregada para operaciones administrativas
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventImageDTO> uploadEventImage(@RequestBody EventImageDTO dto) {
        return ResponseEntity.ok(eventImageService.saveEventImage(dto));
    }

    /**
     * Sube múltiples imágenes desde archivos - SOLO ADMIN SRP: Responsabilidad
     * única de procesar múltiples archivos DIP: Depende de abstracción
     * IEventImageService
     */
    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EventImageDTO>> uploadMultipleEventImages(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("eventId") Long eventId) {

        try {
            List<EventImageDTO> savedImages = eventImageService.processAndSaveImages(files, eventId);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedImages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Elimina una imagen específica - SOLO ADMIN SRP: Responsabilidad única de
     * eliminar imagen
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEventImage(@PathVariable Long id) {
        eventImageService.deleteEventImage(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Elimina múltiples imágenes - SOLO ADMIN SRP: Responsabilidad única de
     * eliminar múltiples imágenes
     */
    @DeleteMapping("/delete-multiple")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMultipleEventImages(@RequestBody ImageIdListDTO dto) {
        try {
            eventImageService.deleteMultipleEventImages(dto.getImageIds());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ========== MÉTODOS HELPER (SRP) ==========
    /**
     * Determina el content type basado en la extensión del archivo SRP:
     * Responsabilidad única de determinar tipo de contenido OCP: Abierto para
     * extensión de nuevos tipos
     */
    private String determineContentType(String imageName) {
        if (imageName == null) {
            return "image/jpeg"; // default
        }

        String extension = imageName.toLowerCase();
        if (extension.endsWith(".png")) {
            return "image/png";
        } else if (extension.endsWith(".gif")) {
            return "image/gif";
        } else if (extension.endsWith(".webp")) {
            return "image/webp";
        } else if (extension.endsWith(".bmp")) {
            return "image/bmp";
        }
        return "image/jpeg"; // default
    }
}
