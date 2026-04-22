package de.stella.agora_web.texts.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.image.service.ITextImageService;
import de.stella.agora_web.texts.controller.dto.TextDTO;
import de.stella.agora_web.texts.service.ITextService;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173", "http://localhost:3000"})
@RequestMapping(path = "${api-endpoint}/all/texts")

public class TextController {

    private final ITextService textItemService;
    @SuppressWarnings("unused")
    private final ITextImageService textImageService;

    private static final Logger LOGGER = LoggerFactory.getLogger(TextController.class);

    public TextController(ITextService textItemService, ITextImageService textImageService) {
        this.textItemService = textItemService;
        this.textImageService = textImageService;
    }

    /**
     * Obtiene todos los textos (público).
     */
    @GetMapping
    public List<TextDTO> getAllTexts() {
        LOGGER.info("Retrieving all texts");
        List<TextDTO> allTexts = textItemService.getAllTexts();
        LOGGER.info("Retrieved {} texts", allTexts.size());
        return allTexts;
    }

    /**
     * Obtiene textos filtrados por categoría (público). Categorías disponibles:
     * agora, services, team, neurodiversity, cea, tda_tdh,
     * learning_difficulties, development_conditions, communication, faq
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<TextDTO>> getTextsByCategory(@PathVariable String category) {
        LOGGER.info("Retrieving texts for category '{}'", category);
        List<TextDTO> texts = textItemService.getTextsByCategory(category);
        return ResponseEntity.ok(texts);
    }

    /**
     * Obtiene un texto por ID (público).
     */
    @GetMapping("/{id}")
    public ResponseEntity<TextDTO> getTextById(@PathVariable Long id) {
        try {
            TextDTO dto = textItemService.getTextById(id);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            LOGGER.warn("Text not found with id {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            LOGGER.error("Error retrieving text with id {}: {}", id, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Crea un nuevo texto (solo ADMIN).
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TextDTO> createText(@Valid @RequestBody TextDTO textItemDTO) {
        try {
            TextDTO created = textItemService.createText(textItemDTO);
            LOGGER.info("Text created with id {}", created.getId());
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            LOGGER.error("Error creating text: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Actualiza un texto existente (solo ADMIN).
     *
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TextDTO> updateText(@PathVariable Long id, @Valid @RequestBody TextDTO dto) {
        try {
            TextDTO updated = textItemService.updateText(id, dto);
            LOGGER.info("Text updated with id {}", updated.getId());
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            LOGGER.warn("Text not found for update with id {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            LOGGER.error("Error updating text with id {}: {}", id, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Elimina un texto (solo ADMIN).
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteText(@PathVariable Long id) {
        try {
            textItemService.deleteText(id);
            LOGGER.info("Text deleted with id {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            LOGGER.warn("Text not found for delete with id {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            LOGGER.error("Error deleting text with id {}: {}", id, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Archiva o desarchivar un texto (solo ADMIN).
     *
     * @param id ID del texto
     * @param archive true para archivar, false para desarchivar
     * @return ResponseEntity vacío
     */
    @PatchMapping("/{id}/archive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> archiveText(@PathVariable Long id, @RequestParam boolean archive) {
        try {
            if (archive) {
                textItemService.archiveText(id);
                LOGGER.info("Text archived with id {}", id);
            } else {
                textItemService.unArchiveText(id);
                LOGGER.info("Text unarchived with id {}", id);
            }
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            LOGGER.warn("Text not found for archive operation with id {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            LOGGER.error("Error {} text with id {}: {}",
                    archive ? "archiving" : "unarchiving", id, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
