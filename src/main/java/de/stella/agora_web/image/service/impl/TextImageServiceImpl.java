package de.stella.agora_web.image.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import de.stella.agora_web.image.controller.dto.TextImageDTO;
import de.stella.agora_web.image.module.TextImage;
import de.stella.agora_web.image.repository.TextImageRepository;
import de.stella.agora_web.image.service.ITextImageService;
import de.stella.agora_web.image.service.ImageStorageService;
import de.stella.agora_web.texts.model.Text;
import de.stella.agora_web.texts.repository.TextRepository;
import lombok.RequiredArgsConstructor;

/**
 * Implementación del servicio de imágenes de texto siguiendo principios SOLID
 * SRP: Responsabilidad única de manejar la lógica de negocio de imágenes de
 * texto OCP: Abierto para extensión, cerrado para modificación LSP: Sustituible
 * por la interfaz ITextImageService DIP: Depende de abstracción
 * TextImageRepository
 */
@Service
@RequiredArgsConstructor
public class TextImageServiceImpl implements ITextImageService {

    private static final Logger log = LoggerFactory.getLogger(TextImageServiceImpl.class);
    private static final int MAX_IMAGES_PER_TEXT = 10;
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB
    private static final java.util.Set<String> ALLOWED_CONTENT_TYPES = java.util.Set.of(
            "image/jpeg", "image/png", "image/gif", "image/bmp", "image/webp"
    );

    private final TextImageRepository textImageRepository;
    private final ImageStorageService imageStorageService;
    private final TextRepository textItemRepository;

    @Override
    public List<TextImageDTO> getAllTextImages() {
        return textImageRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TextImageDTO> getImagesByTextId(Long textId) {
        return textImageRepository.findByTextId(textId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TextImageDTO getTextImageById(Long id) {
        return textImageRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("TextImage not found with id " + id));
    }

    @Override
    public TextImageDTO createTextImage(TextImageDTO dto) {
        // Cargar el Text
        Text text = textItemRepository.findById(dto.getTextId())
                .orElseThrow(() -> new IllegalArgumentException("Text not found with id: " + dto.getTextId()));

        TextImage textImage = new TextImage();
        textImage.setText(text);
        textImage.setImageName(dto.getImageName());

        TextImage saved = textImageRepository.save(textImage);
        log.info("Imagen de texto creada: {} para texto {}", saved.getId(), saved.getText().getId());
        return mapToDTO(saved);
    }

    @Override
    public List<TextImageDTO> processAndSaveImages(MultipartFile[] files, Long textId) {
        List<TextImageDTO> savedImages = new ArrayList<>();
        if (files == null || files.length == 0) {
            return savedImages;
        }

        // Cargar el TextItem
        Text textItem = textItemRepository.findById(textId)
                .orElseThrow(() -> new IllegalArgumentException("Text not found with id: " + textId));

        // Verificar límite de imágenes
        long currentImageCount = textImageRepository.findByTextId(textId).size();
        if (currentImageCount + files.length > MAX_IMAGES_PER_TEXT) {
            throw new IllegalArgumentException("Máximo " + MAX_IMAGES_PER_TEXT + " imágenes por texto");
        }

        for (MultipartFile file : files) {
            if (!file.isEmpty() && isValidImageFile(file)) {
                try {
                    String relativePath = imageStorageService.storeImage(file);

                    TextImage textImage = TextImage.builder()
                            .imageName(file.getOriginalFilename())
                            .imagePath(relativePath)
                            .text(textItem)
                            .build();

                    TextImage savedImage = textImageRepository.save(textImage);
                    savedImages.add(mapToDTO(savedImage));

                } catch (RuntimeException e) {
                    throw new RuntimeException("Error al procesar imagen: " + file.getOriginalFilename(), e);
                }
            }
        }
        return savedImages;
    }

    @Override
    public void deleteTextImage(Long id) {
        textImageRepository.deleteById(id);
    }

    @Override
    public void deleteMultipleTextImages(List<Long> imageIds) {
        if (imageIds == null || imageIds.isEmpty()) {
            return;
        }
        // Se eliminan las imágenes que existen en la base de datos
        List<TextImage> imagesToDelete = textImageRepository.findAllById(imageIds);
        textImageRepository.deleteAll(imagesToDelete);
        log.info("Eliminadas {} imágenes de texto", imagesToDelete.size());
    }

    @Override
    public TextImageDTO updateTextImage(Long id, TextImageDTO dto) {
        TextImage existingImage = textImageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TextImage not found with id " + id));

        if (dto.getImageName() != null) {
            existingImage.setImageName(dto.getImageName());
        }

        TextImage updated = textImageRepository.save(existingImage);
        log.info("Imagen de texto actualizada: {}", id);
        return mapToDTO(updated);
    }

    @Override
    public void deleteImagesByTextId(Long textId) {
        List<TextImage> images = textImageRepository.findByTextId(textId);
        if (!images.isEmpty()) {
            textImageRepository.deleteAll(images);
            log.info("Eliminadas {} imágenes para texto {}", images.size(), textId);
        }
    }

    @Override
    public String getTextImagePath(Long id) {
        return textImageRepository.findById(id)
                .map(TextImage::getImagePath)
                .orElseThrow(() -> new RuntimeException("TextImage not found with id " + id));
    }

    // ========== MÉTODOS HELPER (SRP) ==========
    /**
     * Convierte entidad TextImage a DTO SRP: Responsabilidad única de mapeo
     * entidad-DTO
     */
    private TextImageDTO mapToDTO(TextImage textImage) {
        return TextImageDTO.builder()
                .id(textImage.getId())
                .textId(textImage.getText().getId())
                .imageName(textImage.getImageName())
                .imagePath(textImage.getImagePath())
                .build();
    }

    /**
     * Valida si un archivo es una imagen válida SRP: Responsabilidad única de
     * validación de archivos OCP: Extensible para nuevos criterios de
     * validación
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

    @Override
    public byte[] getTextImageData(Long id) {
        TextImage textImage = textImageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TextImage not found with id " + id));
        return imageStorageService.loadImage(textImage.getImagePath());
    }

}
