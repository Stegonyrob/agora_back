package de.stella.agora_web.posts.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.posts.controller.dto.PostDTO;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.posts.service.IPostService;

@RestController
@RequestMapping(path = "${api-endpoint}/")
public class PostController {

    private final IPostService postService;

    public PostController(IPostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public List<Post> index() {
        return postService.getAllPosts();
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> show(@PathVariable Long id) {
        Post post = postService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(post);
    }

    @PostMapping(path = "/posts")
    public ResponseEntity<Post> create(@RequestBody PostDTO postDTO) {
        if (postDTO == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Post newPost = postService.save(postDTO);
            return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(newPost);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("posts/{id}")
    public ResponseEntity<Post> update(@PathVariable("id") Long id, @RequestBody PostDTO postDTO) {
        Post post = postService.update(postDTO, id);
        return ResponseEntity.accepted().body(post);
    }

    @PatchMapping("/posts/{id}/archive")
    public ResponseEntity<Void> archivePost(@PathVariable Long id, @RequestParam Boolean archive) {
        try {
            Post post = postService.getById(id);
            if (post == null) {
                return ResponseEntity.notFound().build();
            }
            if (archive) {
                postService.archivePost(id);
            } else {
                postService.unArchivePost(id);
            }
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Post>> getPostsByUserId(@PathVariable Long userId) {
        List<Post> posts = postService.getPostsByUserId(userId);
        if (posts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(posts);
    }

    @GetMapping("posts/tag/{tagName}")
    public ResponseEntity<List<Post>> getPostsByTagName(@PathVariable String tagName) {
        List<Post> posts = postService.getPostsByTagName(tagName);
        return ResponseEntity.ok(posts);
    }

    @PutMapping("/posts/{id}/favorite")
    public ResponseEntity<Void> favoritePost(@PathVariable Long id) {
        Post post = postService.getById(id);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        post.setLoves(post.getLoves() + 1);
        postService.save(post);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/posts/{id}/unfavorite")
    public ResponseEntity<Void> unfavoritePost(@PathVariable Long id) {
        Post post = postService.getById(id);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        post.setLoves(Math.max(0, post.getLoves() - 1));
        postService.save(post);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/posts/{id}/favorites/count")
    public ResponseEntity<Integer> getFavoritesCount(@PathVariable Long id) {
        Post post = postService.getById(id);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(post.getLoves());
    }

    public static PostDTO toPostDTO(Post post) {
        if (post == null) {
            return null;
        }
        return PostDTO.builder()
                .id(post.getId())
                .userId(post.getUser() != null ? post.getUser().getId() : null)
                .title(post.getTitle())
                .message(post.getMessage())
                // Agrega aquí otros campos si tu PostDTO los tiene
                .build();
    }
}
