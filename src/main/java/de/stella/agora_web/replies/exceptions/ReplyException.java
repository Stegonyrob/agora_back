package de.stella.agora_web.replies.exceptions;

public class ReplyException extends RuntimeException {

  public ReplyException(String message) {
    super(message);
  }

  public ReplyException(String message, Throwable cause) {
    super(message, cause);
  }
}
