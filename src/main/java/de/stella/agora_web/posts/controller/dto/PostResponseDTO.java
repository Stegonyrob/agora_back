package de.stella.agora_web.posts.controller.dto;

import java.time.LocalDateTime;
import java.util.List;

import de.stella.agora_web.tags.dto.TagSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDTO {

    private Long id;
    private String title;
    private String message;
    private String location;
    private LocalDateTime creationDate;
    private boolean archived;
    private boolean published;
    private int loves;
    private String userUsername;
    private String userFullName;
    private List<TagSummaryDTO> tags;
    private List<String> imageUrls; // Solo las URLs de las imágenes, no objetos completos
    private int repliesCount;
    private int commentsCount;
}
