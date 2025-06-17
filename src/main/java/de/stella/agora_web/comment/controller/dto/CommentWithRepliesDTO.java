package de.stella.agora_web.comment.controller.dto;

import java.util.List;

import de.stella.agora_web.replies.controller.dto.ReplyDTO;

public class CommentWithRepliesDTO {

    private Long id;
    private String message;
    private List<ReplyDTO> replies;

    // Constructor
    public CommentWithRepliesDTO(Long id, String message, List<ReplyDTO> replies) {
        this.id = id;
        this.message = message;
        this.replies = replies;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ReplyDTO> getReplies() {
        return replies;
    }

    public void setReplies(List<ReplyDTO> replies) {
        this.replies = replies;
    }
}
