package de.stella.agora_web.posts.services;

import java.util.List;

import de.stella.agora_web.comment.controller.dto.CommentDTO;
import de.stella.agora_web.comment.model.Comment;
import de.stella.agora_web.posts.controller.dto.PostDTO;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.replies.controller.dto.ReplyDTO;
import de.stella.agora_web.replies.model.Reply;
import de.stella.agora_web.tags.model.Tag;

public interface IPostService {
  List<Post> getAllPosts();

  Post createPost(PostDTO postDTO, Long userId);

  Post getById(Long postId);

  Post updatePost(PostDTO postDTO, Long postId);

  Post save(PostDTO postDTO);

  List<String> extractHashtags(String message);

  Tag getTagByName(String tagName);

  Tag createTag(String tagName);

  List<Tag> getTagsByPostId(Long postId);

  List<Post> getPostsByTagName(String tagName);

  List<Post> getPostsByUserId(Long userId);

  Post update(PostDTO postDTO, Long id);

  Post patch(PostDTO postDTO, Long id);

  void archivedPost(Long postId);

  void unarchivedPost(Long postId);

  List<Comment> getCommentsByPostId(Long postId);

  List<Reply> getRepliesByCommentId(Long commentId);

  void createComment(Long postId, CommentDTO commentDTO);

  void createReply(Long commentId, @SuppressWarnings("rawtypes") ReplyDTO replyDTO);

  void deleteComment(Long commentId);

  void deleteReply(Long replyId);

  public void deletedPost(Long id);
}
