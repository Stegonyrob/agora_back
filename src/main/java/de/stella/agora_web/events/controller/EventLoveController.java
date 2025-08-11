package de.stella.agora_web.events.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.events.service.IEventLoveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador específico para la gestión de likes en eventos. Cumple con SRP:
 * Solo maneja operaciones relacionadas con likes.
 */
@Slf4j
@RestController
@RequestMapping("${api-endpoint}/events")
@RequiredArgsConstructor
public class EventLoveController {

    private final IEventLoveService eventLoveService;

    /**
     * Alterna el estado de like para un evento (toggle). Si ya existe lo quita,
     * si no existe lo agrega.
     *
     * @param eventId ID del evento
     * @param profileId ID del perfil (usuario logueado)
     * @return Información sobre la acción realizada
     */
    @PostMapping("/{eventId}/love/toggle")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<LoveResponse> toggleLove(@PathVariable Long eventId, @RequestParam Long profileId) {
        try {
            boolean liked = eventLoveService.toggleLove(eventId, profileId);
            Long totalLoves = eventLoveService.countLoves(eventId);

            LoveResponse response = new LoveResponse(liked, totalLoves);

            log.info("Toggle like: perfil {} {} evento {}", profileId,
                    liked ? "dio like a" : "quitó like de", eventId);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error en toggle like para evento {} y perfil {}: {}", eventId, profileId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtiene el estado de like para un evento y perfil específicos.
     *
     * @param eventId ID del evento
     * @param profileId ID del perfil
     * @return Estado del like y contador total
     */
    @GetMapping("/{eventId}/love/status")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<LoveResponse> getLoveStatus(@PathVariable Long eventId, @RequestParam Long profileId) {
        try {
            boolean hasLove = eventLoveService.hasLove(eventId, profileId);
            Long totalLoves = eventLoveService.countLoves(eventId);

            LoveResponse response = new LoveResponse(hasLove, totalLoves);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al obtener estado de like para evento {} y perfil {}: {}", eventId, profileId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtiene solo el contador de likes de un evento (endpoint público).
     *
     * @param eventId ID del evento
     * @return Número total de likes
     */
    @GetMapping("/{eventId}/loves/count")
    public ResponseEntity<Long> getLovesCount(@PathVariable Long eventId) {
        try {
            Long count = eventLoveService.countLoves(eventId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            log.error("Error al obtener contador de likes para evento {}: {}", eventId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * DTO para la respuesta de operaciones de like.
     */
    public static class LoveResponse {

        private boolean liked;
        private Long totalLoves;

        public LoveResponse(boolean liked, Long totalLoves) {
            this.liked = liked;
            this.totalLoves = totalLoves;
        }

        // Getters
        public boolean isLiked() {
            return liked;
        }

        public Long getTotalLoves() {
            return totalLoves;
        }

        // Setters
        public void setLiked(boolean liked) {
            this.liked = liked;
        }

        public void setTotalLoves(Long totalLoves) {
            this.totalLoves = totalLoves;
        }
    }
}
