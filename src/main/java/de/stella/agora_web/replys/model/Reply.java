package de.stella.agora_web.replys.model;

import java.time.LocalDateTime;
import java.util.Set;

import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.profiles.model.Profile;
import de.stella.agora_web.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "Reply")
@Table(name = "replys")
@Getter
@Setter
public class Reply {
    
    // Identificador único de la respuesta
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT")
    private Long id;
    
    // Título de la respuesta
    @Column(name = "title", columnDefinition = "VARCHAR(255)")
    private String title;

    // Mensaje de la respuesta
    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    // Fecha y hora de creación de la respuesta
    @Column(name = "creation_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime creationDate;

    // Indica si la respuesta es favorita
    @Column(name = "favorite", columnDefinition = "BOOLEAN")
    private boolean favorite;
    
    // Usuario que creó la respuesta
    @ManyToOne
    @JoinColumn(name = "user_id", columnDefinition = "BIGINT")
    private User author;

    // Publicación asociada a la respuesta
    @ManyToOne
    @JoinColumn(name = "post_id", columnDefinition = "BIGINT")
    private Post post;
 @ManyToMany(mappedBy = "favorites")
    private Set<Profile> favoritedBy;
    // Constructor vacío para Hibernate
    public Reply() {
    }

    // Constructor con parámetros para crear una respuesta
    public Reply(String title, String message, User author, Post post, boolean favorite) {
        this.favorite = favorite;
        this.title = title;
        this.message = message;
        this.author = author;
        this.post = post;
    }
}