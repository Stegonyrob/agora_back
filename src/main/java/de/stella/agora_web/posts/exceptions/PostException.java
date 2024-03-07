package de.stella.agora_web.posts.exceptions;

public class PostException extends RuntimeException {
    public PostException(String message) {
        super(message);
    }

    public PostException(String message, Throwable cause) {
        super(message, cause);
    }
}