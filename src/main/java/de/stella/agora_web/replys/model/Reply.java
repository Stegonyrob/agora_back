package de.stella.agora_web.replys.model;

import java.time.LocalDateTime;

import de.stella.agora_web.posts.model.Post;
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

@Entity(name = "Reply")
@Table(name = "replys")
@Getter
@Setter
public class Reply {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT")
    private Long id;
    
    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "creation_date", updatable = false)
    private LocalDateTime creationDate;

    @Column(name = "favorite")
    private boolean favorite;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public Reply() {
    }

    public Reply(String title, String message, User author, Post post, boolean favorite) {
        this.favorite = favorite;
        this.title = title;
        this.message = message;
        this.author = author;
        this.post = post;
    }

    // getters and setters
}