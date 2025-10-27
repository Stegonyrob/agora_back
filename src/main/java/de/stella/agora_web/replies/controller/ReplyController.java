package de.stella.agora_web.replies.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import de.stella.agora_web.common.dto.SanctionInfoResponse;
import de.stella.agora_web.replies.controller.dto.ReplyDTO;
import de.stella.agora_web.replies.model.Reply;
import de.stella.agora_web.replies.service.IReplyService;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.model.User.SanctionStatus;

@RestController
@RequestMapping(path = "${api-endpoint}/any")
public class ReplyController {

    private static final Logger auditLogger = LoggerFactory.getLogger("AuditLogger");

    private final IReplyService replyService;

    public ReplyController(IReplyService replyService) {
        this.replyService = replyService;
    }

    @PostMapping("/replies/create")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Object> createReply(
            @RequestBody ReplyDTO replyDTO,
            @AuthenticationPrincipal User user
    ) {
        if (user.getSanctionStatus() == SanctionStatus.EXPELLED) {
            String msg = "Intento bloqueado: usuario %d expulsado intentó crear respuesta".formatted(user.getId());
            auditLogger.warn(msg);
            SanctionInfoResponse sanctionInfo = new SanctionInfoResponse(
                    user.getSanctionType().name(),
                    user.getSanctionExpiration() != null ? user.getSanctionExpiration().toString() : null,
                    "No puedes participar porque has sido expulsado. Contacta a soporte si crees que es un error."
            );
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(sanctionInfo);
        }
        if (user.getSanctionStatus() == SanctionStatus.SUSPENDED) {
            String msg = "Intento bloqueado: usuario %d suspendido intentó crear respuesta".formatted(user.getId());
            auditLogger.warn(msg);
            SanctionInfoResponse sanctionInfo = new SanctionInfoResponse(
                    user.getSanctionType().name(),
                    user.getSanctionExpiration() != null ? user.getSanctionExpiration().toString() : null,
                    "No puedes participar porque estás suspendido hasta la fecha indicada."
            );
            return ResponseEntity.status(HttpStatus.LOCKED).body(sanctionInfo);
        }
        Reply reply = replyService.createReply(replyDTO, user);
        ReplyDTO responseDTO = ReplyDTO.fromEntity(reply);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/replies/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ReplyDTO> show(@PathVariable Long id) {
        Reply reply = replyService.getReplyById(id);
        ReplyDTO dto = ReplyDTO.fromEntity(reply);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/replies/comment/{commentId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<ReplyDTO> getRepliesByCommentId(@PathVariable Long commentId) {
        return replyService.getRepliesByCommentId(commentId)
                .stream()
                .map(ReplyDTO::fromEntity)
                .toList();
    }

    @GetMapping("/replies/user/{userId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<ReplyDTO> getRepliesByUserId(@PathVariable Long userId) {
        return replyService.getRepliesByUserId(userId)
                .stream()
                .map(ReplyDTO::fromEntity)
                .toList();
    }

    @PutMapping("/replies/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Object> update(
            @PathVariable Long id,
            @RequestBody ReplyDTO replyDTO,
            @AuthenticationPrincipal User user
    ) {
        if (user.getSanctionStatus() == User.SanctionStatus.EXPELLED) {
            String msg = "Intento bloqueado: usuario %d expulsado intentó editar respuesta %d".formatted(user.getId(), id);
            auditLogger.warn(msg);
            SanctionInfoResponse sanctionInfo = new SanctionInfoResponse(
                    user.getSanctionType().name(),
                    user.getSanctionExpiration() != null ? user.getSanctionExpiration().toString() : null,
                    "No puedes editar respuestas porque has sido expulsado. Contacta a soporte si crees que es un error."
            );
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(sanctionInfo);
        }
        if (user.getSanctionStatus() == User.SanctionStatus.SUSPENDED) {
            String msg = "Intento bloqueado: usuario %d suspendido intentó editar respuesta %d".formatted(user.getId(), id);
            auditLogger.warn(msg);
            SanctionInfoResponse sanctionInfo = new SanctionInfoResponse(
                    user.getSanctionType().name(),
                    user.getSanctionExpiration() != null ? user.getSanctionExpiration().toString() : null,
                    "No puedes editar respuestas porque estás suspendido hasta la fecha indicada."
            );
            return ResponseEntity.status(HttpStatus.LOCKED).body(sanctionInfo);
        }
        Reply reply = replyService.updateReply(id, replyDTO);
        ReplyDTO dto = ReplyDTO.fromEntity(reply);
        return ResponseEntity.accepted().body(dto);
    }

    @DeleteMapping("/replies/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Object> deleteReply(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        if (user.getSanctionStatus() == User.SanctionStatus.EXPELLED) {
            String msg = "Intento bloqueado: usuario %d expulsado intentó borrar respuesta %d".formatted(user.getId(), id);
            auditLogger.warn(msg);
            SanctionInfoResponse sanctionInfo = new SanctionInfoResponse(
                    user.getSanctionType().name(),
                    user.getSanctionExpiration() != null ? user.getSanctionExpiration().toString() : null,
                    "No puedes borrar respuestas porque has sido expulsado. Contacta a soporte si crees que es un error."
            );
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(sanctionInfo);
        }
        if (user.getSanctionStatus() == User.SanctionStatus.SUSPENDED) {
            String msg = "Intento bloqueado: usuario %d suspendido intentó borrar respuesta %d".formatted(user.getId(), id);
            auditLogger.warn(msg);
            SanctionInfoResponse sanctionInfo = new SanctionInfoResponse(
                    user.getSanctionType().name(),
                    user.getSanctionExpiration() != null ? user.getSanctionExpiration().toString() : null,
                    "No puedes borrar respuestas porque estás suspendido hasta la fecha indicada."
            );
            return ResponseEntity.status(HttpStatus.LOCKED).body(sanctionInfo);
        }
        replyService.deleteReply(id);
        return ResponseEntity.noContent().build();
    }
}
