package de.stella.agora_web.image.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
 * Controlador para imágenes de posts - CONSISTENTE con EventImageController. ✅
 * DIFERENCIA CLAVE: Posts son PRIVADOS (requieren autenticación) ✅ MISMO
 * PATRÓN: Mismos endpoints que events pero con seguridad privada
 */
@RestController
@RequestMapping("${api-endpoint}/post-images")
@RequiredArgsConstructor
public class PostImageController {

    private final IPostImageService postImageService;

    // ========== ENDPOINTS CONSISTENTES CON EVENTIMAGECONTROLLER ==========
    /**
     * Obtiene todas las imágenes de un post específico. ✅ CONSISTENTE con
     * EventImageController.getImagesByEvent() ✅ DIFERENCIA: Requiere
     * autenticación (posts privados)
     */
    @GetMapping("/post/{postId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<PostImageDTO>> getImagesByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postImageService.getImagesByPostId(postId));
    }

    /**
     * Obtiene información específica de una imagen por ID. ✅ CONSISTENTE con
     * EventImageController (info de imagen) ✅ DIFERENCIA: Requiere
     * autenticación (posts privados)
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<PostImageDTO> getPostImage(@PathVariable Long id) {
        return ResponseEntity.ok(postImageService.getPostImageById(id));
    }

    /**
     * Obtiene los datos binarios de una imagen específica para mostrarla. ✅
     * CONSISTENTE con EventImageController.getEventImageData() ✅ DIFERENCIA:
     * Requiere autenticación (posts privados)
     */
    @GetMapping("/{id}/data")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<byte[]> getPostImageData(@PathVariable Long id) {
        try {
            byte[] imageData = postImageService.getPostImageData(id);

            // Obtener información de la imagen para establecer el content type correcto
            PostImageDTO imageInfo = postImageService.getPostImageById(id);
            String imageName = imageInfo.getImageName();

            // Determinar content type basado en la extensión
            MediaType mediaType = MediaType.IMAGE_JPEG; // default
            if (imageName != null) {
                String extension = imageName.toLowerCase();
                if (extension.endsWith(".png")) {
                    mediaType = MediaType.IMAGE_PNG;
                } else if (extension.endsWith(".gif")) {
                    mediaType = MediaType.IMAGE_GIF;
                } else if (extension.endsWith(".webp")) {
                    mediaType = MediaType.valueOf("image/webp");
                }
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType);
            headers.setContentLength(imageData.length);
            // ✅ CONSISTENTE: Mismo formato que events pero con inline
            headers.add("Content-Disposition", "inline; filename=\"" + imageName + "\"");

            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Crea una imagen individual para un post. ✅ CONSISTENTE con
     * EventImageController.uploadEventImage() ✅ DIFERENCIA: Solo ADMIN (más
     * restrictivo que events)
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PostImageDTO> uploadPostImage(@RequestBody PostImageDTO dto) {
        return ResponseEntity.ok(postImageService.createPostImage(dto));
    }

    /**
     * Sube múltiples imágenes a un post desde archivos. ✅ CONSISTENTE con
     * EventImageController.uploadMultipleEventImages() ✅ DIFERENCIA: Solo ADMIN
     * (más restrictivo que events)
     */
    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PostImageDTO>> uploadMultiplePostImages(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("postId") Long postId) {

        try {
            List<PostImageDTO> savedImages = postImageService.processAndSaveImages(files, postId);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedImages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Elimina una imagen específica. ✅ CONSISTENTE con
     * EventImageController.deleteEventImage() ✅ DIFERENCIA: Solo ADMIN (más
     * restrictivo que events)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePostImage(@PathVariable Long id) {
        postImageService.deletePostImage(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Elimina múltiples imágenes por IDs. ✅ CONSISTENTE con
     * EventImageController.deleteMultipleEventImages() ✅ DIFERENCIA: Solo ADMIN
     * (más restrictivo que events)
     */
    @DeleteMapping("/delete-multiple")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMultiplePostImages(@RequestBody ImageIdListDTO dto) {
        try {
            postImageService.deleteMultiplePostImages(dto.getImageIds());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
