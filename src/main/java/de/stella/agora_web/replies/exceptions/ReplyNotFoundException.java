package de.stella.agora_web.replies.exceptions;

public class ReplyNotFoundException extends ReplyException {

  public ReplyNotFoundException(String message) {
    super(message);
  }

  public ReplyNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
