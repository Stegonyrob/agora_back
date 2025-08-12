package de.stella.agora_web.image.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import de.stella.agora_web.image.controller.dto.PostImageDTO;

/**
 * Servicio para imágenes de posts - CONSISTENTE con IEventImageService. ✅
 * MISMOS MÉTODOS: Misma funcionalidad que events pero para posts privados
 */
public interface IPostImageService {

    // ========== MÉTODOS BÁSICOS CRUD ==========
    PostImageDTO createPostImage(PostImageDTO postImageDTO);

    PostImageDTO updatePostImage(Long id, PostImageDTO postImageDTO);

    void deletePostImage(Long id);

    List<PostImageDTO> getImagesByPostId(Long postId);

    void deleteImagesByPostId(Long postId);

    // ✅ MÉTODO para obtener datos binarios de imagen (necesario para mostrar imágenes)
    byte[] getPostImageData(Long id);

    // ✅ MÉTODO para obtener información de imagen por ID
    PostImageDTO getPostImageById(Long id);

    // ========== MÉTODOS CONSISTENTES CON EVENTIMAGESERVICE ==========
    // ✅ CONSISTENTE: Método para eliminar múltiples imágenes por IDs
    void deleteMultiplePostImages(List<Long> imageIds);

    // ✅ CONSISTENTE: Método para manejar archivos multipart
    List<PostImageDTO> processAndSaveImages(MultipartFile[] files, Long postId);

    // ✅ CONSISTENTE: Método para validar archivos de imagen
    boolean isValidImageFile(MultipartFile file);
}
