package de.stella.agora_web.comment.services;

import de.stella.agora_web.comment.controller.dto.CommentDTO;
import de.stella.agora_web.comment.model.Comment;
import java.util.List;
import lombok.NonNull;

public interface ICommentService {
  List<Comment> getAllComment();

  Comment getCommentById(@NonNull Long id);

  void deleteComment(Long id);

  Comment createComment(CommentDTO commentDTO, Object object);

  Comment updateComment(Long id, CommentDTO commentDTO);

  public Object getCommentsByPostId(Long postId);

  List<Comment> getCommentsByTagId(Long tagId);

  List<Comment> getCommentsByUserId(Long userId);

  List<Comment> getCommentsByTagName(String tagName);
}
