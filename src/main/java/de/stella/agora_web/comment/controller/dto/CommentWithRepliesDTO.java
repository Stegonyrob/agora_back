package de.stella.agora_web.comment.controller.dto;

import java.time.LocalDateTime;
import java.util.List;

import de.stella.agora_web.replies.controller.dto.ReplyDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentWithRepliesDTO {

    private Long id;
    private String message;
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
