
package de.stella.agora_web.events.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.events.controller.dto.EventDTO;
import de.stella.agora_web.events.model.Event;

@RestController
@RequestMapping(path = "${api-endpoint}/")
public class EventController {

    @Autowired
    private de.stella.agora_web.events.service.IEventService eventService;

    @GetMapping("/events")
    public List<EventDTO> index() {
        return eventService.getAllEvents();
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<Event> show(@PathVariable Long id) {
        Event event = eventService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(event);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "events/")
    public ResponseEntity<de.stella.agora_web.events.controller.dto.EventDTO> createEvent(
            @RequestBody de.stella.agora_web.events.controller.dto.EventDTO eventDTO) {
        return ResponseEntity.ok(eventService.createEvent(eventDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<de.stella.agora_web.events.controller.dto.EventDTO> updateEvent(@PathVariable Long id,
            @RequestBody de.stella.agora_web.events.controller.dto.EventDTO eventDTO) {
        return ResponseEntity.ok(eventService.updateEvent(id, eventDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{eventId}/image")
    public ResponseEntity<EventDTO> updateEventImage(@PathVariable Long eventId,
            @RequestParam("imagePath") String imagePath) {
        EventDTO updatedEvent = eventService.updateEventImage(eventId, imagePath);
        return ResponseEntity.ok(updatedEvent);
    }

    @PatchMapping("/events/{id}/archive")
    public ResponseEntity<Void> archivePost(@PathVariable Long id, @RequestParam Boolean archive) {
        EventDTO event = eventService.getEventById(id);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            if (archive) {
                eventService.archiveEvent(id);
            } else {
                eventService.unArchiveEventt(id);
            }
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
