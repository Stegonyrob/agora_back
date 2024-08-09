package de.stella.agora_web.replies.controller.dto;

import de.stella.agora_web.user.controller.dto.UserDTO;
import java.time.LocalDateTime;
import java.util.List;
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
}
