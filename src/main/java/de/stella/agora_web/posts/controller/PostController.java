package de.stella.agora_web.posts.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import de.stella.agora_web.posts.controller.dto.PostResponseDTO;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.posts.service.IPostService;
import de.stella.agora_web.profiles.repository.ProfileRepository;
import de.stella.agora_web.tags.dto.PostSummaryDTO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unused")
@RestController
@RequestMapping(path = "${api-endpoint}/")
public class PostController {

    private ProfileRepository profileRepository;

    // ✅ CUMPLE SRP: Solo manejo de endpoints de posts
    private final IPostService postService;

    // ✅ CUMPLE DIP: Inyección por constructor (Spring maneja la interfaz)
    public PostController(IPostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public ResponseEntity<Page<PostSummaryDTO>> getAllPosts(Pageable pageable) {
        Page<PostSummaryDTO> posts = postService.getAllPostsWithCounts(pageable);
        if (posts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostResponseDTO> show(@PathVariable Long id) {
        PostResponseDTO post = postService.getPostResponseById(id);
        return ResponseEntity.status(HttpStatus.OK).body(post);
    }

    @PostMapping(path = "/posts")
    public ResponseEntity<Post> create(@Valid @RequestBody PostDTO postDTO) {
        if (postDTO == null) {
            log.warn("Intento de crear post con datos nulos");
            return ResponseEntity.badRequest().build();
        }

        log.info("Payload recibido para crear post: {}", postDTO);

        try {
            // ✅ APLICAR PATRÓN DE EVENTOS: Mapeo manual simple en el controlador
            Post newPost = new Post();
            newPost.setTitle(postDTO.getTitle());
            newPost.setMessage(postDTO.getMessage());
            newPost.setArchived(postDTO.isArchived());

            // ✅ SIMPLIFICAR: Usar método básico de guardado sin lógica compleja
            Post savedPost = postService.saveSimple(newPost);
            log.info("Post creado exitosamente con ID: {}", savedPost.getId());
            return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(savedPost);
        } catch (Exception e) {
            log.error("Error al crear post: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("posts/{id}")
    public ResponseEntity<Post> update(@PathVariable Long id, @Valid @RequestBody PostDTO postDTO) {
        Post post = postService.update(postDTO, id);
        return ResponseEntity.accepted().body(post);
    }

    @PatchMapping("/posts/{id}/archive")
    public ResponseEntity<Void> archivePost(@PathVariable Long id, @RequestParam(name = "archive", required = true) boolean archive) {
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
            log.error("Error archiving/unarchiving post with id {}: {}", id, e.getMessage());
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
