package de.stella.agora_web.replies.controller.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import de.stella.agora_web.replies.model.Reply; // Ajusta el import según tu estructura
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReplyDTO {

    private Long id;
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime creationDate;
    private Long commentId;
    private Long userId;

    public static ReplyDTO fromEntity(Reply reply) {
        ReplyDTO dto = new ReplyDTO();
        dto.setId(reply.getId());
        dto.setMessage(reply.getMessage());
        dto.setCreationDate(reply.getCreationDate());
        dto.setCommentId(reply.getComment().getId());
        dto.setUserId(reply.getUser().getId());
        return dto;
    }
}
