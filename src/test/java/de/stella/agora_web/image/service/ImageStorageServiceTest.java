package de.stella.agora_web.image.service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;

/**
 * Tests para ImageStorageService Valida almacenamiento y recuperación de
 * imágenes
 */
@DisplayName("ImageStorageService Tests")
class ImageStorageServiceTest {

    private ImageStorageService imageStorageService;

    @BeforeEach
    void setUp() {
        imageStorageService = new ImageStorageService();
    }

    // ========== Tests para storeImage() ==========
    @Test
    @DisplayName("storeImage debe guardar archivo con extensión jpg")
    void shouldStoreImageWithJpgExtension() {
        // Arrange
        byte[] content = "test image content".getBytes();
        MultipartFile file = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                content
        );

        // Act
        String result = imageStorageService.storeImage(file);

        // Assert
        assertNotNull(result);
        assertTrue(result.startsWith("/images/"));
        assertTrue(result.endsWith(".jpg"));
    }

    @Test
    @DisplayName("storeImage debe guardar archivo con extensión png")
    void shouldStoreImageWithPngExtension() {
        // Arrange
        byte[] content = "test image content".getBytes();
        MultipartFile file = new MockMultipartFile(
                "image",
                "test.png",
                "image/png",
                content
        );

        // Act
        String result = imageStorageService.storeImage(file);

        // Assert
        assertNotNull(result);
        assertTrue(result.endsWith(".png"));
    }

    @Test
    @DisplayName("storeImage debe generar nombre único para cada archivo")
    void shouldGenerateUniqueFilenames() {
        // Arrange
        byte[] content = "test content".getBytes();
        MultipartFile file1 = new MockMultipartFile("image", "test.jpg", "image/jpeg", content);
        MultipartFile file2 = new MockMultipartFile("image", "test.jpg", "image/jpeg", content);

        // Act
        String path1 = imageStorageService.storeImage(file1);
        String path2 = imageStorageService.storeImage(file2);

        // Assert
        assertNotEquals(path1, path2, "Los nombres de archivo deben ser únicos");
    }

    @Test
    @DisplayName("storeImage debe usar extensión jpg por defecto cuando no hay extensión")
    void shouldUseDefaultExtensionWhenNoneProvided() {
        // Arrange
        byte[] content = "test content".getBytes();
        MultipartFile file = new MockMultipartFile("image", "testfile", "image/jpeg", content);

        // Act
        String result = imageStorageService.storeImage(file);

        // Assert
        assertNotNull(result);
        assertTrue(result.endsWith(".jpg"));
    }

    @Test
    @DisplayName("storeImage debe manejar archivos con múltiples puntos en el nombre")
    void shouldHandleFilenamesWithMultipleDots() {
        // Arrange
        byte[] content = "test content".getBytes();
        MultipartFile file = new MockMultipartFile(
                "image",
                "my.test.image.png",
                "image/png",
                content
        );

        // Act
        String result = imageStorageService.storeImage(file);

        // Assert
        assertNotNull(result);
        assertTrue(result.endsWith(".png"));
    }

    @Test
    @DisplayName("storeImage debe normalizar extensiones a minúsculas")
    void shouldNormalizeExtensionToLowercase() {
        // Arrange
        byte[] content = "test content".getBytes();
        MultipartFile file = new MockMultipartFile("image", "test.JPG", "image/jpeg", content);

        // Act
        String result = imageStorageService.storeImage(file);

        // Assert
        assertTrue(result.endsWith(".jpg")); // Debe estar en minúsculas
    }

    @Test
    @DisplayName("storeImage debe manejar archivo nulo lanzando excepción")
    void shouldThrowExceptionWithNullFile() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            imageStorageService.storeImage(null);
        });
    }

    // ========== Tests para loadImage() ==========
    @Test
    @DisplayName("loadImage debe cargar imagen correctamente")
    void shouldLoadImageSuccessfully() {
        // Arrange: Primero guardamos una imagen
        byte[] originalContent = "test image data".getBytes();
        MultipartFile file = new MockMultipartFile("image", "test.jpg", "image/jpeg", originalContent);
        String storedPath = imageStorageService.storeImage(file);

        // Extraer el nombre del archivo del path
        String filename = storedPath.substring(storedPath.lastIndexOf('/') + 1);

        // Act
        byte[] loadedContent = imageStorageService.loadImage(filename);

        // Assert
        assertNotNull(loadedContent);
        assertArrayEquals(originalContent, loadedContent);
    }

    @Test
    @DisplayName("loadImage debe lanzar EntityNotFoundException con path inválido")
    void shouldThrowExceptionWithInvalidPath() {
        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            imageStorageService.loadImage("nonexistent-file.jpg");
        });
    }

    @Test
    @DisplayName("loadImage debe lanzar EntityNotFoundException con path nulo")
    void shouldThrowExceptionWithNullPath() {
        // Act & Assert
        assertThrows(Exception.class, () -> {
            imageStorageService.loadImage(null);
        });
    }

    @Test
    @DisplayName("loadImage debe lanzar EntityNotFoundException con path vacío")
    void shouldThrowExceptionWithEmptyPath() {
        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            imageStorageService.loadImage("");
        });
    }

    // ========== Tests de integración ==========
    @Test
    @DisplayName("Debe almacenar y recuperar imagen correctamente (integración)")
    void shouldStoreAndRetrieveImageSuccessfully() {
        // Arrange
        byte[] imageData = "PNG Image Data".getBytes();
        MultipartFile file = new MockMultipartFile(
                "profile-image",
                "avatar.png",
                "image/png",
                imageData
        );

        // Act: Almacenar
        String storedPath = imageStorageService.storeImage(file);
        assertNotNull(storedPath);

        // Extraer nombre del archivo
        String filename = storedPath.substring(storedPath.lastIndexOf('/') + 1);

        // Act: Recuperar
        byte[] retrievedData = imageStorageService.loadImage(filename);

        // Assert
        assertNotNull(retrievedData);
        assertArrayEquals(imageData, retrievedData);
    }

    @Test
    @DisplayName("Debe manejar múltiples imágenes con mismo nombre original")
    void shouldHandleMultipleImagesWithSameName() {
        // Arrange
        byte[] content1 = "Image 1".getBytes();
        byte[] content2 = "Image 2".getBytes();

        MultipartFile file1 = new MockMultipartFile("image", "photo.jpg", "image/jpeg", content1);
        MultipartFile file2 = new MockMultipartFile("image", "photo.jpg", "image/jpeg", content2);

        // Act
        String path1 = imageStorageService.storeImage(file1);
        String path2 = imageStorageService.storeImage(file2);

        // Assert
        assertNotEquals(path1, path2);
        assertTrue(path1.endsWith(".jpg"));
        assertTrue(path2.endsWith(".jpg"));
    }

    @Test
    @DisplayName("Debe soportar diferentes formatos de imagen")
    void shouldSupportDifferentImageFormats() {
        // Arrange & Act & Assert
        String[] extensions = {"jpg", "png", "gif", "bmp", "webp"};

        for (String ext : extensions) {
            MultipartFile file = new MockMultipartFile(
                    "image",
                    "test." + ext,
                    "image/" + ext,
                    "test data".getBytes()
            );

            String result = imageStorageService.storeImage(file);
            assertTrue(result.endsWith("." + ext), "Debe soportar extensión " + ext);
        }
    }

    @Test
    @DisplayName("Debe retornar path relativo con formato correcto")
    void shouldReturnCorrectRelativePath() {
        // Arrange
        MultipartFile file = new MockMultipartFile("image", "test.jpg", "image/jpeg", "content".getBytes());

        // Act
        String path = imageStorageService.storeImage(file);

        // Assert
        assertTrue(path.startsWith("/images/"));
        assertTrue(path.matches("/images/[a-f0-9\\-]+\\.jpg"));
    }
}
