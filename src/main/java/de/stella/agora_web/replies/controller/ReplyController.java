package de.stella.agora_web.replies.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<ReplyDTO> createReply(
            @RequestBody ReplyDTO replyDTO,
            @AuthenticationPrincipal User user
    ) {
        Reply reply = replyService.createReply(replyDTO, user);
        ReplyDTO responseDTO = ReplyDTO.fromEntity(reply);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/replies/{id}")
    public ResponseEntity<ReplyDTO> show(@PathVariable Long id) {
        Reply reply = replyService.getReplyById(id);
        ReplyDTO dto = ReplyDTO.fromEntity(reply);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/replies/comment/{commentId}")
    public List<ReplyDTO> getRepliesByCommentId(@PathVariable Long commentId) {
        return replyService.getRepliesByCommentId(commentId)
                .stream()
                .map(ReplyDTO::fromEntity)
                .toList();
    }

    @GetMapping("/replies/tags/{tagName}")
    public List<ReplyDTO> getRepliesByTagName(@PathVariable String tagName) {
        return replyService.getRepliesByTagName(tagName)
                .stream()
                .map(ReplyDTO::fromEntity)
                .toList();
    }

    @GetMapping("/replies/user/{userId}")
    public List<ReplyDTO> getRepliesByUserId(@PathVariable Long userId) {
        return replyService.getRepliesByUserId(userId)
                .stream()
                .map(ReplyDTO::fromEntity)
                .toList();
    }

    @PutMapping("/replies/{id}")
    public ResponseEntity<ReplyDTO> update(
            @PathVariable Long id,
            @RequestBody ReplyDTO replyDTO
    ) {
        Reply reply = replyService.updateReply(id, replyDTO);
        ReplyDTO dto = ReplyDTO.fromEntity(reply);
        return ResponseEntity.accepted().body(dto);
    }
}
