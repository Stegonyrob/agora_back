package de.stella.agora_web.events.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import de.stella.agora_web.events.controller.dto.EventDTO;
import de.stella.agora_web.events.model.Event;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventDTO toDto(Event event);

    List<EventDTO> toDtoList(List<Event> events);

    Event toEntity(EventDTO eventDTO);
}
