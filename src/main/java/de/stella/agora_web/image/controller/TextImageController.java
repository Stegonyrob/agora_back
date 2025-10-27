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

import de.stella.agora_web.image.controller.dto.TextImageDTO;
import de.stella.agora_web.image.service.ITextImageService;
import lombok.RequiredArgsConstructor;

/**
 * Controlador unificado para imágenes de texto siguiendo principios SOLID SRP:
 * Responsabilidad única de manejar operaciones de imágenes de texto OCP:
 * Abierto para extensión, cerrado para modificación LSP: Sustituible por
 * cualquier implementación de controlador de imágenes ISP: Interfaces
 * segregadas por tipo de operación (público vs admin) DIP: Depende de
 * abstracción ITextImageService
 */
@RestController
@RequestMapping("${api-endpoint}/text-images")
@RequiredArgsConstructor
public class TextImageController {

    private final ITextImageService textImageService;

    // ========== ENDPOINTS PÚBLICOS (Textos son públicos) ==========
    /**
     * Obtiene todas las imágenes de texto - PÚBLICO SRP: Responsabilidad única
     * de listar todas las imágenes
     */
    @GetMapping
    public ResponseEntity<List<TextImageDTO>> getAllTextImages() {
        return ResponseEntity.ok(textImageService.getAllTextImages());
    }

    /**
     * Obtiene información de una imagen específica - PÚBLICO SRP:
     * Responsabilidad única de obtener metadata de imagen
     */
    @GetMapping("/image/{id}")
    public ResponseEntity<TextImageDTO> getTextImage(@PathVariable Long id) {
        return ResponseEntity.ok(textImageService.getTextImageById(id));
    }

    /**
     * Obtiene todas las imágenes asociadas a un texto específico - PÚBLICO SRP:
     * Responsabilidad única de obtener imágenes por textId
     */
    @GetMapping("/{textId}")
    public ResponseEntity<List<TextImageDTO>> getImagesByTextId(@PathVariable Long textId) {
        return ResponseEntity.ok(textImageService.getImagesByTextId(textId));
    }

    // ========== ENDPOINTS ADMINISTRATIVOS ==========
    /**
     * Crea una imagen para un texto - SOLO ADMIN SRP: Responsabilidad única de
     * crear imagen ISP: Interfaz segregada para operaciones administrativas
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TextImageDTO> createTextImage(@RequestBody TextImageDTO dto) {
        return ResponseEntity.ok(textImageService.createTextImage(dto));
    }

    /**
     * Sube múltiples imágenes desde archivos - SOLO ADMIN SRP: Responsabilidad
     * única de procesar múltiples archivos DIP: Depende de abstracción
     * ITextImageService
     */
    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TextImageDTO>> uploadMultipleTextImages(
            @RequestParam MultipartFile[] files,
            @RequestParam Long textId) {

        try {
            List<TextImageDTO> savedImages = textImageService.processAndSaveImages(files, textId);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedImages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Elimina una imagen específica - SOLO ADMIN SRP: Responsabilidad única de
     * eliminar imagen
     */
    @DeleteMapping("/image/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTextImage(@PathVariable Long id) {
        textImageService.deleteTextImage(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Elimina múltiples imágenes - SOLO ADMIN SRP: Responsabilidad única de
     * eliminar múltiples imágenes
     */
    @DeleteMapping("/delete-multiple")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMultipleTextImages(@RequestBody List<Long> imageIds) {
        try {
            textImageService.deleteMultipleTextImages(imageIds);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Elimina todas las imágenes de un texto - SOLO ADMIN SRP: Responsabilidad
     * única de limpiar imágenes por texto
     */
    @DeleteMapping("/{textId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteImagesByText(@PathVariable Long textId) {
        textImageService.deleteImagesByTextId(textId);
        return ResponseEntity.noContent().build();
    }
}
