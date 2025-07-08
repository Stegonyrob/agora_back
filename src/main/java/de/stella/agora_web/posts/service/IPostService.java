package de.stella.agora_web.posts.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.stella.agora_web.comment.controller.dto.CommentDTO;
import de.stella.agora_web.comment.model.Comment;
import de.stella.agora_web.posts.controller.dto.PostDTO;
import de.stella.agora_web.posts.controller.dto.PostResponseDTO;
import de.stella.agora_web.posts.controller.dto.PostSummaryDTO;
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

    List<Post> getPostsByUserId(Long userId);

    Post update(PostDTO postDTO, Long id);

    void archivePost(Long postId);

    void unArchivePost(Long postId);

    List<Comment> getCommentsByPostId(Long postId);

    List<Reply> getRepliesByCommentId(Long commentId);

    void createComment(Long postId, CommentDTO commentDTO);

    void createReply(Long commentId, @SuppressWarnings("rawtypes") ReplyDTO replyDTO);

    void deleteComment(Long commentId);

    void deleteReply(Long replyId);

    // Métodos para "loves" usando la tabla auxiliar
    void lovePost(Long postId, Long userId);

    void unlovePost(Long postId, Long userId);

    Integer getLoveCount(Long postId);

    void save(Post post);

    Page<PostSummaryDTO> getAllPostsWithCounts(Pageable pageable);

    // ========== MÉTODOS OPTIMIZADOS CON DTOs ==========
    PostResponseDTO getPostResponseById(Long postId);

    List<PostResponseDTO> getPostsResponseByUserId(Long userId);

    PostResponseDTO createPostResponse(PostDTO postDTO);

    PostResponseDTO updatePostResponse(PostDTO postDTO, Long id);
}
