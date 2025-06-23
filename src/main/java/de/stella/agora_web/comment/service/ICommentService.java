package de.stella.agora_web.comment.service;

import java.util.List;

import de.stella.agora_web.comment.controller.dto.CommentDTO;
import de.stella.agora_web.comment.model.Comment;
import de.stella.agora_web.user.model.User;

public interface ICommentService {

    Comment getCommentById(Long id);

    Comment createComment(CommentDTO commentDTO, User user);

    Comment updateComment(Long id, CommentDTO commentDTO, User user);

    void deleteComment(Long id, User user);

    List<Comment> getAllComments();

    List<Comment> getCommentsByPostId(Long postId);

    List<Comment> getCommentsByUserId(Long userId);

    List<Comment> getCommentsByTagName(String tagName);

    Comment findById(Long commentId);
}
