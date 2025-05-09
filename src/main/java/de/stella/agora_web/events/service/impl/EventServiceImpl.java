package de.stella.agora_web.events.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.stella.agora_web.events.controller.dto.EventDTO;
import de.stella.agora_web.events.model.Event;
import de.stella.agora_web.events.repository.EventRepository;
import de.stella.agora_web.events.service.IEventService;

@Service
public class EventServiceImpl implements IEventService {

    @Autowired
    private EventRepository eventRepository;

    private EventDTO convertToDTO(Event event) {
        EventDTO dto = new EventDTO();
        if (event != null) {
            dto.setId(event.getId());
            dto.setTitle(event.getTitle());
            dto.setMessage(event.getMessage());
            dto.setArchived(event.getArchived());
        }
        return dto;
    }

    private Event convertToEntity(EventDTO dto) {
        Event event = new Event();
        event.setId(dto.getId());
        event.setTitle(dto.getTitle());
        event.setMessage(dto.getMessage());
        event.setArchived(dto.isArchived());
        return event;
    }

    @Override
    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public EventDTO getEventById(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        return convertToDTO(event);
    }

    @Override
    public EventDTO createEvent(EventDTO eventDTO) {
        Event event = convertToEntity(eventDTO);
        Event savedEvent = eventRepository.save(event);
        return convertToDTO(savedEvent);
    }

    @Override
    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new RuntimeException("Event not found");
        }
        eventRepository.deleteById(id);
    }

    @Override
    public EventDTO updateEvent(Long id, EventDTO eventDTO) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        event.setTitle(eventDTO.getTitle());
        event.setMessage(eventDTO.getMessage());
        event.setArchived(eventDTO.isArchived());
        Event savedEvent = eventRepository.save(event);
        return convertToDTO(savedEvent);
    }
}