package de.stella.agora_web.comment.exceptions;

public class CommentException extends RuntimeException {

    public CommentException(String message) {
        super(message);
    }

    public CommentException(String message, Throwable cause) {
        super(message, cause);
    }
}