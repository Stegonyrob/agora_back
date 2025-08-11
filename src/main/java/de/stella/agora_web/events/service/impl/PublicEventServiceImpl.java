package de.stella.agora_web.events.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import de.stella.agora_web.events.controller.dto.PublicEventDTO;
import de.stella.agora_web.events.model.Event;
import de.stella.agora_web.events.repository.EventRepository;
import de.stella.agora_web.events.service.IEventLoveService;
import de.stella.agora_web.events.service.IPublicEventService;
import de.stella.agora_web.image.controller.dto.EventImageDTO;
import de.stella.agora_web.image.module.EventImage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para eventos públicos. Se enfoca únicamente en la
 * gestión de eventos para consumo público.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PublicEventServiceImpl implements IPublicEventService {

    private final EventRepository eventRepository;
    private final IEventLoveService eventLoveService;

    @Override
    public List<PublicEventDTO> getAllPublicEventsWithImages() {
        try {
            List<Event> events = eventRepository.findAllPublicEventsWithImages();
            return events.stream()
                    .map(this::convertToPublicEventDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al obtener eventos públicos: {}", e.getMessage());
            throw new RuntimeException("Error al obtener eventos públicos", e);
        }
    }

    @Override
    public Page<PublicEventDTO> getAllPublicEventsWithImagesPaginated(Pageable pageable) {
        try {
            Page<Event> eventsPage = eventRepository.findAllPublicEventsPaginated(pageable);

            // Convertir cada evento a PublicEventDTO y cargar sus imágenes individualmente
            return eventsPage.map(event -> {
                // Para cada evento de la página, cargar sus relaciones manualmente
                Event eventWithImages = eventRepository.findPublicEventWithImages(event.getId())
                        .orElse(event);
                return convertToPublicEventDTO(eventWithImages);
            });
        } catch (Exception e) {
            log.error("Error al obtener eventos públicos paginados: {}", e.getMessage());
            throw new RuntimeException("Error al obtener eventos públicos paginados", e);
        }
    }

    @Override
    public PublicEventDTO getPublicEventWithImages(Long id) {
        Event event = eventRepository.findPublicEventWithImages(id)
                .orElseThrow(() -> new RuntimeException("Evento público no encontrado con ID: " + id));

        return convertToPublicEventDTO(event);
    }

    @Override
    public List<PublicEventDTO> getMostPopularEvents(int limit) {
        List<Event> events = eventRepository.findAllPublicEventsWithImages();

        return events.stream()
                .map(this::convertToPublicEventDTO)
                .sorted((e1, e2) -> e2.getLovesCount().compareTo(e1.getLovesCount()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Convierte una entidad Event a PublicEventDTO. Carga las relaciones de
     * forma individual para evitar MultipleBagFetchException.
     *
     * @param event Entidad Event
     * @return PublicEventDTO con todos los datos necesarios
     */
    private PublicEventDTO convertToPublicEventDTO(Event event) {
        PublicEventDTO dto = new PublicEventDTO();

        // Datos básicos del evento
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setMessage(event.getMessage());
        dto.setArchived(event.isArchived());
        dto.setCapacity(event.getCapacity());
        dto.setCreationDate(event.getCreationDate());

        // ✅ CARGAR TAGS DE FORMA INDIVIDUAL si no están ya cargados
        if (event.getTags() == null || event.getTags().isEmpty()) {
            Event eventWithTags = eventRepository.findEventWithTags(event.getId()).orElse(event);
            if (eventWithTags.getTags() != null) {
                dto.setTags(eventWithTags.getTags().stream()
                        .map(tag -> new de.stella.agora_web.tags.dto.TagSummaryDTO(
                        tag.getId(),
                        tag.getName(),
                        tag.getArchived()
                ))
                        .collect(Collectors.toList()));
            }
        } else {
            dto.setTags(event.getTags().stream()
                    .map(tag -> new de.stella.agora_web.tags.dto.TagSummaryDTO(
                    tag.getId(),
                    tag.getName(),
                    tag.getArchived()
            ))
                    .collect(Collectors.toList()));
        }

        // ✅ IMÁGENES - Ya están cargadas por la consulta principal
        if (event.getImages() != null && !event.getImages().isEmpty()) {
            dto.setImages(event.getImages().stream()
                    .map(this::convertToEventImageDTOWithoutData)
                    .collect(Collectors.toList()));
        }

        // ✅ CARGAR ATTENDEES DE FORMA INDIVIDUAL si no están ya cargados
        if (event.getAttendees() == null || event.getAttendees().isEmpty()) {
            Event eventWithAttendees = eventRepository.findEventWithAttendees(event.getId()).orElse(event);
            if (eventWithAttendees.getAttendees() != null) {
                dto.setAttendeesCount(eventWithAttendees.getAttendees().size());
                dto.setAttendees(eventWithAttendees.getAttendees().stream()
                        .map(attendee -> new de.stella.agora_web.attendee.controller.dto.AttendeeDTO(
                        attendee.getId(),
                        attendee.getName(),
                        attendee.getPhone(),
                        attendee.getEmail()
                ))
                        .collect(Collectors.toList()));
            } else {
                dto.setAttendeesCount(0);
            }
        } else {
            dto.setAttendeesCount(event.getAttendees().size());
            dto.setAttendees(event.getAttendees().stream()
                    .map(attendee -> new de.stella.agora_web.attendee.controller.dto.AttendeeDTO(
                    attendee.getId(),
                    attendee.getName(),
                    attendee.getPhone(),
                    attendee.getEmail()
            ))
                    .collect(Collectors.toList()));
        }

        // ✅ CONTADOR DE LIKES usando el servicio dedicado
        dto.setLovesCount(eventLoveService.countLoves(event.getId()));

        return dto;
    }

    /**
     * Convierte EventImage a EventImageDTO sin incluir los datos binarios. Esto
     * reduce significativamente el tamaño del payload.
     *
     * @param image EventImage entity
     * @return EventImageDTO sin imageData
     */
    private EventImageDTO convertToEventImageDTOWithoutData(EventImage image) {
        EventImageDTO dto = new EventImageDTO();
        dto.setId(image.getId());
        dto.setEventId(image.getEvent().getId());
        dto.setImageName(image.getImageName());
        // ✅ NO incluir imageData para reducir payload
        dto.setImageData(null);
        return dto;
    }
}
