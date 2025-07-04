package de.stella.agora_web.tags.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.events.model.Event;
import de.stella.agora_web.tags.model.Tag;
import de.stella.agora_web.tags.service.ITagService;

@RestController
@RequestMapping("${api-endpoint}/all/tags")
public class PublicTagController {

    @Autowired
    private ITagService tagService;

    // ========== ENDPOINTS PÚBLICOS DE LECTURA ==========
    // Obtener todos los tags - PÚBLICO
    @GetMapping
    public ResponseEntity<List<Tag>> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    // Obtener eventos por tag - PÚBLICO (para eventos públicos)
    @GetMapping("/events/{tagName}")
    public ResponseEntity<List<Event>> getEventsByTagName(@PathVariable String tagName) {
        List<Event> events = tagService.getEventsByTagName(tagName);
        return ResponseEntity.ok(events);
    }
}
