package de.stella.agora_web.censured.model;

import de.stella.agora_web.comment.model.Comment;
import de.stella.agora_web.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "censured_comments")
public class CensuredComment {

  public CensuredComment(Long long1, Long userId) {
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "comment_id")
  private Comment comment;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "reason")
  private String reason;

  @Column(name = "notification_sent")
  private Boolean notificationSent;

  public CensuredComment() {
  }

  public CensuredComment(Long commentId, User user, String reason) {
    this.id = commentId;
    this.user = user;
    this.reason = reason;
  }
}
