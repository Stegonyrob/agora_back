package de.stella.agora_web.events.controller.dto;

import java.time.LocalDateTime;
import java.util.List;

import de.stella.agora_web.tags.dto.TagSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventResponseDTO {

    private Long id;
    private String title;
    private String message;
    private String location;
    private LocalDateTime creationDate;
    private LocalDateTime eventDate;
    private int capacity;
    private boolean archived;
    private String userUsername;
    private String userFullName;
    private List<TagSummaryDTO> tags;
    private List<String> images; // Unificado con PostResponseDTO
    private int participantsCount;
}
