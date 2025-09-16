package de.stella.agora_web.texts.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.image.service.ITextImageService;
import de.stella.agora_web.texts.controller.dto.TextItemDTO;
import de.stella.agora_web.texts.service.ITextItemService;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173", "http://localhost:3000"})
@RequestMapping(path = "${api-endpoint}/all/texts")

public class TextItemController {

    @Autowired
    private ITextItemService textItemService;

    @Autowired
    private ITextImageService textImageService;

    private static final Logger LOGGER = LoggerFactory.getLogger(TextItemController.class);

    /**
     * Obtiene todos los textos (público).
     */
    @GetMapping
    public List<TextItemDTO> getAllTexts() {
        LOGGER.info("Retrieving all texts");
        List<TextItemDTO> allTexts = textItemService.getAllTexts();
        LOGGER.info("Retrieved {} texts", allTexts.size());
        return allTexts;
    }

    /**
     * Obtiene un texto por ID (público).
     */
    @GetMapping("/{id}")
    public ResponseEntity<TextItemDTO> getTextById(@PathVariable Long id) {
        try {
            TextItemDTO dto = textItemService.getTextById(id);
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
    public ResponseEntity<TextItemDTO> createText(@Valid @RequestBody TextItemDTO textItemDTO) {
        try {
            TextItemDTO created = textItemService.createText(textItemDTO);
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
    public ResponseEntity<TextItemDTO> updateText(@PathVariable Long id, @Valid @RequestBody TextItemDTO dto) {
        try {
            TextItemDTO updated = textItemService.updateText(id, dto);
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
}
