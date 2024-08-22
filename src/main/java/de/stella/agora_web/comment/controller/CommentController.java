package de.stella.agora_web.comment.controller;

import de.stella.agora_web.comment.controller.dto.CommentDTO;
import de.stella.agora_web.comment.model.Comment;
import de.stella.agora_web.comment.services.ICommentService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import java.util.List;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "${api-endpoint}/")
public class CommentController {

  private final ICommentService CommentService;

  public CommentController(ICommentService CommentService) {
    this.CommentService = CommentService;
  }

  //(Get, endpoint/comments).hasAnyRoles(user,admin)
  //respuesta a un comentario el cual debera esta asignado a un post y a un comentario refactorizar reolies para que sean
  //las respuestas del admin crear entidad de comenta para usuarios

  //censurar controllador unico

  @PostMapping("/comments/create")
  @PreAuthorize("hasRole('USER','ADMIN')")
  public ResponseEntity<Comment> createComment(
    @RequestBody CommentDTO CommentDTO
  ) {
    Comment Comment = CommentService.createComment(CommentDTO, null);
    return ResponseEntity.status(HttpStatus.CREATED).body(Comment);
  }

  @GetMapping("/comments/{id}")
  public ResponseEntity<Comment> show(@NonNull @PathVariable Long id) {
    Comment Comment = CommentService.getCommentById(id);
    return ResponseEntity.status(HttpStatus.OK).body(Comment);
  }

  @SuppressWarnings("unchecked")
  @GetMapping("/comments/post/{postId}")
  public List<Comment> getCommentsByPostId(@PathVariable Long postId) {
    return (List<Comment>) CommentService.getCommentsByPostId(postId);
  }

  @GetMapping("/comments/tags/{tagName}")
  public List<Comment> getCommentsByTagName(String tagName) {
    return (List<Comment>) CommentService.getCommentsByTagName(tagName);
  }

  @GetMapping("/comments/user/{userId}")
  public List<Comment> getCommentsByUserId(@PathVariable Long userId) {
    return (List<Comment>) CommentService.getCommentsByUserId(userId);
  }

  @DeleteMapping("/comments/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    CommentService.deleteComment(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/comments/{id}")
  public ResponseEntity<Comment> update(
    @PathVariable Long id,
    @RequestBody CommentDTO CommentDTO
  ) {
    Comment Comment = CommentService.updateComment(id, CommentDTO);
    return ResponseEntity.accepted().body(Comment);
  }
}
