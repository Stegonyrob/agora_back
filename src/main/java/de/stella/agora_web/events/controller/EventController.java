
package de.stella.agora_web.events.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import de.stella.agora_web.events.service.IEventService;

@RestController
@RequestMapping(path = "${api-endpoint}/")
public class EventController {

    private final IEventService eventService;

    public EventController(IEventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/events")
    public List<EventDTO> index() {
        return eventService.getAllEvents();
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<Event> show(@PathVariable Long id) {
        Event event = eventService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(event);
    }

    @PostMapping(path = "/events")
    @SuppressWarnings("CallToPrintStackTrace")
    public ResponseEntity<Event> create(@RequestBody EventDTO eventDTO) {
        if (eventDTO == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Event newEvent = eventService.save(eventDTO);
            System.out.println("Event recibido: " + eventDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON).body(newEvent);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("events/{id}")
    public ResponseEntity<EventDTO> update(@PathVariable("id") Long id, @RequestBody EventDTO eventDTO) {
        EventDTO event = eventService.updateEvent(id, eventDTO);
        return ResponseEntity.accepted().body(event);
    }

    @PatchMapping("/events/{id}/archive")
    public ResponseEntity<Void> archiveEvent(@PathVariable Long id, @RequestParam Boolean archive) {
        Event event = eventService.getById(id);
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

    @GetMapping("events/tag/{tagName}")
    public ResponseEntity<List<Event>> getEventsByTagName(@PathVariable String tagName) {
        List<Event> events = eventService.getEventsByTagName(tagName);
        return ResponseEntity.ok(events);
    }

    public ResponseEntity<EventDTO> createEvent(EventDTO eventDTO, long userId) {
        EventDTO newEvent = eventService.createEvent(eventDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON).body(newEvent);
    }

}
