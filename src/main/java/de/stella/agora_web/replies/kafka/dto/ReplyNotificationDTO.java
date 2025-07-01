package de.stella.agora_web.replies.kafka.dto;

import org.springframework.beans.factory.annotation.Autowired;

import de.stella.agora_web.comment.model.Comment;
import de.stella.agora_web.comment.repository.CommentRepository;
import de.stella.agora_web.replies.model.Reply;
import de.stella.agora_web.user.model.User;

/**
 * DTO para notificaciones de replies mediante Kafka
 */
public class ReplyNotificationDTO {

    private Long replyId;
    private Long commentId;
    private String author;
    private String message;

    @Autowired
    private CommentRepository commentRepository;

    public ReplyNotificationDTO() {
    }

    public ReplyNotificationDTO(Long replyId, Long commentId, String author, String message) {
        this.replyId = replyId;
        this.commentId = commentId;
        this.author = author;
        this.message = message;
    }

    // Getters and Setters
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Obtiene el título del post asociado al comentario de esta respuesta
     */
    public String getPostTitle() {
        if (this.commentId == null) {
            throw new IllegalArgumentException("commentId is null");
        }

        Comment comment = commentRepository.findById(this.commentId).orElse(null);
        if (comment == null) {
            throw new IllegalArgumentException("Comment not found with id: " + this.commentId);
        }

        if (comment.getPost() == null) {
            throw new IllegalArgumentException("Post is null for comment: " + this.commentId);
        }

        String postTitle = comment.getPost().getTitle();
        if (postTitle == null) {
            throw new IllegalArgumentException("Post title is null");
        }

        return postTitle;
    }

    /**
     * Construye un objeto Reply a partir de los datos del DTO
     */
    public Reply getReply() {
        if (this.replyId == null) {
            throw new IllegalArgumentException("replyId is null");
        }

        Reply reply = new Reply();
        reply.setId(this.replyId);

        if (this.author != null) {
            User user = new User();
            user.setUsername(this.author);
            reply.setUser(user);
        }

        if (this.message != null) {
            reply.setMessage(this.message);
        }

        if (this.commentId != null) {
            Comment comment = new Comment();
            comment.setId(this.commentId);
            reply.setComment(comment);
        }

        return reply;
    }
}
