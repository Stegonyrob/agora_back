package de.stella.agora_web.comment.controller.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CommentDTO {

    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime creationDate;
    private Long postId;
    private Long userId;

    public CommentDTO() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public CommentDTO(String message, LocalDateTime creationDate, Long postId, Long userId) {
        this.message = message;
        this.creationDate = creationDate;
        this.postId = postId;
        this.userId = userId;
    }
}
