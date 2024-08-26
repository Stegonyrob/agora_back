package de.stella.agora_web.comment.model;

import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.replies.model.Reply;
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
@Table(name = "comments")
public class Comment {

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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  private Post post;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToMany
  @JoinTable(
    name = "comment_tags",
    joinColumns = @JoinColumn(name = "comment_id"),
    inverseJoinColumns = @JoinColumn(name = "tag_id")
  )
  private List<Tag> tags;

  public Comment() {}

  public Comment(
    Long id,
    String title,
    String message,
    LocalDateTime creationDate,
    User author,
    Post post
  ) {
    this.id = id;
    this.title = title;
    this.message = message;
    this.creationDate = creationDate;
    this.user = author;
    this.post = post;
  }

  public void setAuthor(User author) {
    this.user = author;
  }

  public void setPost(Post post) {
    this.post = post;
  }

  @OneToMany(
    mappedBy = "comment",
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  private List<Reply> replies;

  public List<Reply> getReplies() {
    return replies;
  }
}
