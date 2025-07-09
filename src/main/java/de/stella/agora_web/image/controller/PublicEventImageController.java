package de.stella.agora_web.image.controller;

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

    // ✅ ENDPOINT PARA SERVIR LA IMAGEN COMO BYTES 
    @GetMapping("/{id}/data")
    public ResponseEntity<byte[]> getEventImageData(@PathVariable Long id) {
        try {
            EventImageDTO image = eventImageService.getEventImageById(id);

            return ResponseEntity.ok()
                    .header("Content-Type", "image/jpeg") // Podríamos determinar el tipo real
                    .header("Content-Disposition", "inline; filename=\"" + image.getImageName() + "\"")
                    .body(image.getImageData());

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
