package de.stella.agora_web.posts.controller;

import de.stella.agora_web.generics.IGenericFullService;
import de.stella.agora_web.generics.IGenericSearchService;
import de.stella.agora_web.messages.Message;
import de.stella.agora_web.posts.controller.dto.PostDTO;
import de.stella.agora_web.posts.model.Post;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping(path = "${api-endpoint}")
public class PostController {

  IGenericFullService<Post, PostDTO> service;
  IGenericSearchService<Post> searchService;

  @GetMapping("/any/posts")
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')") // Permitir acceso a ADMIN y USER
  public List<Post> index() {
    return service.getAll();
  }

  @GetMapping("/any/posts/{id}")
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')") // Permitir acceso a ADMIN y USER
  public ResponseEntity<Post> findById(@PathVariable("id") @NonNull Long id)
    throws Exception {
    Post post = service.getById(id);
    return ResponseEntity.status(HttpStatus.OK).body(post);
  }

  @PostMapping("/admin/posts")
  @PreAuthorize("hasRole('ADMIN')") // Solo ADMIN puede crear posts
  public ResponseEntity<Post> create(@RequestBody PostDTO post) {
    Post newPost = service.save();
    return ResponseEntity.status(201).body(newPost);
  }

  @PutMapping("/admin/posts/{id}")
  @PreAuthorize("hasRole('ADMIN')") // Solo ADMIN puede actualizar posts
  public ResponseEntity<Post> update(
    @PathVariable("id") Long id,
    @RequestBody PostDTO postDTO
  ) throws Exception {
    Post updatePost = service.update(id, postDTO);
    return ResponseEntity.status(200).body(updatePost);
  }

  @DeleteMapping(path = "/admin/posts/{id}")
  @PreAuthorize("hasRole('ADMIN')") // Solo ADMIN puede eliminar posts
  public ResponseEntity<Message> remove(@PathVariable("id") @NonNull Long id)
    throws Exception {
    Message delete = service.delete(id);
    return ResponseEntity.status(200).body(delete);
  }

  @GetMapping("/user/{userId}")
  @PreAuthorize("hasRole('USER')") // Solo USER puede obtener posts por userId
  public ResponseEntity<List<Post>> getPostsByUserId(
    @PathVariable Long userId
  ) {
    List<Post> posts = service.findPostsByUserId(userId);
    return ResponseEntity.ok(posts);
  }
}
