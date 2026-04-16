package de.stella.agora_web.comment.exceptions;

public class CommentNotFoundException extends CommentException {

    public CommentNotFoundException(String message) {
        super(message);
    }

    public CommentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}