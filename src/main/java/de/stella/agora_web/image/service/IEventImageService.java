package de.stella.agora_web.image.service;

import java.util.List;

import de.stella.agora_web.image.controller.dto.EventImageDTO;

public interface IEventImageService {
    EventImageDTO getEventImageById(Long id);

    List<EventImageDTO> getImagesByEventId(Long eventId);

    EventImageDTO saveEventImage(EventImageDTO dto);

    void deleteEventImage(Long id);

    public void deleteImagesByEventId(Long id);
}