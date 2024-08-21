package de.stella.agora_web.posts.services;

import de.stella.agora_web.posts.controller.dto.PostDTO;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.tags.model.Tag;
import java.util.List;

public interface IPostService {
  List<Post> getAllPosts();

  Post createPost(PostDTO postDTO, Long userId);

  Post getById(Long postId);

  void deletePost(Long postId);

  Post updatePost(PostDTO postDTO, Long postId);

  Post save(PostDTO postDTO);

  List<String> extractHashtags(String message);

  Tag getTagByName(String tagName);

  Tag createTag(String tagName);

  List<Tag> getTagsByPostId(Long postId);

  List<Post> getPostsByTag(String tagName);

  List<Post> getPostsByUserId(Long userId);

  void deleteById(Long id);

  Post update(PostDTO postDTO, Long id);

  List<Post> getPostsByTagId(Long userId, Long tagId);
}
