package de.stella.agora_web.image.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import de.stella.agora_web.events.model.Event;
import de.stella.agora_web.events.repository.EventRepository;
import de.stella.agora_web.image.controller.dto.EventImageDTO;
import de.stella.agora_web.image.exception.EventImageNotFoundException;
import de.stella.agora_web.image.module.EventImage;
import de.stella.agora_web.image.repository.EventImageRepository;
import de.stella.agora_web.image.service.IEventImageService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventImageServiceImpl implements IEventImageService {

    private final EventImageRepository eventImageRepository;
    private final EventRepository eventRepository;

    @Override
    public EventImageDTO getEventImageById(Long id) {
        EventImage image = eventImageRepository.findById(id)
                .orElseThrow(() -> new EventImageNotFoundException("Event image not found with id: " + id));
        return toDTO(image);
    }

    @Override
    public List<EventImageDTO> getImagesByEventId(Long eventId) {
        return eventImageRepository.findByEventId(eventId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public EventImageDTO saveEventImage(EventImageDTO dto) {
        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new IllegalArgumentException("Event not found with id: " + dto.getEventId()));
        EventImage image = EventImage.builder().id(dto.getId()).imageName(dto.getImageName())
                .imageData(dto.getImageData()).event(event).build();
        image = eventImageRepository.save(image);
        return toDTO(image);
    }

    @Override
    public void deleteEventImage(Long id) {
        if (!eventImageRepository.existsById(id)) {
            throw new EventImageNotFoundException("Event image not found with id: " + id);
        }
        eventImageRepository.deleteById(id);
    }

    private EventImageDTO toDTO(EventImage image) {
        return EventImageDTO.builder().id(image.getId()).imageName(image.getImageName()).imageData(image.getImageData())
                .eventId(image.getEvent().getId()).build();
    }

    @Override
    public void deleteImagesByEventId(Long eventId) {
        List<EventImage> images = eventImageRepository.findByEventId(eventId);
        images.forEach(image -> eventImageRepository.deleteById(image.getId()));
    }
}