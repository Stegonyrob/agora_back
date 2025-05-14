package de.stella.agora_web.image.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.image.controller.dto.EventImageDTO;
import de.stella.agora_web.image.service.IEventImageService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/event-images")
@RequiredArgsConstructor
public class EventImageController {

    private final IEventImageService eventImageService;

    @GetMapping("/{id}")
    public ResponseEntity<EventImageDTO> getEventImage(@PathVariable Long id) {
        return ResponseEntity.ok(eventImageService.getEventImageById(id));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<EventImageDTO>> getImagesByEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventImageService.getImagesByEventId(eventId));
    }

    @PostMapping
    public ResponseEntity<EventImageDTO> uploadEventImage(@RequestBody EventImageDTO dto) {
        return ResponseEntity.ok(eventImageService.saveEventImage(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventImage(@PathVariable Long id) {
        eventImageService.deleteEventImage(id);
        return ResponseEntity.noContent().build();
    }
}