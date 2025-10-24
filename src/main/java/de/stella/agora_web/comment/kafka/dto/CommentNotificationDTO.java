package de.stella.agora_web.comment.kafka.dto;

import java.time.LocalDateTime;

/**
 * DTO para notificaciones de comentarios via Kafka Se envía cuando se crea un
 * nuevo comentario para notificar al admin por email
 */
public class CommentNotificationDTO {

    private Long commentId;
    private Long postId;
    private String postTitle;
    private String userName;
    private String commentContent;
    private LocalDateTime createdAt;

    // Constructores
    public CommentNotificationDTO() {
    }

    public CommentNotificationDTO(Long commentId, Long postId, String postTitle,
            String userName, String commentContent, LocalDateTime createdAt) {
        this.commentId = commentId;
        this.postId = postId;
        this.postTitle = postTitle;
        this.userName = userName;
        this.commentContent = commentContent;
        this.createdAt = createdAt;
    }

    // Getters y Setters
    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Para debugging y logging
    @Override
    public String toString() {
        return String.format("CommentNotificationDTO{commentId=%d, postId=%d, postTitle='%s', userName='%s', content='%s'}",
                commentId, postId, postTitle, userName, commentContent);
    }

    public String getMessage() {
        return commentContent;
    }

    public String getAuthor() {
        return userName;
    }

    public String getComment() {
        return commentContent;
    }
}
