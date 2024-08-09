package de.stella.agora_web.replies.controller;

import de.stella.agora_web.replies.controller.dto.ReplyDTO;
import de.stella.agora_web.replies.model.Reply;
import de.stella.agora_web.replies.services.IReplyService;
import io.micrometer.common.lang.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//separar dos controlladores de replies uno admin y otro user
@RestController
@RequestMapping(path = "${api-endpoint}/all")
public class ReplyController {

  private final IReplyService replyService;

  public ReplyController(IReplyService replyService) {
    this.replyService = replyService;
  }

  //(Get, endpoint/replies).hasAnyRoles(user,admin)
  //respuesta a un comentario el cual debera esta asignado a un post y a un comentario refactorizar reolies para que sean
  //las respuestas del admin crear entidad de comenta para usuarios

  //censurar controllador unico

  @PostMapping("/replies/create")
  @PreAuthorize("hasRole('USER','ADMIN')")
  public ResponseEntity<Reply> createReply(
    @SuppressWarnings("rawtypes") @RequestBody ReplyDTO replyDTO
  ) {
    Reply reply = replyService.createReply(replyDTO, null);
    return ResponseEntity.status(HttpStatus.CREATED).body(reply);
  }

  @GetMapping("/replies/{id}")
  public ResponseEntity<Reply> show(@NonNull @PathVariable Long id) {
    Reply reply = replyService.getReplyById(id);
    return ResponseEntity.status(HttpStatus.OK).body(reply);
  }

  @DeleteMapping("/replies/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    replyService.deleteReply(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/replies/{id}")
  public ResponseEntity<Reply> update(
    @PathVariable Long id,
    @SuppressWarnings("rawtypes") @RequestBody ReplyDTO replyDTO
  ) {
    Reply reply = replyService.updateReply(id, replyDTO);
    return ResponseEntity.accepted().body(reply);
  }
}
