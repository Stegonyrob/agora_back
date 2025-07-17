package de.stella.agora_web.comment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.comment.controller.dto.CommentDTO;
import de.stella.agora_web.comment.controller.dto.CommentWithRepliesDTO;
import de.stella.agora_web.comment.model.Comment;
import de.stella.agora_web.comment.service.ICommentService;
import de.stella.agora_web.replies.controller.dto.ReplyDTO;
import de.stella.agora_web.replies.model.Reply;
import de.stella.agora_web.replies.service.IReplyService;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.repository.UserRepository;

@RestController
@RequestMapping(path = "${api-endpoint}/")
public class CommentController {

    @Autowired
    private ICommentService commentService;

    @Autowired
    private IReplyService replyService;

    @SuppressWarnings("unused")
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/comments/create")
    public ResponseEntity<Comment> createComment(
            @RequestBody CommentDTO commentDTO,
            @AuthenticationPrincipal de.stella.agora_web.security.SecurityUser principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = principal.getUser(); // Accede directamente a tu entidad User

        Comment comment = commentService.createComment(commentDTO, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<Comment> update(
            @PathVariable Long id,
            @RequestBody CommentDTO commentDTO,
            @AuthenticationPrincipal de.stella.agora_web.security.SecurityUser principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = principal.getUser();
        Comment comment = commentService.getCommentById(id);
        // Solo el dueño o admin puede modificar
        if (!comment.getUser().getId().equals(user.getId()) && !principal.getRoles().contains("ROLE_ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Comment updated = commentService.updateComment(id, commentDTO, user);
        return ResponseEntity.accepted().body(updated);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal de.stella.agora_web.security.SecurityUser principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = principal.getUser();
        Comment comment = commentService.getCommentById(id);
        // Solo el dueño o admin puede borrar
        if (!comment.getUser().getId().equals(user.getId()) && !principal.getRoles().contains("ROLE_ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        commentService.deleteComment(id, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/comments/post/{postId}/with-replies")
    public ResponseEntity<Page<CommentWithRepliesDTO>> getCommentsWithReplies(
            @PathVariable Long postId,
            Pageable pageable) {
        Page<Comment> comments = commentService.getCommentsByPostId(postId, pageable);
        Page<CommentWithRepliesDTO> dtos = comments.map(comment -> {
            List<Reply> replies = replyService.getRepliesByCommentId(comment.getId());
            List<ReplyDTO> replyDTOs = replies.stream()
                    .map(ReplyDTO::fromEntity)
                    .toList();
            return new CommentWithRepliesDTO(
                    comment.getId(),
                    comment.getMessage(),
                    comment.getCreationDate(),
                    comment.getUser().getId(),
                    replyDTOs
            );
        });
        return ResponseEntity.ok(dtos);
    }

}
