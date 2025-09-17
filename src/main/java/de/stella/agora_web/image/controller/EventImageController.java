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

/**
 * Controlador para imágenes de eventos - REPLICANDO TextImageController ✅
 * DIFERENCIA: Events son públicos para GET, resto ADMIN PATRÓN: Exactamente
 * igual que TextImageController pero eventos públicos
 */
@RestController
@RequestMapping("${api-endpoint}/event-images")
@RequiredArgsConstructor
public class EventImageController {

    private final IEventImageService eventImageService;

    // ========== ENDPOINTS PÚBLICOS - SIGUIENDO PATRÓN TEXT-IMAGES ==========
    /**
     * Obtiene todas las imágenes de un evento - PÚBLICO PATRÓN: Exactamente
     * igual que TextImageController.getImagesByTextId()
     */
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<EventImageDTO>> getImagesByEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventImageService.getImagesByEventId(eventId));
    }

    /**
     * Obtiene información de una imagen específica - PÚBLICO PATRÓN:
     * Exactamente igual que TextImageController.getTextImage()
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventImageDTO> getEventImage(@PathVariable Long id) {
        return ResponseEntity.ok(eventImageService.getEventImageById(id));
    }

    // ========== ENDPOINTS ADMINISTRATIVOS - SIGUIENDO PATRÓN TEXT-IMAGES ==========
    /**
     * Crea una imagen para un evento - SOLO ADMIN PATRÓN: Exactamente igual que
     * TextImageController.createTextImage()
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventImageDTO> uploadEventImage(@RequestBody EventImageDTO dto) {
        return ResponseEntity.ok(eventImageService.saveEventImage(dto));
    }

    /**
     * Sube múltiples imágenes desde archivos - SOLO ADMIN PATRÓN: Exactamente
     * igual que TextImageController.uploadMultipleTextImages()
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
     * Elimina una imagen específica - SOLO ADMIN PATRÓN: Exactamente igual que
     * TextImageController.deleteTextImage()
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEventImage(@PathVariable Long id) {
        eventImageService.deleteEventImage(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Elimina múltiples imágenes - SOLO ADMIN PATRÓN: Igual que
     * TextImageController pero usando ImageIdListDTO (ya existente)
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
}
