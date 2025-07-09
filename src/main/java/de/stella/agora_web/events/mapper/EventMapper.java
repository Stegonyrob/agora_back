package de.stella.agora_web.events.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import de.stella.agora_web.events.controller.dto.EventDTO;
import de.stella.agora_web.events.model.Event;

@Component
public class EventMapper {

    public EventDTO toDto(Event event) {
        if (event == null) {
            return null;
        }
        EventDTO dto = new EventDTO();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setMessage(event.getMessage());
        dto.setArchived(event.getArchived());
        dto.setCapacity(event.getCapacity());
        dto.setAttendeesCount(event.getAttendees() != null ? event.getAttendees().size() : 0);
        // --- MAPEO DE TAGS: de entidades a nombres ---
        if (event.getTags() != null) {
            dto.setTags(event.getTags().stream()
                    .filter(java.util.Objects::nonNull)
                    .map(de.stella.agora_web.tags.model.Tag::getName)
                    .collect(java.util.stream.Collectors.toList()));
        } else {
            dto.setTags(new java.util.ArrayList<>());
        }
        return dto;
    }

    public List<EventDTO> toDtoList(List<Event> events) {
        if (events == null) {
            return null;
        }
        return events.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Event toEntity(EventDTO eventDTO) {
        if (eventDTO == null) {
            return null;
        }
        Event event = new Event();
        event.setId(eventDTO.getId());
        event.setTitle(eventDTO.getTitle());
        event.setMessage(eventDTO.getMessage());
        event.setArchived(eventDTO.isArchived());
        event.setCapacity(eventDTO.getCapacity());
        // NOTA: Las tags se asignan en EventServiceImpl, no aquí, para poder usar el servicio de tags.
        return event;
    }
}
