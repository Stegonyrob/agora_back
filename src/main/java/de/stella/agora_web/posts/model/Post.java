package de.stella.agora_web.posts.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "posts")
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "title")
  private String title;

  @Column(name = "message", length = 2500)
  private String message;

  @Column(name = "creation_date")
  private LocalDateTime creationDate;

  @Column(name = "archived")
  private Boolean archived;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  @JsonBackReference
  private User user;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> comments;

  @ManyToMany
  @JoinTable(
    name = "post_tag",
    joinColumns = @JoinColumn(name = "post_id"),
    inverseJoinColumns = @JoinColumn(name = "tag_id")
  )
  private List<Tag> tags;

  public Post() {}

  public Post(
    Long id,
    String title,
    String message,
    LocalDateTime creationDate,
    User user
  ) {
    this.id = id;
    this.title = title;
    this.message = message;
    this.creationDate = creationDate;
    this.user = user;
  }

  public void setUser(User user2) {
    this.user = user2;
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
  // No hay método para obtener respuestas, ya que un post no tiene respuestas directas
  // Las respuestas están asociadas a los comentarios, no a los posts
}
