package de.stella.agora_web.comment.kafka.dto;

import org.springframework.beans.factory.annotation.Autowired;

import de.stella.agora_web.comment.model.Comment;
import de.stella.agora_web.comment.repository.CommentRepository;
import de.stella.agora_web.user.model.User;

public class CommentNotificationDTO {

    private Long commentId;
    private String author;
    private String message;
    @Autowired
    private CommentRepository commentRepository;

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

    public String getPostTitle() {
        // Get the post title from the database
        // Check for invalid commentId
        if (this.commentId == null) {
            throw new IllegalArgumentException("commentId is null");
        }

        // Get the comment from the database
        Comment comment = commentRepository.findById(this.commentId).orElse(null);

        // Check if comment doesn't exist
        if (comment == null) {
            throw new IllegalArgumentException("commentId is invalid");
        }

        // Check if post doesn't exist
        if (comment.getPost() == null) {
            throw new IllegalArgumentException("post is null");
        }

        // Get the post title
        String postTitle = comment.getPost().getTitle();

        // Check if post title is null
        if (postTitle == null) {
            throw new IllegalArgumentException("post title is null");
        }

        return postTitle;
    }
}