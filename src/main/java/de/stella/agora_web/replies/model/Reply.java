package de.stella.agora_web.replies.model;

import java.time.LocalDateTime;

import de.stella.agora_web.comment.model.Comment;
import de.stella.agora_web.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank
    @Size(min = 1, max = 1000)
    @Column(name = "message ", length = 1000)
    private String message;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "archived")
    private Boolean archived;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    @com.fasterxml.jackson.annotation.JsonBackReference
    private Comment comment;

    public Reply() {
    }

    public Reply(Long id, String message, LocalDateTime creationDate, User user) {
        this.id = id;
        this.archived = false; // Por defecto, no está archivado
        this.message = message;
        this.creationDate = creationDate;
        this.user = user;

    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
