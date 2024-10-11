package de.stella.agora_web.comment.controller.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CommentDTO {

  private String title;
  private String message;
  private LocalDateTime creationDate;
  private Long postId;
  private Long userId;
  private List<String> tags;

  public String[] getTags() {
    return tags.toArray(String[]::new);
  }
}
