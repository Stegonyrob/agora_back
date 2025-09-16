package de.stella.agora_web.image.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import de.stella.agora_web.events.model.Event;
import de.stella.agora_web.events.repository.EventRepository;
import de.stella.agora_web.image.controller.dto.EventImageDTO;
import de.stella.agora_web.image.module.EventImage;
import de.stella.agora_web.image.repository.EventImageRepository;
import de.stella.agora_web.image.service.IEventImageService;
import de.stella.agora_web.image.service.ImageStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EventImageServiceImpl implements IEventImageService {

    private final EventImageRepository eventImageRepository;
    private final EventRepository eventRepository;
    private final ImageStorageService imageStorageService;

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg", "image/png", "image/gif", "image/webp"
    );

    @Override
    public EventImageDTO getEventImageById(Long id) {
        EventImage eventImage = eventImageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("EventImage not found"));
        return mapToDTO(eventImage);
    }

    @Override
    public List<EventImageDTO> getImagesByEventId(Long eventId) {
        return eventImageRepository.findByEventId(eventId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EventImageDTO saveEventImage(EventImageDTO dto) {
        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        EventImage eventImage = EventImage.builder()
                .imageName(dto.getImageName())
                .imagePath(dto.getImagePath())
                .event(event)
                .build();

        EventImage savedImage = eventImageRepository.save(eventImage);
        return mapToDTO(savedImage);
    }

    @Override
    public void deleteEventImage(Long id) {
        eventImageRepository.deleteById(id);
    }

    @Override
    public void deleteMultipleEventImages(List<Long> imageIds) {
        eventImageRepository.deleteAllById(imageIds);
    }

    @Override
    public void deleteImagesByEventId(Long id) {
        List<EventImage> images = eventImageRepository.findByEventId(id);
        eventImageRepository.deleteAll(images);
    }

    @Override
    public List<EventImageDTO> processAndSaveImages(MultipartFile[] files, Long eventId) {
        List<EventImageDTO> savedImages = new ArrayList<>();

        if (files == null || files.length == 0) {
            return savedImages;
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        for (MultipartFile file : files) {
            if (!file.isEmpty() && isValidImageFile(file)) {
                String relativePath = imageStorageService.storeImage(file);

                EventImage eventImage = EventImage.builder()
                        .imageName(file.getOriginalFilename())
                        .imagePath(relativePath)
                        .event(event)
                        .build();

                EventImage savedImage = eventImageRepository.save(eventImage);
                savedImages.add(mapToDTO(savedImage));
            }
        }

        return savedImages;
    }

    @Override
    public boolean isValidImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase());
    }

    @Override
    public byte[] getEventImageData(Long id) {
        throw new UnsupportedOperationException("Binary image data is no longer supported.");
    }

    private EventImageDTO mapToDTO(EventImage eventImage) {
        return EventImageDTO.builder()
                .id(eventImage.getId())
                .imageName(eventImage.getImageName())
                .imagePath(eventImage.getImagePath())
                .eventId(eventImage.getEvent().getId())
                .build();
    }
}
