package de.stella.agora_web.comment.kafka.dto;

import de.stella.agora_web.comment.model.Comment;
import de.stella.agora_web.user.model.User;

public class CommentNotificationDTO {

    private Long commentId;
    private String author;
    private String message;

    // ... otros campos
    public void setCommentId(Long id) {
        this.commentId = id;
    }

    public void setAuthor(String username) {
        this.author = username;
    }

    public void setMessage(String message2) {
        this.message = message2;
    }

    public String getMessage() {
        return this.message;
    }

    public String getAuthor() {
        return this.author;
    }

    public Comment getComment() {
        Comment comment = new Comment();
        comment.setId(this.commentId);
        comment.setUser(new User(commentId, this.author, author, author, null, null, null));
        comment.setMessage(this.message);
        return comment;
    }
}