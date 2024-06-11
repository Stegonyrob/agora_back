package de.stella.agora_web.posts.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.stella.agora_web.replys.model.Reply;
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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "Post")
@RequiredArgsConstructor 
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("title")
    @Column(name = "title", columnDefinition = "VARCHAR(255)")
    private String title;

    @JsonProperty("message")
    @Column(name = "message", columnDefinition = "TEXT")
    private String message;
@Column(name = "creation_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime creationDate = LocalDateTime.now();

    public Post(Long id, String title, String message, LocalDateTime creationDate, Reply reply_id, User user) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.creationDate = creationDate;
        this.reply_id = reply_id;
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "reply_id")
    private Reply reply_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    public void setUser(User user2) {
        this.user = user2;
    }
}
