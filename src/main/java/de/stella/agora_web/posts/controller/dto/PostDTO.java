package de.stella.agora_web.posts.controller.dto;

import java.time.LocalDateTime;
import java.util.List;

import de.stella.agora_web.replies.controller.dto.ReplyDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PostDTO {

  private String title;
  private String message;
  private LocalDateTime creationDate;
  private Long userId;

  @SuppressWarnings("rawtypes")
  private List<ReplyDTO> replies;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  private List<String> tags;

  public String[] getTags() {
    if (tags == null) {
      return new String[0];
    }
    return tags.toArray(String[]::new);
  }
}
