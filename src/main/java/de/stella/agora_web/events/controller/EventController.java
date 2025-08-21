package de.stella.agora_web.events.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.events.controller.dto.EventDTO;
import de.stella.agora_web.events.controller.dto.EventResponseDTO;
import de.stella.agora_web.events.controller.dto.PublicEventDTO;
import de.stella.agora_web.events.model.Event;
import de.stella.agora_web.events.service.IEventService;
import de.stella.agora_web.events.service.IPublicEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador principal para operaciones CRUD básicas de eventos. Cumple con
 * SRP: Solo maneja operaciones básicas de eventos.
 */
@Slf4j
@RestController
@RequestMapping("${api-endpoint}/events")
@RequiredArgsConstructor
public class EventController {

    private final IEventService eventService;
    private final IPublicEventService publicEventService;

    /**
     * Obtiene todos los eventos con imágenes (para usuarios autenticados).
     * Incluye imágenes para que usuarios registrados puedan ver y gestionar el
     * contenido completo.
     *
     * @return Lista de eventos con imágenes
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<PublicEventDTO>> getAllEventsWithImages() {
        try {
            List<PublicEventDTO> events = publicEventService.getAllPublicEventsWithImages();
            log.info("Se obtuvieron {} eventos con imágenes para usuario autenticado", events.size());
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            log.error("Error al obtener eventos con imágenes: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ✅ NUEVO: Obtiene eventos paginados con imágenes (para usuarios
     * autenticados). Soluciona el problema de la ruta /events/paginated.
     *
     * @param pageable Parámetros de paginación
     * @return Página de eventos con imágenes
     */
    @GetMapping("/paginated")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Page<PublicEventDTO>> getPaginatedEventsWithImages(Pageable pageable) {
        try {
            Page<PublicEventDTO> eventsPage = publicEventService.getAllPublicEventsWithImagesPaginated(pageable);

            log.info("Se obtuvieron {} eventos paginados (página {}, tamaño {})",
                    eventsPage.getNumberOfElements(), pageable.getPageNumber(), pageable.getPageSize());
            return ResponseEntity.ok(eventsPage);
        } catch (Exception e) {
            log.error("Error al obtener eventos paginados: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtiene un evento específico con información detallada.
     *
     * @param id ID del evento
     * @return Evento con información detallada
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable Long id) {
        try {
            EventResponseDTO event = eventService.getEventResponseById(id);
            log.info("Se obtuvo evento con ID: {}", id);
            return ResponseEntity.ok(event);
        } catch (RuntimeException e) {
            log.warn("Evento no encontrado con ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al obtener evento con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Crea un nuevo evento.
     *
     * @param eventDTO Datos del evento a crear
     * @return Evento creado
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Event> createEvent(@RequestBody EventDTO eventDTO) {
        if (eventDTO == null) {
            log.warn("Intento de crear evento con datos nulos");
            return ResponseEntity.badRequest().build();
        }

        log.info("Payload recibido para crear evento: {}", eventDTO);

        try {
            Event newEvent = new Event();
            newEvent.setTitle(eventDTO.getTitle());
            newEvent.setMessage(eventDTO.getMessage());
            newEvent.setEventDate(eventDTO.getEventDate()); // Mapeo del campo eventDate
            newEvent.setEventTime(eventDTO.getEventTime()); // Mapeo del campo eventTime
            newEvent.setCapacity(eventDTO.getCapacity());
            newEvent.setArchived(eventDTO.isArchived());

            Event savedEvent = eventService.save(newEvent);
            log.info("Evento creado exitosamente con ID: {}", savedEvent.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
        } catch (Exception e) {
            log.error("Error al crear evento: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Actualiza un evento existente.
     *
     * @param id ID del evento a actualizar
     * @param eventDTO Nuevos datos del evento
     * @return Evento actualizado
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable Long id, @RequestBody EventDTO eventDTO) {
        if (eventDTO == null) {
            log.warn("Intento de actualizar evento con datos nulos");
            return ResponseEntity.badRequest().build();
        }

        try {
            EventDTO updatedEvent = eventService.updateEvent(id, eventDTO);
            log.info("Evento actualizado exitosamente con ID: {}", id);
            return ResponseEntity.ok(updatedEvent);
        } catch (RuntimeException e) {
            log.warn("Evento no encontrado para actualizar con ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al actualizar evento con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
