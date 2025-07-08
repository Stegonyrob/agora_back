package de.stella.agora_web.image.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    // ✅ CONSTANTES PARA VALIDACIÓN (SIGUIENDO EL PATRÓN DE AVATARES)
    private static final List<String> ALLOWED_CONTENT_TYPES = List.of(
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
    );
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final int MAX_IMAGES_PER_EVENT = 10;

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

    @Override
    public void deleteMultipleEventImages(List<Long> imageIds) {
        if (imageIds == null || imageIds.isEmpty()) {
            return;
        }

        // Verificar que todas las imágenes existen antes de eliminar
        for (Long id : imageIds) {
            if (!eventImageRepository.existsById(id)) {
                throw new EventImageNotFoundException("Event image not found with id: " + id);
            }
        }

        // Eliminar todas las imágenes
        eventImageRepository.deleteAllById(imageIds);
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

    // ✅ IMPLEMENTACIÓN DE NUEVOS MÉTODOS
    @Override
    public List<EventImageDTO> processAndSaveImages(MultipartFile[] files, Long eventId) {
        List<EventImageDTO> savedImages = new ArrayList<>();

        if (files == null || files.length == 0) {
            return savedImages;
        }

        // Verificar que el evento existe
        eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with id: " + eventId));

        // Verificar límite de imágenes
        long currentImageCount = eventImageRepository.findByEventId(eventId).size();
        if (currentImageCount + files.length > MAX_IMAGES_PER_EVENT) {
            throw new IllegalArgumentException("Máximo " + MAX_IMAGES_PER_EVENT + " imágenes por evento");
        }

        for (MultipartFile file : files) {
            if (!file.isEmpty() && isValidImageFile(file)) {
                try {
                    // Generar nombre único
                    String originalFilename = file.getOriginalFilename();
                    String extension = getFileExtension(originalFilename);
                    String uniqueFilename = UUID.randomUUID().toString() + "." + extension;

                    // Crear EventImageDTO
                    EventImageDTO imageDTO = EventImageDTO.builder()
                            .imageName(uniqueFilename)
                            .imageData(file.getBytes())
                            .eventId(eventId)
                            .build();

                    // Guardar usando método existente
                    EventImageDTO savedImage = saveEventImage(imageDTO);
                    savedImages.add(savedImage);

                } catch (IOException e) {
                    throw new RuntimeException("Error al procesar imagen: " + file.getOriginalFilename(), e);
                }
            }
        }

        return savedImages;
    }

    @Override
    public boolean isValidImageFile(MultipartFile file) {
        if (file.isEmpty() || file.getSize() > MAX_FILE_SIZE) {
            return false;
        }

        String contentType = file.getContentType();
        return contentType != null && ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase());
    }

    private String getFileExtension(String filename) {
        if (filename == null) {
            return "";
        }
        int lastDotIndex = filename.lastIndexOf('.');
        return (lastDotIndex != -1) ? filename.substring(lastDotIndex + 1) : "";
    }
}
