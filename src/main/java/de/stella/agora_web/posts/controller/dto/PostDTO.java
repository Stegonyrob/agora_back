package de.stella.agora_web.posts.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import de.stella.agora_web.comment.model.Comment;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.replies.controller.dto.ReplyDTO;
import de.stella.agora_web.roles.model.Role;
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
  private String userName;
  private String role;
  private String urlAvatar;

  public PostDTO(Post post) {
    this.id = post.getId();
    this.title = post.getTitle();
    this.message = post.getMessage();
    this.location = post.getLocation();
    this.isArchived = post.getArchived();
    this.loves = post.getLoves();
    this.userId = post.getUser() != null ? post.getUser().getId() : null; // Mapea el userId desde el objeto User
    this.comments = post.getComments();
    this.tags = post.getTags().stream().map(Tag::getName).collect(Collectors.toList());
    this.userName = post.getUser() != null ? post.getUser().getUsername() : null;
    this.role = post.getUser() != null
        ? (String) post.getUser().getRoles().stream().findFirst().map(Role::getName).orElse(null)
        : null;
  }

  @SuppressWarnings("rawtypes")
  private List<ReplyDTO> replies;

  public PostDTO(Boolean archived) {
    this.isArchived = archived;
  }

  public long getUserId() {
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
