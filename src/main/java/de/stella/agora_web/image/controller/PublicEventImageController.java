package de.stella.agora_web.image.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.image.controller.dto.EventImageDTO;
import de.stella.agora_web.image.service.IEventImageService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api-endpoint}/all/event-images")
@RequiredArgsConstructor
public class PublicEventImageController {

    private final IEventImageService eventImageService;

    @GetMapping("/{id}")
    public ResponseEntity<EventImageDTO> getEventImage(@PathVariable Long id) {
        return ResponseEntity.ok(eventImageService.getEventImageById(id));
    }

    // ✅ ENDPOINT PARA SERVIR LA IMAGEN COMO BYTES (MEJORADO)
    @GetMapping("/{id}/data")
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
            headers.set("Content-Disposition", "inline; filename=\"" + imageName + "\"");

            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
