package de.stella.agora_web.posts.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import de.stella.agora_web.replys.model.Reply;
import de.stella.agora_web.tags.module.Tag;
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
@Table(name = "posts")
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "title")
  private String title;

  @Column(name = "message")
  private String message;

  @Column(name = "creation_date")
  private LocalDateTime creationDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  @JsonBackReference
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reply_id")
  private Reply reply;

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
    User user,
    Reply reply
  ) {
    this.id = id;
    this.title = title;
    this.message = message;
    this.creationDate = creationDate;
    this.user = user;
    this.reply = reply;
  }

  public void setUser(User user2) {
    this.user = user2;
  }
}
