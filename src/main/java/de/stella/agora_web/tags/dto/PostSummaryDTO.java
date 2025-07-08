package de.stella.agora_web.tags.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostSummaryDTO {

    private Long id;
    private String title;
    private String message;
    private LocalDateTime creationDate;
    private boolean archived;
    private String userUsername; // Solo el username del usuario
    private List<TagSummaryDTO> tags;
}
