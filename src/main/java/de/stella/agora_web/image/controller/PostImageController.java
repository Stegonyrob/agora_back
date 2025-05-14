package de.stella.agora_web.image.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.image.controller.dto.PostImageDTO;
import de.stella.agora_web.image.service.IPostImageService;

@RestController
@RequestMapping("/api/v1/post-images")
public class PostImageController {

    private final IPostImageService postImageService;

    public PostImageController(IPostImageService postImageService) {
        this.postImageService = postImageService;
    }

    @PostMapping
    public ResponseEntity<PostImageDTO> createPostImage(@RequestBody PostImageDTO postImageDTO) {
        return ResponseEntity.ok(postImageService.createPostImage(postImageDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostImageDTO> updatePostImage(@PathVariable Long id, @RequestBody PostImageDTO postImageDTO) {
        return ResponseEntity.ok(postImageService.updatePostImage(id, postImageDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostImage(@PathVariable Long id) {
        postImageService.deletePostImage(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<PostImageDTO>> getImagesByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(postImageService.getImagesByPostId(postId));
    }
}