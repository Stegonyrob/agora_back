package de.stella.agora_web.posts.controller.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import de.stella.agora_web.comment.model.Comment;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.replies.controller.dto.ReplyDTO;
import de.stella.agora_web.tags.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PostDTO {

  private Long id;
  private String title;
  private String message;
  private LocalDateTime creationDate;
  private Boolean archived;
  private Long userId;
  private List<Comment> comments;
  private List<String> tags;

  public PostDTO(Post post) {
    this.id = post.getId();
    this.title = post.getTitle();
    this.message = post.getMessage();
    this.creationDate = post.getCreationDate();
    this.archived = post.getArchived();
    this.userId = post.getUser().getId();
    this.comments = post.getComments();
    this.tags = post.getTags().stream().map(Tag::getName).collect(Collectors.toList());
  }

  @SuppressWarnings("rawtypes")
  private List<ReplyDTO> replies;

  public PostDTO(Boolean archived) {
    this.archived = archived;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String[] getTags() {
    if (tags == null) {
      return new String[0];
    }
    return tags.toArray(String[]::new);
  }

  public boolean isArchived() {
    return creationDate != null && creationDate.isBefore(LocalDateTime.now());
  }

  public Boolean getArchived() {
    return isArchived();
  }
}
