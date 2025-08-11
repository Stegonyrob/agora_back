package de.stella.agora_web.events.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.events.model.Event;
import de.stella.agora_web.events.service.IEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador específico para la gestión de archivado de eventos. Cumple con
 * SRP: Solo maneja operaciones de archivado/desarchivado.
 */
@Slf4j
@RestController
@RequestMapping("${api-endpoint}/events")
@RequiredArgsConstructor
public class EventArchiveController {

    private final IEventService eventService;

    /**
     * Archiva o desarchivar un evento.
     *
     * @param id ID del evento
     * @param archive true para archivar, false para desarchivar
     * @return ResponseEntity vacío
     */
    @PatchMapping("/{id}/archive")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Void> archiveEvent(@PathVariable Long id, @RequestParam Boolean archive) {
        try {
            Event event = eventService.getById(id);
            if (event == null) {
                log.warn("Evento no encontrado con ID: {}", id);
                return ResponseEntity.notFound().build();
            }

            if (archive) {
                eventService.archiveEvent(id);
                log.info("Evento {} archivado exitosamente", id);
            } else {
                eventService.unArchiveEvent(id);
                log.info("Evento {} desarchivado exitosamente", id);
            }

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error al {} evento con ID {}: {}",
                    archive ? "archivar" : "desarchivar", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
