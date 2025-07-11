package de.stella.agora_web.events.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import de.stella.agora_web.events.controller.dto.EventDTO;
import de.stella.agora_web.events.model.Event;
import de.stella.agora_web.tags.dto.TagSummaryDTO;

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
        dto.setCreationDate(event.getCreationDate());
        // Set tags
        if (event.getTags() != null) {
            dto.setTags(event.getTags().stream()
                    .map(tag -> new TagSummaryDTO(tag.getId(), tag.getName(), tag.getArchived() != null && tag.getArchived()))
                    .collect(Collectors.toList()));
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
        event.setCreationDate(eventDTO.getCreationDate());
        // Tags will be set in the service layer, as it requires DB lookup
        return event;
    }
}
