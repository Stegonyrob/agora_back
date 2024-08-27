package de.stella.agora_web.censured.controller.dto;

public class CensuredCommentDTO {

  private Long userId;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  private Long commentId;

  public Long getCommentId() {
    return commentId;
  }

  public void setCommentId(Long commentId) {
    this.commentId = commentId;
  }
}
