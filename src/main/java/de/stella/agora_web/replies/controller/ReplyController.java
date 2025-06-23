package de.stella.agora_web.replies.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.replies.controller.dto.ReplyDTO;
import de.stella.agora_web.replies.model.Reply;
import de.stella.agora_web.replies.service.IReplyService;
import de.stella.agora_web.user.model.User;

@RestController
@RequestMapping(path = "${api-endpoint}/all")
public class ReplyController {

    private final IReplyService replyService;

    public ReplyController(IReplyService replyService) {
        this.replyService = replyService;
    }

    @PostMapping("/replies/create")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Reply> createReply(
            @RequestBody ReplyDTO replyDTO,
            @AuthenticationPrincipal User user // Spring Security inyecta el usuario autenticado
    ) {
        Reply reply = replyService.createReply(replyDTO, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(reply);
    }

    @GetMapping("/replies/{id}")
    public ResponseEntity<Reply> show(@PathVariable Long id) {
        Reply reply = replyService.getReplyById(id);
        return ResponseEntity.ok(reply);
    }

    @GetMapping("/replies/comment/{commentId}")
    public List<Reply> getRepliesByCommentId(@PathVariable Long commentId) {
        return replyService.getRepliesByCommentId(commentId);
    }

    @GetMapping("/replies/tags/{tagName}")
    public List<Reply> getRepliesByTagName(@PathVariable String tagName) {
        return replyService.getRepliesByTagName(tagName);
    }

    @GetMapping("/replies/user/{userId}")
    public List<Reply> getRepliesByUserId(@PathVariable Long userId) {
        return replyService.getRepliesByUserId(userId);
    }

    @DeleteMapping("/replies/{id}")
    public ResponseEntity<Void> deleteReply(@PathVariable Long id) {
        replyService.deleteReply(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/replies/{id}")
    public ResponseEntity<Reply> update(
            @PathVariable Long id,
            @RequestBody ReplyDTO replyDTO
    ) {
        Reply reply = replyService.updateReply(id, replyDTO);
        return ResponseEntity.accepted().body(reply);
    }
}
