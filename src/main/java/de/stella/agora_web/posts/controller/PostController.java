package de.stella.agora_web.posts.controller;

import de.stella.agora_web.posts.controller.dto.PostDTO;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.posts.services.IPostService;
import java.util.List;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "${api-endpoint}/")
public class PostController {

  private final IPostService postService;

  public PostController(IPostService postService) {
    this.postService = postService;
  }

  @PostMapping("any/posts")
  @PreAuthorize("hasRole('USER','ADMIN')")
  public ResponseEntity<Post> createPost(@RequestBody PostDTO postDTO) {
    Post post = postService.createPost(postDTO, null);
    return ResponseEntity.status(HttpStatus.CREATED).body(post);
  }

  @GetMapping
  public List<Post> index() {
    return postService.getAllPosts();
  }

  @GetMapping("any/posts/{id}")
  public ResponseEntity<Post> show(@NonNull @PathVariable Long id) {
    Post post = postService.getById(id);
    return ResponseEntity.status(HttpStatus.OK).body(post);
  }

  @PostMapping("admin/posts/store")
  public ResponseEntity<Post> store(@RequestBody PostDTO postDTO) {
    Post post = postService.save(postDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(post);
  }

  @DeleteMapping("admin/posts/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    postService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("admin/posts/{id}")
  public ResponseEntity<Post> update(
    @PathVariable Long id,
    @RequestBody PostDTO postDTO
  ) {
    Post post = postService.update(postDTO, id);
    return ResponseEntity.accepted().body(post);
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<Post>> getPostsByUserId(
    @PathVariable Long userId
  ) {
    List<Post> posts = postService.findPostsByUserId(userId);
    return ResponseEntity.ok(posts);
  }
}
