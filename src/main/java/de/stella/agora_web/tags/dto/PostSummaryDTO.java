package de.stella.agora_web.tags.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostSummaryDTO {

    private Long id;
    private String title;
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime creationDate;
    private boolean archived;
    private String username;
    private List<TagSummaryDTO> tags;

    // Constructor for TagServiceImpl and getAll
    public PostSummaryDTO(Long id, String title, String message, LocalDateTime creationDate, boolean archived, String username, List<TagSummaryDTO> tags) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.creationDate = creationDate;
        this.archived = archived;
        this.username = username;
        this.tags = tags;
    }

    // Existing constructor for JPQL projection mapping (keep for compatibility)
    public PostSummaryDTO(Long id, String title, String message, LocalDateTime creationDate, boolean archived, int commentsCount, int favoritesCount, Long userId) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.creationDate = creationDate;
        this.archived = archived;
        // Removed fields: userId, commentsCount, favoritesCount (no longer present in class)
    }
}
