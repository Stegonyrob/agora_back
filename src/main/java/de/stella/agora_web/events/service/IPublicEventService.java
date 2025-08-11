package de.stella.agora_web.events.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.stella.agora_web.events.controller.dto.PublicEventDTO;

/**
 * Servicio específico para la gestión de eventos públicos. Cumple con el
 * principio de Responsabilidad Única (SRP).
 */
public interface IPublicEventService {

    /**
     * Obtiene todos los eventos públicos (no archivados) con sus imágenes
     * asociadas.
     *
     * @return Lista de eventos públicos con imágenes
     */
    List<PublicEventDTO> getAllPublicEventsWithImages();

    /**
     * ✅ NUEVO: Obtiene eventos públicos paginados con sus imágenes asociadas.
     *
     * @param pageable Parámetros de paginación
     * @return Página de eventos públicos con imágenes
     */
    Page<PublicEventDTO> getAllPublicEventsWithImagesPaginated(Pageable pageable);

    /**
     * Obtiene un evento público específico con sus imágenes asociadas.
     *
     * @param id ID del evento
     * @return Evento público con imágenes
     * @throws RuntimeException si el evento no existe o está archivado
     */
    PublicEventDTO getPublicEventWithImages(Long id);

    /**
     * Obtiene eventos públicos ordenados por popularidad (número de likes).
     *
     * @param limit Número máximo de eventos a devolver
     * @return Lista de eventos más populares
     */
    List<PublicEventDTO> getMostPopularEvents(int limit);
}
