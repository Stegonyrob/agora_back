package de.stella.agora_web.events.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.events.controller.dto.PublicEventDTO;
import de.stella.agora_web.events.service.IPublicEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador específico para eventos públicos. Cumple con SRP: Solo maneja la
 * exposición pública de eventos.
 */
@Slf4j
@RestController
@RequestMapping("${api-endpoint}/public/events")
@RequiredArgsConstructor
public class PublicEventController {

    private final IPublicEventService publicEventService;

    /**
     * Obtiene todos los eventos públicos con sus imágenes. Este endpoint es
     * consumido por el frontend para mostrar la lista de eventos.
     *
     * @return Lista de eventos públicos con imágenes incluidas
     */
    @GetMapping
    public ResponseEntity<List<PublicEventDTO>> getPublicEvents() {
        try {
            List<PublicEventDTO> events = publicEventService.getAllPublicEventsWithImages();
            log.info("Se obtuvieron {} eventos públicos", events.size());
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            log.error("Error al obtener eventos públicos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ✅ NUEVO: Obtiene eventos públicos paginados con imágenes. Endpoint
     * público sin autenticación para el frontend.
     *
     * @param pageable Parámetros de paginación
     * @return Página de eventos públicos con imágenes
     */
    @GetMapping("/paginated")
    public ResponseEntity<Page<PublicEventDTO>> getPublicEventsPaginated(Pageable pageable) {
        try {
            Page<PublicEventDTO> eventsPage = publicEventService.getAllPublicEventsWithImagesPaginated(pageable);
            log.info("Se obtuvieron {} eventos públicos paginados (página {}, tamaño {})",
                    eventsPage.getNumberOfElements(), pageable.getPageNumber(), pageable.getPageSize());
            return ResponseEntity.ok(eventsPage);
        } catch (Exception e) {
            log.error("Error al obtener eventos públicos paginados: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtiene un evento público específico con sus imágenes.
     *
     * @param id ID del evento
     * @return Evento público con imágenes
     */
    @GetMapping("/{id}")
    public ResponseEntity<PublicEventDTO> getPublicEvent(@PathVariable Long id) {
        try {
            PublicEventDTO event = publicEventService.getPublicEventWithImages(id);
            log.info("Se obtuvo el evento público con ID: {}", id);
            return ResponseEntity.ok(event);
        } catch (RuntimeException _) {
            log.warn("Evento público no encontrado con ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al obtener evento público con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtiene los eventos más populares basados en la cantidad de likes. Útil
     * para mostrar eventos destacados o estadísticas.
     *
     * @param limit Número máximo de eventos a devolver (por defecto 10)
     * @return Lista de eventos más populares
     */
    @GetMapping("/popular")
    public ResponseEntity<List<PublicEventDTO>> getMostPopularEvents(
            @RequestParam(defaultValue = "10") int limit) {
        try {
            if (limit <= 0 || limit > 100) {
                limit = 10; // Valor por defecto si el límite es inválido
            }

            List<PublicEventDTO> events = publicEventService.getMostPopularEvents(limit);
            log.info("Se obtuvieron {} eventos populares", events.size());
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            log.error("Error al obtener eventos populares: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
