package de.stella.agora_web.posts.controller;

import de.stella.agora_web.posts.controller.dto.PostDTO;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.posts.services.IPostService;
import java.util.List;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
  } //re¡separar en dos controlladoress postuser y postadmin

  @PostMapping("any/posts")
  @PreAuthorize("hasRole('USER')") //retirar esta
  public ResponseEntity<Post> createPost(@RequestBody PostDTO postDTO) {
    Post post = postService.createPost(postDTO, null);
    return ResponseEntity.status(HttpStatus.CREATED).body(post);
  }

  @GetMapping("any/posts")
  public List<Post> index() {
    return postService.getAllPosts();
  }

  //unica ru¡ta get compartido
  @GetMapping("any/posts/{id}") //innecesaria por que en realidad usarias por tags paa encontrar un post
  public ResponseEntity<Post> show(@NonNull @PathVariable("id") Long id) {
    Post post = postService.getById(id);
    return ResponseEntity.status(HttpStatus.OK).body(post);
  }

  @PostMapping("any/posts/store")
  public ResponseEntity<Post> store(@RequestBody PostDTO postDTO) {
    Post post = postService.save(postDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(post);
  }

  @PostMapping(path = "/admin/posts") // ok
  public ResponseEntity<Post> create(@RequestBody PostDTO post)
    throws Exception {
    Post newPost = postService.save(post);

    return ResponseEntity
      .status(201)
      .contentType(MediaType.APPLICATION_JSON)
      .body(newPost);
  }

  @DeleteMapping("admin/posts/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) { // no va 500
    postService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("admin/posts/{id}") //ok
  public ResponseEntity<Post> update(
    @PathVariable("id") Long id,
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
