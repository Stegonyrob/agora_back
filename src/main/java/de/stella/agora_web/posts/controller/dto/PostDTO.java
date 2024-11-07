package de.stella.agora_web.posts.controller.dto;

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

  private Long userId;
  private String title;
  private String message;
  private String location;
  private int loves;
  private List<Comment> comments;
  private boolean isArchived;
  private List<String> tags;
  private List<String> images;
  private boolean isPublished;
  private String altImage;
  private String sourceImage;
  private String altAvatar;
  private String sourceAvatar;
  private String username;
  private String role;
  private String urlAvatar;

  public PostDTO(Post post) {
    this.id = post.getId();
    this.title = post.getTitle();
    this.message = post.getMessage();
    this.location = post.getLocation();
    this.isArchived = post.getArchived();
    this.userId = post.getUser().getId();
    this.comments = post.getComments();
    this.tags = post.getTags().stream().map(Tag::getName).collect(Collectors.toList());
  }

  @SuppressWarnings("rawtypes")
  private List<ReplyDTO> replies;

  public PostDTO(Boolean archived) {
    this.isArchived = archived;
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
    return Boolean.TRUE.equals(isArchived);
  }

  public Boolean getArchived() {
    return isArchived();
  }
}
