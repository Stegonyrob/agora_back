package de.stella.agora_web.image.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import de.stella.agora_web.image.controller.dto.ImageIdListDTO;
import de.stella.agora_web.image.controller.dto.PostImageDTO;
import de.stella.agora_web.image.service.IPostImageService;
import lombok.RequiredArgsConstructor;

/**
 * Controlador para imágenes de posts - REPLICANDO TextImageController ✅
 * DIFERENCIA: Posts requieren autenticación USER/ADMIN para GET, resto ADMIN
 * PATRÓN: Mismo que TextImageController pero con autenticación para posts
 * privados
 */
@RestController
@RequestMapping("${api-endpoint}/post-images")
@RequiredArgsConstructor
public class PostImageController {

    private final IPostImageService postImageService;

    // ========== ENDPOINTS SIGUIENDO PATRÓN DE TEXT-IMAGES ==========
    /**
     * Obtiene información específica de una imagen por ID. PATRÓN: Igual que
     * TextImageController pero requiere autenticación (posts privados)
     */
    @GetMapping("/image/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<PostImageDTO> getPostImage(@PathVariable Long id) {
        return ResponseEntity.ok(postImageService.getPostImageById(id));
    }

    /**
     * Obtiene todas las imágenes de un post específico. PATRÓN: Igual que
     * TextImageController pero requiere autenticación (posts privados)
     */
    @GetMapping("/{postId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<PostImageDTO>> getImagesByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postImageService.getImagesByPostId(postId));
    }

    // ========== ENDPOINTS ADMINISTRATIVOS - SIGUIENDO PATRÓN TEXT-IMAGES ==========
    /**
     * Crea una imagen para un post - SOLO ADMIN PATRÓN: Exactamente igual que
     * TextImageController
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PostImageDTO> uploadPostImage(@RequestBody PostImageDTO dto) {
        return ResponseEntity.ok(postImageService.createPostImage(dto));
    }

    /**
     * Sube múltiples imágenes desde archivos - SOLO ADMIN PATRÓN: Exactamente
     * igual que TextImageController
     */
    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PostImageDTO>> uploadMultiplePostImages(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("postId") Long postId) {

        try {
            List<PostImageDTO> savedImages = postImageService.processAndSaveImages(files, postId);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedImages);
        } catch (Exception _) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Elimina una imagen específica - SOLO ADMIN PATRÓN: Exactamente igual que
     * TextImageController
     */
    @DeleteMapping("/image/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePostImage(@PathVariable Long id) {
        postImageService.deletePostImage(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Elimina múltiples imágenes por IDs - SOLO ADMIN PATRÓN: Igual que
     * TextImageController pero usando ImageIdListDTO (ya existente)
     */
    @DeleteMapping("/delete-multiple")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMultiplePostImages(@RequestBody ImageIdListDTO dto) {
        try {
            postImageService.deleteMultiplePostImages(dto.getImageIds());
            return ResponseEntity.noContent().build();
        } catch (Exception _) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Elimina todas las imágenes de un post - SOLO ADMIN PATRÓN: Siguiendo el
     * patrón de TextImageController y EventImageController
     */
    @DeleteMapping("/{postId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteImagesByPostId(@PathVariable Long postId) {
        postImageService.deleteImagesByPostId(postId);
        return ResponseEntity.noContent().build();
    }
}
