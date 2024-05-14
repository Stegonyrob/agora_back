package de.stella.agora_web.replys.model;

import java.time.LocalDateTime;

import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.user.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



    @Setter
    @Getter
    @Entity(name = "Reply")
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Table(name = "replys")
    public class Reply {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String title;
        private String message;
        private LocalDateTime creationDate = LocalDateTime.now();

        @ManyToOne
        @JoinColumn(name = "user_id")
        private User author;

        @ManyToOne
        @JoinColumn(name = "post_id")
        public Post  posts;
    }

