package de.stella.agora_web.posts.services;

import de.stella.agora_web.posts.controller.dto.PostDTO;
import de.stella.agora_web.posts.model.Post;
import java.util.List;
import lombok.NonNull;

public interface IPostService {
  List<Post> getAllPosts();

  Post getPostById(Long id);

  Post updatePost(Long id, PostDTO postDTO);

  void deletePost(Long id);

  Post save(Post postDTO);

  void deleteById(Long id);

  Post update(PostDTO postDTO, Long id);

  Post getById(@NonNull Long id);

  List<Post> findPostsByUserId(Long userId);

  Post createPost(PostDTO postDTO, Long userId);
}
