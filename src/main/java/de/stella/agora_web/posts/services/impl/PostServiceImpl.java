package de.stella.agora_web.posts.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.stella.agora_web.posts.controller.dto.PostDTO;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.posts.repository.PostRepository;
import de.stella.agora_web.posts.services.IPostService;
import de.stella.agora_web.tags.module.Tag;
import de.stella.agora_web.tags.service.ITagService;
import de.stella.agora_web.user.exceptions.UserNotFoundException;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.services.impl.UserServiceImpl;

@Service
public class PostServiceImpl implements IPostService {
    private final PostRepository postRepository;
    private final UserServiceImpl userService;
    @Autowired
    private ITagService tagService;

    public PostServiceImpl(PostRepository postRepository, UserServiceImpl userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public Post createPost(PostDTO postDTO, Long userId) {
        Optional<User> user = userService.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }

        Post post = new Post();
        post.setUser(user.get());
        post.setMessage(postDTO.getMessage());
        post.setTitle(postDTO.getTitle());
        post.setCreationDate(postDTO.getCreationDate());

        // Add tags from tag list
        List<Tag> tags = new ArrayList<>();
        for (String tagName : postDTO.getTags()) {
            Tag tag = tagService.getTagByName(tagName);
            if (tag == null) {
                tag = tagService.createTag(tagName);
            }
            tags.add(tag);
        }

        // Add tags from hashtags in post message
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

    @Override
    public Post updatePost(Long id, PostDTO postDTO) {
        Post existingPost = postRepository.findById(id).orElse(null);
        if (existingPost != null) {
            existingPost.setTitle(postDTO.getTitle());
            existingPost.setMessage(postDTO.getMessage());

            // Remove existing tags
            existingPost.getTags().clear();

            // Add tags from tag list
            List<Tag> tags = new ArrayList<>();
            for (String tagName : postDTO.getTags()) {
                Tag tag = tagService.getTagByName(tagName);
                if (tag == null) {
                    tag = tagService.createTag(tagName);
                }
                tags.add(tag);
            }

            // Add tags from hashtags in post message
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
        return postRepository.findById(postId).orElse(null);
    }

    @Override
    public Post save(PostDTO postDTO) {
        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setMessage(postDTO.getMessage());

        return postRepository.save(post);
    }

    @Override
    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public Post update(PostDTO postDTO, Long id) {
        Post existingPost = postRepository.findById(id).orElse(null);
        if (existingPost != null) {
            existingPost.setTitle(postDTO.getTitle());
            existingPost.setMessage(postDTO.getMessage());

            return postRepository.save(existingPost);
        }
        return null;
    }

    @Override
    public List<Post> findPostsByUserId(Long userId) {

        return postRepository.findByUserId(userId);
    }
}