package de.stella.agora_web.replies.model;

import de.stella.agora_web.comment.model.Comment;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.tags.model.Tag;
import de.stella.agora_web.user.model.User;
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
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "replies")
public class Reply {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "title")
  private String title;

  @Column(name = "message ", length = 1000)
  private String message;

  @Column(name = "creation_date")
  private LocalDateTime creationDate;

  @Column(name = "archived")
  private Boolean archived;

  @ManyToMany
  @JoinTable(
    name = "reply_tags",
    joinColumns = @JoinColumn(name = "reply_id"),
    inverseJoinColumns = @JoinColumn(name = "tag_id")
  )
  private List<Tag> tags;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user; // Cambiado de author a user

  @ManyToOne
  @JoinColumn(name = "post_id")
  private Post post;

  @ManyToOne
  @JoinColumn(name = "comment_id")
  private Comment comment;

  public Reply() {}

  public Reply(
    Long id,
    String title,
    String message,
    LocalDateTime creationDate,
    User user,
    Post post
  ) {
    this.id = id;
    this.title = title;
    this.message = message;
    this.creationDate = creationDate;
    this.user = user;
    this.post = post;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public void setTags(List<Tag> tags) {
    this.tags = tags;
  }

  public void setPost(Post post) {
    this.post = post;
  }

  public void setComment(Comment comment) {
    this.comment = comment;
  }
}
