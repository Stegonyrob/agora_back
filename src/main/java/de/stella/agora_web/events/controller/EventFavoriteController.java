package de.stella.agora_web.events.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.events.controller.dto.EventDTO;
import de.stella.agora_web.events.service.IEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador específico para la gestión de favoritos de eventos. Cumple con
 * SRP: Solo maneja operaciones relacionadas con favoritos.
 */
@Slf4j
@RestController
@RequestMapping("${api-endpoint}/events")
@RequiredArgsConstructor
public class EventFavoriteController {

    private final IEventService eventService;

    /**
     * Marca un evento como favorito para un usuario.
     *
     * @param eventId ID del evento
     * @param userId ID del usuario
     * @return ResponseEntity vacío
     */
    @PutMapping("/{eventId}/favorite")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Void> favoriteEvent(@PathVariable Long eventId, @RequestParam Long userId) {
        try {
            eventService.addFavorite(eventId, userId);
            log.info("Usuario {} marcó evento {} como favorito", userId, eventId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error al marcar evento {} como favorito para usuario {}: {}", eventId, userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Quita un evento de los favoritos de un usuario.
     *
     * @param eventId ID del evento
     * @param userId ID del usuario
     * @return ResponseEntity vacío
     */
    @PutMapping("/{eventId}/unfavorite")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Void> unfavoriteEvent(@PathVariable Long eventId, @RequestParam Long userId) {
        try {
            eventService.removeFavorite(eventId, userId);
            log.info("Usuario {} quitó evento {} de favoritos", userId, eventId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error al quitar evento {} de favoritos para usuario {}: {}", eventId, userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtiene todos los eventos favoritos de un usuario.
     *
     * @param userId ID del usuario
     * @return Lista de eventos favoritos
     */
    @GetMapping("/users/{userId}/favorites")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<EventDTO>> getFavoriteEvents(@PathVariable Long userId) {
        try {
            List<EventDTO> favorites = eventService.getFavoriteEventsByUser(userId);
            log.info("Se obtuvieron {} eventos favoritos para usuario {}", favorites.size(), userId);
            return ResponseEntity.ok(favorites);
        } catch (Exception e) {
            log.error("Error al obtener eventos favoritos para usuario {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
