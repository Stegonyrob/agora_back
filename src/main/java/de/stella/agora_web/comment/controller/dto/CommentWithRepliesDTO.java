package de.stella.agora_web.comment.controller.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import de.stella.agora_web.replies.controller.dto.ReplyDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentWithRepliesDTO {

    private Long id;
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime creationDate;
    private Long userId;
    private List<ReplyDTO> replies;

    public CommentWithRepliesDTO(Long id, String message, LocalDateTime creationDate, Long userId, List<ReplyDTO> replies) {
        this.id = id;
        this.message = message;
        this.creationDate = creationDate;
        this.userId = userId;
        this.replies = replies;
    }
}
