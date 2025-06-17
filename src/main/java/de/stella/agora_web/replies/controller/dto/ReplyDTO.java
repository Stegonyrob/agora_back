package de.stella.agora_web.replies.controller.dto;

import java.time.LocalDateTime;
import java.util.List;

import de.stella.agora_web.replies.model.Reply;
import de.stella.agora_web.user.controller.dto.UserDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReplyDTO<PostDTO> {

    private String title;
    private String message;
    private LocalDateTime creationDate;
    private List<PostDTO> post; //un unico dto id post
    private UserDTO user; //user id revisar que llega de front

    private List<String> tags;

    public String[] getTags() {
        return tags.toArray(new String[0]);
    }

    public Long getPostId() {
        if (post == null || post.isEmpty()) {
            return null;
        }
        PostDTO firstPost = post.get(0);
        return ((UserDTO) firstPost).getId();
    }

    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    public static ReplyDTO fromEntity(Reply reply) {
        return ReplyDTO.builder()
                .title(reply.getTitle())
                .message(reply.getMessage())
                .creationDate(reply.getCreationDate())
                // .post(...) // set this if you have a way to map posts
                // .user(...) // set this if you have a way to map user
                // .tags(...) // set this if you have a way to map tags
                .build();
    }
}
