package de.stella.agora_web.events.controller.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime creationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate eventDate;
    private String eventTime;
    private int capacity;
    private boolean archived;
    private String userUsername;
    private List<TagSummaryDTO> tags;
    private List<String> images; // Unificado con PostResponseDTO
    private int participantsCount;

    public EventResponseDTO(Long id, String title, String message, LocalDateTime creationDate, LocalDate eventDate, String eventTime, int capacity, boolean archived, String userUsername, List<TagSummaryDTO> tags, List<String> images, int participantsCount) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.creationDate = creationDate;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.capacity = capacity;
        this.archived = archived;
        this.userUsername = userUsername;
        this.tags = tags;
        this.images = images;
        this.participantsCount = participantsCount;

        // Debug logs
        System.out.println("[DEBUG] EventResponseDTO - eventDate: " + eventDate);
        System.out.println("[DEBUG] EventResponseDTO - eventTime: " + eventTime);
    }
}