package de.stella.agora_web.image.service;

import java.util.List;

import de.stella.agora_web.image.controller.dto.PostImageDTO;

public interface IPostImageService {
    PostImageDTO createPostImage(PostImageDTO postImageDTO);

    PostImageDTO updatePostImage(Long id, PostImageDTO postImageDTO);

    void deletePostImage(Long id);

    List<PostImageDTO> getImagesByPostId(Long postId);

    public void deleteImagesByPostId(Long postId);
}