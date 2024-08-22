package de.stella.agora_web.posts.services.impl;

import de.stella.agora_web.posts.controller.dto.PostDTO;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.posts.repository.PostRepository;
import de.stella.agora_web.posts.services.IPostService;
import de.stella.agora_web.tags.model.Tag;
import de.stella.agora_web.tags.service.ITagService;
import de.stella.agora_web.user.exceptions.UserNotFoundException;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.services.impl.UserServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements IPostService {

  private final PostRepository postRepository;
  private final UserServiceImpl userService;
  private final ITagService tagService;

  public PostServiceImpl(
    PostRepository postRepository,
    UserServiceImpl userService,
    ITagService tagService
  ) {
    this.postRepository = postRepository;
    this.userService = userService;
    this.tagService = tagService;
  }

  @Override
  public List<Post> getAllPosts() {
    return postRepository.findAll();
  }

  @Override
  public Post createPost(PostDTO postDTO, Long userId) {
    Optional<User> user = userService.findById(userId);
    if (user.isEmpty()) {
      throw new UserNotFoundException("User not found with ID: " + userId);
    }

    Post post = new Post();
    post.setUser(user.get());
    post.setTitle(postDTO.getTitle());
    post.setMessage(postDTO.getMessage());
    post.setCreationDate(postDTO.getCreationDate());

    List<Tag> tags = new ArrayList<>();
    for (String tagName : postDTO.getTags()) {
      Tag tag = tagService.getTagByName(tagName);
      if (tag == null) {
        tag = tagService.createTag(tagName);
      }
      tags.add(tag);
    }

    List<String> hashtags = tagService.extractHashtags(post.getMessage());
    for (String hashtag : hashtags) {
      Tag tag = tagService.getTagByName(hashtag);
      if (tag == null) {
        tag = tagService.createTag(hashtag);
      }
      tags.add(tag);
    }

    post.setTags(tags);

    return postRepository.save(post);
  }

  public Post updatePost(Long id, PostDTO postDTO) {
    Post existingPost = postRepository.findById(id).orElse(null);
    if (existingPost != null) {
      existingPost.setTitle(postDTO.getTitle());
      existingPost.setMessage(postDTO.getMessage());

      existingPost.getTags().clear();

      List<Tag> tags = new ArrayList<>();
      for (String tagName : postDTO.getTags()) {
        Tag tag = tagService.getTagByName(tagName);
        if (tag == null) {
          tag = tagService.createTag(tagName);
        }
        tags.add(tag);
      }

      List<String> hashtags = tagService.extractHashtags(postDTO.getMessage());
      for (String hashtag : hashtags) {
        Tag tag = tagService.getTagByName(hashtag);
        if (tag == null) {
          tag = tagService.createTag(hashtag);
        }
        tags.add(tag);
      }

      existingPost.setTags(tags);

      return postRepository.save(existingPost);
    }

    return null;
  }

  @Override
  public void deletePost(Long id) {
    postRepository.deleteById(id);
  }

  @Override
  public Post getById(Long postId) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Post updatePost(PostDTO postDTO, Long postId) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Post save(PostDTO postDTO) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<String> extractHashtags(String message) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Tag getTagByName(String tagName) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Tag createTag(String tagName) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<Tag> getTagsByPostId(Long postId) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<Post> getPostsByTagName(String tagName) {
    List<Post> posts = postRepository.findByTags_Name(tagName);
    return posts;
  }

  @Override
  public List<Post> getPostsByUserId(Long userId) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void deleteById(Long id) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Post update(PostDTO postDTO, Long id) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
