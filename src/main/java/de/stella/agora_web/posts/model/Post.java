package de.stella.agora_web.posts.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;

import de.stella.agora_web.comment.model.Comment;
import de.stella.agora_web.tags.model.Tag;
import de.stella.agora_web.user.model.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "posts")
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(min = 1, max = 100)
  @Column(name = "title")
  private String title;

  @NotBlank
  @Size(min = 1, max = 2500)
  @Column(name = "message", length = 2500)
  private String message;

  @Column(name = "creation_date")
  private LocalDateTime creationDate = LocalDateTime.now();

  @Column(name = "archived")
  private boolean archived;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  @JsonBackReference
  private User user;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> comments;

  @ManyToMany
  @JoinTable(name = "post_tag", joinColumns = @JoinColumn(name = "post_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
  private List<Tag> tags;

  public Post() {
  }

  public Post(Long id, String title, String message, Long userId, boolean archived, String user) {
    this.id = id;
    this.title = title;
    this.message = message;
    this.user = new User();
    this.user.setId(userId);
    this.archived = archived;
  }

  public void setUserId(Long userId) {
    if (this.user == null) {
      this.user = new User();
    }
    this.user.setId(userId);
  }

  public void setUser(User user) {
    this.user = user;
  }

  public User getUser() {
    return user;
  }

  public boolean getArchived() {
    return archived;
  }

  public void setArchived(boolean archived) {
    this.archived = archived;
  }

  public List<Comment> getComments() {
    return comments;
  }

  public void setComments(List<Comment> comments) {
    this.comments = comments;
  }

  public List<Tag> getTags() {
    return tags;
  }

  public void setTags(List<Tag> tags) {
    this.tags = tags;
  }

  public String getLocation() {
    return "Agora Web";
  }

  @Override
  public String toString() {
    return "Post{" + "id=" + id + ", title='" + title + '\'' + ", message='" + message + '\'' + ", creationDate="
        + creationDate + ", archived=" + archived + ", user=" + (user != null ? user.getId() : "null") + ", comments="
        + comments + ", tags=" + tags + '}';
  }

  public int getLoves() {
    return 0;
  }

  public int getId() {
    return id.intValue();
  }

  public String getTitle() {
    return title;
  }

  public String getMessage() {
    return message;
  }
}
