package de.stella.agora_web.image.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import de.stella.agora_web.image.controller.dto.PostImageDTO;
import de.stella.agora_web.image.module.PostImage;
import de.stella.agora_web.image.repository.PostImageRepository;
import de.stella.agora_web.image.service.IPostImageService;
import de.stella.agora_web.image.service.ImageStorageService;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.posts.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio para imágenes de posts - CONSISTENTE con EventImageServiceImpl. ✅
 * MISMA FUNCIONALIDAD: Mismos métodos que events pero para posts privados
 */
@Slf4j
@Service
@Transactional
public class PostImageServiceImpl implements IPostImageService {

    private final PostImageRepository postImageRepository;
    private final PostRepository postRepository;
    private final ImageStorageService imageStorageService;

    // ✅ CONSISTENTE: Mismas constantes de validación que EventImageService
    private static final List<String> ALLOWED_CONTENT_TYPES = List.of(
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
    );
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final int MAX_IMAGES_PER_POST = 10;

    public PostImageServiceImpl(PostImageRepository postImageRepository, PostRepository postRepository, ImageStorageService imageStorageService) {
        this.postImageRepository = postImageRepository;
        this.postRepository = postRepository;
        this.imageStorageService = imageStorageService;
    }

    @Override
    public PostImageDTO createPostImage(PostImageDTO postImageDTO) {
        Post post = postRepository.findById(postImageDTO.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        PostImage postImage = PostImage.builder().imageName(postImageDTO.getImageName())
                .isMainImage(postImageDTO.isMainImage()).post(post).build();

        PostImage savedImage = postImageRepository.save(postImage);
        return mapToDTO(savedImage);
    }

    @Override
    public PostImageDTO updatePostImage(Long id, PostImageDTO postImageDTO) {
        PostImage postImage = postImageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PostImage not found"));

        postImage.setImageName(postImageDTO.getImageName());
        postImage.setMainImage(postImageDTO.isMainImage());

        PostImage updatedImage = postImageRepository.save(postImage);
        return mapToDTO(updatedImage);
    }

    @Override
    public void deletePostImage(Long id) {
        postImageRepository.deleteById(id);
    }

    @Override
    public List<PostImageDTO> getImagesByPostId(Long postId) {
        return postImageRepository.findByPostId(postId).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // NUEVO: Obtener información de imagen por ID
    @Override
    public PostImageDTO getPostImageById(Long id) {
        PostImage postImage = postImageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PostImage not found"));
        return mapToDTO(postImage);
    }

    // ========== MÉTODOS CONSISTENTES CON EVENTIMAGESERVICE ==========
    /**
     * ✅ CONSISTENTE: Elimina múltiples imágenes por IDs
     */
    @Override
    @Transactional
    public void deleteMultiplePostImages(List<Long> imageIds) {
        for (Long imageId : imageIds) {
            postImageRepository.deleteById(imageId);
        }
        log.info("Eliminadas {} imágenes de posts", imageIds.size());
    }

    /**
     * ✅ CONSISTENTE: Procesa y guarda múltiples archivos de imagen
     */
    @Override
    @Transactional
    public List<PostImageDTO> processAndSaveImages(MultipartFile[] files, Long postId) {
        List<PostImageDTO> savedImages = new ArrayList<>();

        if (files == null || files.length == 0) {
            return savedImages;
        }

        // Verificar que el post existe
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + postId));

        // Verificar límite de imágenes
        long currentImageCount = postImageRepository.findByPostId(postId).size();
        if (currentImageCount + files.length > MAX_IMAGES_PER_POST) {
            throw new IllegalArgumentException("Máximo " + MAX_IMAGES_PER_POST + " imágenes por post");
        }

        for (MultipartFile file : files) {
            if (!file.isEmpty() && isValidImageFile(file)) {
                try {
                    String relativePath = imageStorageService.storeImage(file);

                    PostImage newPostImage = PostImage.builder()
                            .imageName(file.getOriginalFilename())
                            .imagePath(relativePath)
                            .isMainImage(false)
                            .post(post)
                            .build();

                    PostImage savedImage = postImageRepository.save(newPostImage);
                    savedImages.add(mapToDTO(savedImage));

                } catch (RuntimeException e) {
                    log.error("Error al procesar imagen: {}", file.getOriginalFilename(), e);
                    throw new RuntimeException("Error al procesar imagen: " + file.getOriginalFilename(), e);
                }
            }
        }

        log.info("Procesadas {} imágenes para post {}", savedImages.size(), postId);
        return savedImages;
    }

    /**
     * ✅ CONSISTENTE: Valida archivos de imagen
     */
    @Override
    public boolean isValidImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        // Validar tamaño
        if (file.getSize() > MAX_FILE_SIZE) {
            log.warn("Archivo demasiado grande: {} bytes", file.getSize());
            return false;
        }

        // Validar tipo MIME
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            log.warn("Tipo de archivo no permitido: {}", contentType);
            return false;
        }

        return true;
    }

    // ========== MÉTODOS HELPER ==========
    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "jpg";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    private PostImageDTO mapToDTO(PostImage postImage) {
        return PostImageDTO.builder()
                .id(postImage.getId())
                .imageName(postImage.getImageName())
                .imagePath(postImage.getImagePath())
                .postId(postImage.getPost().getId())
                .build();
    }

    @Override
    public void deleteImagesByPostId(Long postId) {
        List<PostImage> postImages = postImageRepository.findByPostId(postId);
        for (PostImage postImage : postImages) {
            postImageRepository.deleteById(postImage.getId());
        }
    }

    @Override
    public byte[] getPostImageData(Long id) {
        PostImage postImage = postImageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PostImage not found with id " + id));

        return imageStorageService.loadImage(postImage.getImagePath());
    }
}
