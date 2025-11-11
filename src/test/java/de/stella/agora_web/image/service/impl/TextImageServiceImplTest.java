package de.stella.agora_web.image.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import de.stella.agora_web.image.controller.dto.TextImageDTO;
import de.stella.agora_web.image.module.TextImage;
import de.stella.agora_web.image.repository.TextImageRepository;
import de.stella.agora_web.image.service.ImageStorageService;
import de.stella.agora_web.texts.model.Text;
import de.stella.agora_web.texts.repository.TextRepository;
import jakarta.persistence.EntityNotFoundException;

/**
 * Tests comprehensivos para TextImageServiceImpl Cobertura: CRUD operations,
 * validación, excepciones, límites
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("TextImageServiceImpl - Tests de servicio de imágenes de texto")
class TextImageServiceImplTest {

    @Mock
    private TextImageRepository textImageRepository;

    @Mock
    private ImageStorageService imageStorageService;

    @Mock
    private TextRepository textItemRepository;

    @InjectMocks
    private TextImageServiceImpl textImageService;

    private Text testText;
    private TextImage testTextImage;

    @BeforeEach
    void setUp() {
        // Text de prueba
        testText = new Text();
        testText.setId(1L);
        testText.setContent("Test text content");

        // TextImage de prueba
        testTextImage = TextImage.builder()
                .id(1L)
                .imageName("test-image.jpg")
                .imagePath("/images/test-image.jpg")
                .text(testText)
                .build();
    }

    // ========== Tests para getAllTextImages() ==========
    @Test
    @DisplayName("getAllTextImages debe retornar lista de DTOs cuando hay imágenes")
    void shouldGetAllTextImages() {
        // Arrange
        List<TextImage> images = Arrays.asList(testTextImage);
        when(textImageRepository.findAll()).thenReturn(images);

        // Act
        List<TextImageDTO> result = textImageService.getAllTextImages();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testTextImage.getId(), result.get(0).getId());
        assertEquals(testTextImage.getImageName(), result.get(0).getImageName());
        verify(textImageRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getAllTextImages debe retornar lista vacía cuando no hay imágenes")
    void shouldReturnEmptyListWhenNoImages() {
        // Arrange
        when(textImageRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<TextImageDTO> result = textImageService.getAllTextImages();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(textImageRepository, times(1)).findAll();
    }

    // ========== Tests para getImagesByTextId() ==========
    @Test
    @DisplayName("getImagesByTextId debe retornar imágenes del texto especificado")
    void shouldGetImagesByTextId() {
        // Arrange
        Long textId = 1L;
        List<TextImage> images = Arrays.asList(testTextImage);
        when(textImageRepository.findByTextId(textId)).thenReturn(images);

        // Act
        List<TextImageDTO> result = textImageService.getImagesByTextId(textId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testText.getId(), result.get(0).getTextId());
        verify(textImageRepository, times(1)).findByTextId(textId);
    }

    @Test
    @DisplayName("getImagesByTextId debe retornar lista vacía cuando texto no tiene imágenes")
    void shouldReturnEmptyListWhenTextHasNoImages() {
        // Arrange
        Long textId = 999L;
        when(textImageRepository.findByTextId(textId)).thenReturn(new ArrayList<>());

        // Act
        List<TextImageDTO> result = textImageService.getImagesByTextId(textId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(textImageRepository, times(1)).findByTextId(textId);
    }

    // ========== Tests para getTextImageById() ==========
    @Test
    @DisplayName("getTextImageById debe retornar DTO cuando imagen existe")
    void shouldGetTextImageById() {
        // Arrange
        Long imageId = 1L;
        when(textImageRepository.findById(imageId)).thenReturn(Optional.of(testTextImage));

        // Act
        TextImageDTO result = textImageService.getTextImageById(imageId);

        // Assert
        assertNotNull(result);
        assertEquals(imageId, result.getId());
        assertEquals(testTextImage.getImageName(), result.getImageName());
        verify(textImageRepository, times(1)).findById(imageId);
    }

    @Test
    @DisplayName("getTextImageById debe lanzar EntityNotFoundException cuando imagen no existe")
    void shouldThrowExceptionWhenImageNotFound() {
        // Arrange
        Long imageId = 999L;
        when(textImageRepository.findById(imageId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            textImageService.getTextImageById(imageId);
        });

        assertTrue(exception.getMessage().contains("TextImage not found with id"));
        verify(textImageRepository, times(1)).findById(imageId);
    }

    // ========== Tests para createTextImage() ==========
    @Test
    @DisplayName("createTextImage debe crear imagen cuando texto existe")
    void shouldCreateTextImage() {
        // Arrange
        TextImageDTO newImageDTO = TextImageDTO.builder()
                .textId(1L)
                .imageName("new-image.png")
                .build();

        when(textItemRepository.findById(1L)).thenReturn(Optional.of(testText));
        when(textImageRepository.save(any(TextImage.class))).thenReturn(testTextImage);

        // Act
        TextImageDTO result = textImageService.createTextImage(newImageDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testTextImage.getId(), result.getId());
        verify(textItemRepository, times(1)).findById(1L);
        verify(textImageRepository, times(1)).save(any(TextImage.class));
    }

    @Test
    @DisplayName("createTextImage debe lanzar EntityNotFoundException cuando texto no existe")
    void shouldThrowExceptionWhenTextNotFoundOnCreate() {
        // Arrange
        TextImageDTO newImageDTO = TextImageDTO.builder()
                .textId(999L)
                .imageName("new-image.png")
                .build();

        when(textItemRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            textImageService.createTextImage(newImageDTO);
        });

        assertTrue(exception.getMessage().contains("Text not found"));
        verify(textItemRepository, times(1)).findById(999L);
        verify(textImageRepository, never()).save(any());
    }

    // ========== Tests para processAndSaveImages() ==========
    @Test
    @DisplayName("processAndSaveImages debe procesar y guardar imágenes válidas")
    void shouldProcessAndSaveValidImages() {
        // Arrange
        Long textId = 1L;
        MultipartFile file1 = new MockMultipartFile("image1", "photo1.jpg", "image/jpeg", "content1".getBytes());
        MultipartFile file2 = new MockMultipartFile("image2", "photo2.png", "image/png", "content2".getBytes());
        MultipartFile[] files = {file1, file2};

        when(textItemRepository.findById(textId)).thenReturn(Optional.of(testText));
        when(textImageRepository.findByTextId(textId)).thenReturn(new ArrayList<>());
        when(imageStorageService.storeImage(any(MultipartFile.class))).thenReturn("/images/stored-image.jpg");
        when(textImageRepository.save(any(TextImage.class))).thenReturn(testTextImage);

        // Act
        List<TextImageDTO> result = textImageService.processAndSaveImages(files, textId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(textItemRepository, times(1)).findById(textId);
        verify(imageStorageService, times(2)).storeImage(any(MultipartFile.class));
        verify(textImageRepository, times(2)).save(any(TextImage.class));
    }

    @Test
    @DisplayName("processAndSaveImages debe retornar lista vacía cuando no hay archivos")
    void shouldReturnEmptyListWhenNoFilesProvided() {
        // Arrange
        Long textId = 1L;
        MultipartFile[] files = null;

        // Act
        List<TextImageDTO> result = textImageService.processAndSaveImages(files, textId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(textImageRepository, never()).save(any());
    }

    @Test
    @DisplayName("processAndSaveImages debe lanzar excepción cuando se excede límite de imágenes")
    void shouldThrowExceptionWhenImageLimitExceeded() {
        // Arrange
        Long textId = 1L;
        MultipartFile[] files = new MultipartFile[11]; // Más de MAX_IMAGES_PER_TEXT (10)
        for (int i = 0; i < 11; i++) {
            files[i] = new MockMultipartFile("image" + i, "photo" + i + ".jpg", "image/jpeg", "content".getBytes());
        }

        when(textItemRepository.findById(textId)).thenReturn(Optional.of(testText));
        when(textImageRepository.findByTextId(textId)).thenReturn(new ArrayList<>());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            textImageService.processAndSaveImages(files, textId);
        });

        assertTrue(exception.getMessage().contains("Máximo"));
        assertTrue(exception.getMessage().contains("10"));
        verify(textItemRepository, times(1)).findById(textId);
        verify(textImageRepository, never()).save(any());
    }

    @Test
    @DisplayName("processAndSaveImages debe omitir archivos vacíos")
    void shouldSkipEmptyFiles() {
        // Arrange
        Long textId = 1L;
        MultipartFile emptyFile = new MockMultipartFile("empty", "", "image/jpeg", new byte[0]);
        MultipartFile validFile = new MockMultipartFile("valid", "photo.jpg", "image/jpeg", "content".getBytes());
        MultipartFile[] files = {emptyFile, validFile};

        when(textItemRepository.findById(textId)).thenReturn(Optional.of(testText));
        when(textImageRepository.findByTextId(textId)).thenReturn(new ArrayList<>());
        when(imageStorageService.storeImage(validFile)).thenReturn("/images/photo.jpg");
        when(textImageRepository.save(any(TextImage.class))).thenReturn(testTextImage);

        // Act
        List<TextImageDTO> result = textImageService.processAndSaveImages(files, textId);

        // Assert
        assertEquals(1, result.size()); // Solo el archivo válido
        verify(imageStorageService, times(1)).storeImage(any(MultipartFile.class));
    }

    // ========== Tests para deleteTextImage() ==========
    @Test
    @DisplayName("deleteTextImage debe eliminar imagen por ID")
    void shouldDeleteTextImage() {
        // Arrange
        Long imageId = 1L;
        doNothing().when(textImageRepository).deleteById(imageId);

        // Act
        textImageService.deleteTextImage(imageId);

        // Assert
        verify(textImageRepository, times(1)).deleteById(imageId);
    }

    // ========== Tests para deleteMultipleTextImages() ==========
    @Test
    @DisplayName("deleteMultipleTextImages debe eliminar múltiples imágenes")
    void shouldDeleteMultipleImages() {
        // Arrange
        List<Long> imageIds = Arrays.asList(1L, 2L, 3L);
        List<TextImage> imagesToDelete = Arrays.asList(testTextImage);

        when(textImageRepository.findAllById(imageIds)).thenReturn(imagesToDelete);
        doNothing().when(textImageRepository).deleteAll(imagesToDelete);

        // Act
        textImageService.deleteMultipleTextImages(imageIds);

        // Assert
        verify(textImageRepository, times(1)).findAllById(imageIds);
        verify(textImageRepository, times(1)).deleteAll(imagesToDelete);
    }

    @Test
    @DisplayName("deleteMultipleTextImages no debe hacer nada cuando lista está vacía")
    void shouldNotDeleteWhenListIsEmpty() {
        // Arrange
        List<Long> emptyList = new ArrayList<>();

        // Act
        textImageService.deleteMultipleTextImages(emptyList);

        // Assert
        verify(textImageRepository, never()).findAllById(any());
        verify(textImageRepository, never()).deleteAll(any());
    }

    @Test
    @DisplayName("deleteMultipleTextImages no debe hacer nada cuando lista es null")
    void shouldNotDeleteWhenListIsNull() {
        // Act
        textImageService.deleteMultipleTextImages(null);

        // Assert
        verify(textImageRepository, never()).findAllById(any());
        verify(textImageRepository, never()).deleteAll(any());
    }

    // ========== Tests para updateTextImage() ==========
    @Test
    @DisplayName("updateTextImage debe actualizar imagen existente")
    void shouldUpdateTextImage() {
        // Arrange
        Long imageId = 1L;
        TextImageDTO updateDTO = TextImageDTO.builder()
                .imageName("updated-name.jpg")
                .build();

        when(textImageRepository.findById(imageId)).thenReturn(Optional.of(testTextImage));
        when(textImageRepository.save(any(TextImage.class))).thenReturn(testTextImage);

        // Act
        TextImageDTO result = textImageService.updateTextImage(imageId, updateDTO);

        // Assert
        assertNotNull(result);
        verify(textImageRepository, times(1)).findById(imageId);
        verify(textImageRepository, times(1)).save(any(TextImage.class));
    }

    @Test
    @DisplayName("updateTextImage debe lanzar EntityNotFoundException cuando imagen no existe")
    void shouldThrowExceptionWhenUpdatingNonExistentImage() {
        // Arrange
        Long imageId = 999L;
        TextImageDTO updateDTO = TextImageDTO.builder().imageName("new-name.jpg").build();

        when(textImageRepository.findById(imageId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            textImageService.updateTextImage(imageId, updateDTO);
        });

        assertTrue(exception.getMessage().contains("TextImage not found"));
        verify(textImageRepository, never()).save(any());
    }

    // ========== Tests para deleteImagesByTextId() ==========
    @Test
    @DisplayName("deleteImagesByTextId debe eliminar todas las imágenes de un texto")
    void shouldDeleteImagesByTextId() {
        // Arrange
        Long textId = 1L;
        List<TextImage> images = Arrays.asList(testTextImage);

        when(textImageRepository.findByTextId(textId)).thenReturn(images);
        doNothing().when(textImageRepository).deleteAll(images);

        // Act
        textImageService.deleteImagesByTextId(textId);

        // Assert
        verify(textImageRepository, times(1)).findByTextId(textId);
        verify(textImageRepository, times(1)).deleteAll(images);
    }

    @Test
    @DisplayName("deleteImagesByTextId no debe hacer nada cuando texto no tiene imágenes")
    void shouldNotDeleteWhenTextHasNoImagesToDelete() {
        // Arrange
        Long textId = 999L;
        when(textImageRepository.findByTextId(textId)).thenReturn(new ArrayList<>());

        // Act
        textImageService.deleteImagesByTextId(textId);

        // Assert
        verify(textImageRepository, times(1)).findByTextId(textId);
        verify(textImageRepository, never()).deleteAll(any());
    }

    // ========== Tests para getTextImagePath() ==========
    @Test
    @DisplayName("getTextImagePath debe retornar path cuando imagen existe")
    void shouldGetTextImagePath() {
        // Arrange
        Long imageId = 1L;
        when(textImageRepository.findById(imageId)).thenReturn(Optional.of(testTextImage));

        // Act
        String result = textImageService.getTextImagePath(imageId);

        // Assert
        assertNotNull(result);
        assertEquals(testTextImage.getImagePath(), result);
        verify(textImageRepository, times(1)).findById(imageId);
    }

    @Test
    @DisplayName("getTextImagePath debe lanzar EntityNotFoundException cuando imagen no existe")
    void shouldThrowExceptionWhenGettingPathOfNonExistentImage() {
        // Arrange
        Long imageId = 999L;
        when(textImageRepository.findById(imageId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            textImageService.getTextImagePath(imageId);
        });

        assertTrue(exception.getMessage().contains("TextImage not found"));
        verify(textImageRepository, times(1)).findById(imageId);
    }

    // ========== Tests para isValidImageFile() ==========
    @Test
    @DisplayName("isValidImageFile debe retornar true para archivo JPEG válido")
    void shouldValidateJpegFile() {
        // Arrange
        MultipartFile file = new MockMultipartFile("image", "photo.jpg", "image/jpeg", new byte[1024]);

        // Act
        boolean result = textImageService.isValidImageFile(file);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("isValidImageFile debe retornar true para archivo PNG válido")
    void shouldValidatePngFile() {
        // Arrange
        MultipartFile file = new MockMultipartFile("image", "photo.png", "image/png", new byte[1024]);

        // Act
        boolean result = textImageService.isValidImageFile(file);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("isValidImageFile debe retornar false para archivo null")
    void shouldRejectNullFile() {
        // Act
        boolean result = textImageService.isValidImageFile(null);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("isValidImageFile debe retornar false para archivo vacío")
    void shouldRejectEmptyFile() {
        // Arrange
        MultipartFile file = new MockMultipartFile("image", "", "image/jpeg", new byte[0]);

        // Act
        boolean result = textImageService.isValidImageFile(file);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("isValidImageFile debe retornar false para archivo demasiado grande")
    void shouldRejectOversizedFile() {
        // Arrange - Archivo de 6MB (mayor a MAX_FILE_SIZE de 5MB)
        byte[] largeContent = new byte[6 * 1024 * 1024];
        MultipartFile file = new MockMultipartFile("image", "large.jpg", "image/jpeg", largeContent);

        // Act
        boolean result = textImageService.isValidImageFile(file);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("isValidImageFile debe retornar false para tipo MIME no permitido")
    void shouldRejectInvalidMimeType() {
        // Arrange
        MultipartFile file = new MockMultipartFile("file", "document.pdf", "application/pdf", new byte[1024]);

        // Act
        boolean result = textImageService.isValidImageFile(file);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("isValidImageFile debe aceptar todos los tipos MIME permitidos")
    void shouldAcceptAllAllowedMimeTypes() {
        // Arrange
        String[] allowedTypes = {"image/jpeg", "image/png", "image/gif", "image/bmp", "image/webp"};

        for (String mimeType : allowedTypes) {
            MultipartFile file = new MockMultipartFile("image", "test.jpg", mimeType, new byte[1024]);

            // Act
            boolean result = textImageService.isValidImageFile(file);

            // Assert
            assertTrue(result, "Debe aceptar tipo MIME: " + mimeType);
        }
    }

    // ========== Tests para getTextImageData() ==========
    @Test
    @DisplayName("getTextImageData debe retornar datos de imagen cuando existe")
    void shouldGetTextImageData() {
        // Arrange
        Long imageId = 1L;
        byte[] imageData = "image binary data".getBytes();

        when(textImageRepository.findById(imageId)).thenReturn(Optional.of(testTextImage));
        when(imageStorageService.loadImage(testTextImage.getImagePath())).thenReturn(imageData);

        // Act
        byte[] result = textImageService.getTextImageData(imageId);

        // Assert
        assertNotNull(result);
        assertArrayEquals(imageData, result);
        verify(textImageRepository, times(1)).findById(imageId);
        verify(imageStorageService, times(1)).loadImage(testTextImage.getImagePath());
    }

    @Test
    @DisplayName("getTextImageData debe lanzar EntityNotFoundException cuando imagen no existe")
    void shouldThrowExceptionWhenGettingDataOfNonExistentImage() {
        // Arrange
        Long imageId = 999L;
        when(textImageRepository.findById(imageId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            textImageService.getTextImageData(imageId);
        });

        assertTrue(exception.getMessage().contains("TextImage not found"));
        verify(textImageRepository, times(1)).findById(imageId);
        verify(imageStorageService, never()).loadImage(anyString());
    }
}
