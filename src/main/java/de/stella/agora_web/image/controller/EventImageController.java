package de.stella.agora_web.image.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import de.stella.agora_web.image.service.IEventImageService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api-endpoint}/any/event-images")
@RequiredArgsConstructor
public class EventImageController {

    private final IEventImageService eventImageService;

    @GetMapping("/event/{eventId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<EventImageDTO>> getImagesByEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventImageService.getImagesByEventId(eventId));
    }

    // ✅ ENDPOINT PARA SERVIR DATOS DE IMAGEN (NECESARIO PARA FRONTEND)
    @GetMapping("/{id}/data")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<byte[]> getEventImageData(@PathVariable Long id) {
        try {
            byte[] imageData = eventImageService.getEventImageData(id);

            // Obtener información de la imagen para establecer el content type correcto
            EventImageDTO imageInfo = eventImageService.getEventImageById(id);
            String imageName = imageInfo.getImageName();

            // Determinar content type basado en la extensión
            MediaType mediaType = MediaType.IMAGE_JPEG; // default
            if (imageName != null) {
                String extension = imageName.toLowerCase();
                if (extension.endsWith(".png")) {
                    mediaType = MediaType.IMAGE_PNG;
                } else if (extension.endsWith(".gif")) {
                    mediaType = MediaType.IMAGE_GIF;
                } else if (extension.endsWith(".webp")) {
                    mediaType = MediaType.valueOf("image/webp");
                }
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType);
            headers.setContentLength(imageData.length);

            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<EventImageDTO> uploadEventImage(@RequestBody EventImageDTO dto) {
        return ResponseEntity.ok(eventImageService.saveEventImage(dto));
    }

    // ✅ ENDPOINT PARA SUBIR MÚLTIPLES IMÁGENES A UN EVENTO
    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
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

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Void> deleteEventImage(@PathVariable Long id) {
        eventImageService.deleteEventImage(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ ENDPOINT PARA ELIMINAR MÚLTIPLES IMÁGENES
    @DeleteMapping("/delete-multiple")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Void> deleteMultipleEventImages(@RequestBody de.stella.agora_web.image.dtos.ImageIdListDTO dto) {

        try {
            eventImageService.deleteMultipleEventImages(dto.getImageIds());

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
