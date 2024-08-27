package de.stella.agora_web.censured.controller;

import de.stella.agora_web.censured.controller.dto.CensuredCommentDTO;
import de.stella.agora_web.censured.model.CensuredComment;
import de.stella.agora_web.censured.repository.CensuredCommentRepository;
import de.stella.agora_web.comment.model.Comment;
import de.stella.agora_web.comment.services.ICommentService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/censured-comments")
public class CensuredCommentController {

  @Autowired
  private CensuredCommentRepository censuredCommentRepository;

  @Autowired
  private ICommentService commentService;

  @GetMapping("/{id}")
  public ResponseEntity<Boolean> isCommentCensured(@PathVariable Long id) {
    Optional<CensuredComment> censuredComment = censuredCommentRepository.findById(
      id
    );
    return censuredComment.isPresent()
      ? ResponseEntity.ok(true)
      : ResponseEntity.ok(false);
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<Integer> countUserInfractions(
    @PathVariable Long userId
  ) {
    List<CensuredComment> censuredComments = censuredCommentRepository.findByUserId(
      userId
    );
    return ResponseEntity.ok(censuredComments.size());
  }

  @PostMapping("/censure")
  public ResponseEntity<Void> censureComment(
    @RequestBody CensuredCommentDTO censuredCommentDTO
  ) {
    Comment comment = commentService.findById(
      censuredCommentDTO.getCommentId()
    );
    if (comment != null) {
      CensuredComment censuredComment = new CensuredComment(
        comment,
        censuredCommentDTO.getUserId()
      );
      censuredCommentRepository.save(censuredComment);
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
