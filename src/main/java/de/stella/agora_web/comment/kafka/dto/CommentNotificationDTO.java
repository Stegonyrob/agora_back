package de.stella.agora_web.comment.kafka.dto;

import de.stella.agora_web.comment.model.Comment;
import de.stella.agora_web.user.model.User;

/**
 * DTO para transportar datos de notificación de comentario a través de Kafka.
 * Es un POJO puro — no tiene dependencias de Spring ni repositorios.
 */
public class CommentNotificationDTO {

    private Long commentId;
    private String author;
    private String authorEmail;
    private String message;
    private String postTitle;

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    /**
     * Construye un Comment mínimo para pasar al servicio de moderación. Solo
     * contiene los campos necesarios para la moderación de contenido.
     */
    public Comment getComment() {
        if (this.commentId == null) {
            throw new IllegalArgumentException("commentId es null");
        }
        if (this.author == null) {
            throw new IllegalArgumentException("author es null");
        }
        if (this.message == null) {
            throw new IllegalArgumentException("message es null");
        }

        Comment comment = new Comment();
        comment.setId(this.commentId);
        User user = new User();
        user.setId(commentId);
        user.setUsername(this.author);
        comment.setUser(user);
        comment.setMessage(this.message);
        return comment;
    }
}
