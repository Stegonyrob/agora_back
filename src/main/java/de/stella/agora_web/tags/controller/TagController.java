package de.stella.agora_web.tags.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.tags.model.Tag;
import de.stella.agora_web.tags.service.ITagService;

@RestController
@RequestMapping("${api-endpoint}/any/tags")
public class TagController {

    @Autowired
    private ITagService tagService;

    // ========== ENDPOINTS DE LECTURA PRIVADOS ==========
    // Obtener posts por tag - REQUIERE AUTENTICACIÓN (posts son privados)
    @GetMapping("/posts/{tagName}")
    public ResponseEntity<List<Post>> getPostsByTagName(@PathVariable String tagName) {
        List<Post> posts = tagService.getPostsByTagName(tagName);
        return ResponseEntity.ok(posts);
    }

    // ========== ENDPOINTS ADMINISTRATIVOS ==========
    // Crear nuevo tag - SOLO ADMIN
    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag) {
        Tag createdTag = tagService.createTag(tag.getName());
        return ResponseEntity.ok(createdTag);
    }

    // ========== ENDPOINTS PARA AGREGAR TAGS ==========
    // Agregar tag a evento
    @PostMapping("/events/{eventId}/tags/{tagName}")
    public ResponseEntity<String> addTagToEvent(@PathVariable Long eventId, @PathVariable String tagName) {
        tagService.addTagToEvent(eventId, tagName);
        return ResponseEntity.ok("Tag '" + tagName + "' agregado al evento " + eventId);
    }

    // Agregar tag a post
    @PostMapping("/posts/{postId}/tags/{tagName}")
    public ResponseEntity<String> addTagToPost(@PathVariable Long postId, @PathVariable String tagName) {
        tagService.addTagToPost(postId, tagName);
        return ResponseEntity.ok("Tag '" + tagName + "' agregado al post " + postId);
    }

    // ========== ENDPOINTS PARA QUITAR TAGS ==========
    // Quitar tag de evento
    @DeleteMapping("/events/{eventId}/tags/{tagName}")
    public ResponseEntity<String> removeTagFromEvent(@PathVariable Long eventId, @PathVariable String tagName) {
        tagService.removeTagFromEvent(eventId, tagName);
        return ResponseEntity.ok("Tag '" + tagName + "' eliminado del evento " + eventId);
    }

    // Quitar tag de post
    @DeleteMapping("/posts/{postId}/tags/{tagName}")
    public ResponseEntity<String> removeTagFromPost(@PathVariable Long postId, @PathVariable String tagName) {
        tagService.removeTagFromPost(postId, tagName);
        return ResponseEntity.ok("Tag '" + tagName + "' eliminado del post " + postId);
    }
}
