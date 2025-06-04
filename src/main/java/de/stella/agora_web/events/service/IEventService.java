package de.stella.agora_web.events.service;

import java.util.List;

import de.stella.agora_web.events.controller.dto.EventDTO;
import de.stella.agora_web.events.model.Event;

public interface IEventService {

    List<EventDTO> getAllEvents();

    EventDTO getEventById(Long id);

    EventDTO createEvent(EventDTO eventDTO);

    EventDTO updateEvent(Long id, EventDTO eventDTO);

    void deleteEvent(Long id);

    EventDTO updateEventImage(Long eventId, String imagePath);

    void unArchiveEventt(Long id);

    void archiveEvent(Long id);

    Event getById(Long id);

    Event save(Event event);

    Event save(EventDTO eventDTO);

    List<Event> getEventsByTagName(String tagName);

}
