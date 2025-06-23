package de.stella.agora_web.replies.controller.dto;

import java.time.LocalDateTime;
import java.util.List;

import de.stella.agora_web.replies.model.Reply;
import de.stella.agora_web.tags.model.Tag;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReplyDTO {

    private String title;
    private String message;
    private LocalDateTime creationDate;
    private Long commentId; // Relación directa con Comment
    private Long userId;
    private List<String> tags;

    // Métodos utilitarios
    public String[] getTagsArray() {
        return tags != null ? tags.toArray(new String[0]) : new String[0];
    }

    public static ReplyDTO fromEntity(Reply reply) {
        return ReplyDTO.builder()
                .title(reply.getTitle())
                .message(reply.getMessage())
                .creationDate(reply.getCreationDate())
                .commentId(reply.getComment() != null ? reply.getComment().getId() : null)
                .userId(reply.getUser() != null ? reply.getUser().getId() : null)
                .tags(reply.getTags() != null
                        ? reply.getTags().stream().map(Tag::getName).toList()
                        : List.of())
                .build();
    }
}
