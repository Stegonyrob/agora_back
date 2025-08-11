package de.stella.agora_web.events.controller.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import de.stella.agora_web.attendee.controller.dto.AttendeeDTO;
import de.stella.agora_web.image.controller.dto.EventImageDTO;
import de.stella.agora_web.tags.dto.TagSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para eventos públicos que incluye imágenes. Se usa específicamente para
 * endpoints públicos donde se necesita mostrar eventos con sus imágenes
 * asociadas.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicEventDTO {

    private Long id;
    private String title;
    private String message;
    private boolean archived;
    private List<AttendeeDTO> attendees;
    private int capacity;
    private int attendeesCount;
    private List<TagSummaryDTO> tags;
    private List<EventImageDTO> images; // ✅ Campo específico para imágenes

    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDateTime creationDate;

    private Long lovesCount; // ✅ Contador de likes para estadísticas
}
