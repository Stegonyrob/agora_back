package de.stella.agora_web.replies.kafka.dto;

import java.time.LocalDateTime;

import de.stella.agora_web.replies.controller.dto.ReplyDTO;
import de.stella.agora_web.replies.model.Reply;

/**
 * DTO para notificaciones de respuestas via Kafka Se envía cuando se crea una
 * nueva respuesta para notificar al admin por email
 */
public class ReplyNotificationDTO {

    private Long replyId;
    private Long commentId;
    private Long postId;
    private String postTitle;
    private String userName;
    private String replyContent;
    private LocalDateTime createdAt;
    private Reply reply;

    // Constructores
    public ReplyNotificationDTO() {
    }

    public ReplyNotificationDTO(Long replyId, Long commentId, Long postId, String postTitle, String userName,
            String replyContent, LocalDateTime createdAt) {
        this.replyId = replyId;
        this.commentId = commentId;
        this.postId = postId;
        this.postTitle = postTitle;
        this.userName = userName;
        this.replyContent = replyContent;
        this.createdAt = createdAt;
    }

    // Getters y Setters
    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

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

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
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
        return String.format(
                "ReplyNotificationDTO{replyId=%d, commentId=%d, postId=%d, postTitle='%s', userName='%s', content='%s'}",
                replyId, commentId, postId, postTitle, userName, replyContent);
    }

    public void setAuthor(String author) {
        this.userName = author;
    }

    public void setMessage(String message) {
        this.replyContent = message;
    }

    public String getAuthor() {
        return this.userName;
    }

    public String getMessage() {
        return this.replyContent;
    }

    public ReplyDTO getReply() {
        return ReplyDTO.fromEntity(this.reply);
    }
}
