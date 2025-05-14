package de.stella.agora_web.avatar.exception;

public class AvatarNotFoundException extends RuntimeException {
    public AvatarNotFoundException(String message) {
        super(message);
    }
}