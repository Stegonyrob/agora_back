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
import de.stella.agora_web.tags.dto.PostSummaryDTO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "${api-endpoint}/")
public class PostController {

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
    public ResponseEntity<Post> update(@PathVariable("id") Long id, @Valid @RequestBody PostDTO postDTO) {
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

    // --- NUEVOS ENDPOINTS PARA LOVES ---
    // Marcar un post como favorito (love)
    @PutMapping("/posts/{postId}/love")
    public ResponseEntity<Void> lovePost(@PathVariable Long postId, @RequestParam Long userId) {
        // ✅ CUMPLE SRP: Validaciones separadas en método helper
        if (!isValidLoveRequest(postId, userId)) {
            return ResponseEntity.badRequest().build();
        }
        try {
            postService.lovePost(postId, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Quitar favorito (unlove)
    @PutMapping("/posts/{postId}/unlove")
    public ResponseEntity<Void> unlovePost(@PathVariable Long postId, @RequestParam Long userId) {
        // ✅ CUMPLE SRP: Reutilizar método helper de validación
        if (!isValidLoveRequest(postId, userId)) {
            return ResponseEntity.badRequest().build();
        }
        try {
            postService.unlovePost(postId, userId);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Obtener el número de favoritos (loves) de un post
    @GetMapping("/posts/{postId}/loves/count")
    public ResponseEntity<Integer> getLoveCount(@PathVariable Long postId) {
        try {
            Integer count = postService.getLoveCount(postId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // --- FIN ENDPOINTS LOVES ---
    // ========== MÉTODOS HELPER PARA CUMPLIR SRP ==========
    /**
     * Valida los parámetros para operaciones de love/unlove. Separa la lógica
     * de validación del endpoint.
     */
    private boolean isValidLoveRequest(Long postId, Long userId) {
        return postId != null && userId != null && postId > 0 && userId > 0;
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
