package de.stella.agora_web.image.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.stella.agora_web.image.controller.dto.PostImageDTO;
import de.stella.agora_web.image.module.PostImage;
import de.stella.agora_web.image.repository.PostImageRepository;
import de.stella.agora_web.image.service.IPostImageService;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.posts.repository.PostRepository;

@Service
@Transactional
public class PostImageServiceImpl implements IPostImageService {

    private final PostImageRepository postImageRepository;
    private final PostRepository postRepository;

    public PostImageServiceImpl(PostImageRepository postImageRepository, PostRepository postRepository) {
        this.postImageRepository = postImageRepository;
        this.postRepository = postRepository;
    }

    @Override
    public PostImageDTO createPostImage(PostImageDTO postImageDTO) {
        Post post = postRepository.findById(postImageDTO.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        PostImage postImage = PostImage.builder().imageName(postImageDTO.getImageName())
                .isMainImage(postImageDTO.isMainImage()).post(post).build();

        PostImage savedImage = postImageRepository.save(postImage);
        return mapToDTO(savedImage);
    }

    @Override
    public PostImageDTO updatePostImage(Long id, PostImageDTO postImageDTO) {
        PostImage postImage = postImageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PostImage not found"));

        postImage.setImageName(postImageDTO.getImageName());
        postImage.setMainImage(postImageDTO.isMainImage());

        PostImage updatedImage = postImageRepository.save(postImage);
        return mapToDTO(updatedImage);
    }

    @Override
    public void deletePostImage(Long id) {
        postImageRepository.deleteById(id);
    }

    @Override
    public List<PostImageDTO> getImagesByPostId(Long postId) {
        return postImageRepository.findByPostId(postId).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private PostImageDTO mapToDTO(PostImage postImage) {
        return PostImageDTO.builder().id(postImage.getId()).imageName(postImage.getImageName())
                .isMainImage(postImage.isMainImage()).postId(postImage.getPost().getId().longValue()).build();
    }

    @Override
    @Transactional
    public void deleteImagesByPostId(Long postId) {
        List<PostImage> images = postImageRepository.findByPostId(postId);
        for (PostImage image : images) {
            postImageRepository.deleteById(image.getId());
        }
    }
}