package de.stella.agora_web.violations.controller.dto;

import java.time.LocalDateTime;

import de.stella.agora_web.violations.model.ViolationType;

public class UserViolationDTO {

    private Long id;
    private Long userId;
    private ViolationType violationType;
    private Long commentId;
    private LocalDateTime timestamp;

    public UserViolationDTO() {
    }

    public UserViolationDTO(Long id, Long userId, ViolationType violationType, Long commentId, LocalDateTime timestamp) {
        this.id = id;
        this.userId = userId;
        this.violationType = violationType;
        this.commentId = commentId;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ViolationType getViolationType() {
        return violationType;
    }

    public void setViolationType(ViolationType violationType) {
        this.violationType = violationType;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
