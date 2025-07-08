package de.stella.agora_web.tags.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.tags.dto.EventSummaryDTO;
import de.stella.agora_web.tags.dto.TagSummaryDTO;
import de.stella.agora_web.tags.service.ITagService;

@RestController
@RequestMapping("${api-endpoint}/all/tags")
public class PublicTagController {

    @Autowired
    private ITagService tagService;

    // ========== ENDPOINTS PÚBLICOS DE LECTURA ==========
    // Obtener todos los tags - PÚBLICO (OPTIMIZADO)
    @GetMapping
    public ResponseEntity<List<TagSummaryDTO>> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTagsSummary());
    }

    // Obtener eventos por tag - PÚBLICO (OPTIMIZADO)
    @GetMapping("/events/{tagName}")
    public ResponseEntity<List<EventSummaryDTO>> getEventsByTagName(@PathVariable String tagName) {
        List<EventSummaryDTO> events = tagService.getEventsSummaryByTagName(tagName);
        return ResponseEntity.ok(events);
    }
}
