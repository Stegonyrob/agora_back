package de.stella.agora_web.posts.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.generics.IGenericFullService;
import de.stella.agora_web.generics.IGenericSearchService;
import de.stella.agora_web.messages.Message;
import de.stella.agora_web.posts.controller.dto.PostDTO;
import de.stella.agora_web.posts.model.Post;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping(path = "${api-endpoint}")
public class PostController {

    IGenericFullService<Post, PostDTO> service;
    IGenericSearchService<Post> searchService;

    @GetMapping("/any/posts")
    public List<Post> index() {
        List<Post> post = service.getAll();
        return post;
    }

    @GetMapping("/any/posts/{id}")
    public ResponseEntity<Post> findById(@PathVariable("id") @NonNull Long id) throws Exception {

        Post post = service.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(post);
    }

    @PostMapping("/admin/posts")

    public ResponseEntity<Post> create(@RequestBody PostDTO post) {

        Post newPost = service.save(post);

        return ResponseEntity.status(201).body(newPost);
    }

    @PutMapping("/admin/posts/{id}")
    public ResponseEntity<Post> update(@PathVariable("id") Long id, @RequestBody PostDTO postDTO) throws Exception {
        Post updatePost = service.update(id, postDTO);
        Post updatedPost = updatePost;

        return ResponseEntity.status(200).body(updatedPost);
    }

    @DeleteMapping(path = "/admin/posts/{id}")
    public ResponseEntity<Message> remove(@PathVariable("id") @NonNull Long id) throws Exception {

        Message delete = service.delete(id);

        return ResponseEntity.status(200).body(delete);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Post>> getPostsByUserId(@PathVariable Long userId) {
        List<Post> posts = service.findPostsByUserId(userId);
        return ResponseEntity.ok(posts);
    }
}