package de.stella.agora_web.events.service;

import java.util.List;

import de.stella.agora_web.events.controller.dto.EventDTO;

public interface IEventService {
    List<EventDTO> getAllEvents();

    EventDTO getEventById(Long id);

    EventDTO createEvent(EventDTO eventDTO);

    EventDTO updateEvent(Long id, EventDTO eventDTO);

    void deleteEvent(Long id);

}
