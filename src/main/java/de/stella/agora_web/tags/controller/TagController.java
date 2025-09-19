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

import de.stella.agora_web.tags.dto.PostSummaryDTO;
import de.stella.agora_web.tags.dto.TagSummaryDTO;
import de.stella.agora_web.tags.model.Tag;
import de.stella.agora_web.tags.service.ITagService;

@RestController
@RequestMapping("${api-endpoint}/any/tags")
public class TagController {

    // Asociar múltiples tags a un post - ACEPTA AMBOS FORMATOS
    @PostMapping("/posts/{postId}/tags")
    public ResponseEntity<String> addTagsToPost(@PathVariable Long postId, @RequestBody Object requestBody) {
        try {
            // Intentar como array simple de strings
            if (requestBody instanceof List<?>) {
                @SuppressWarnings("unchecked")
                List<String> tagNames = (List<String>) requestBody;
                if (tagNames.isEmpty()) {
                    return ResponseEntity.badRequest().body("No se recibieron tags para asociar");
                }
                tagNames.forEach(tagName -> tagService.addTagToPost(postId, tagName));
                return ResponseEntity.ok("Tags asociados correctamente al post " + postId);
            }

            // Intentar como TagListDTO
            if (requestBody instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> map = (java.util.Map<String, Object>) requestBody;
                Object tagsObj = map.get("tags");
                if (tagsObj instanceof List<?>) {
                    @SuppressWarnings("unchecked")
                    List<Object> tagsList = (List<Object>) tagsObj;
                    if (tagsList.isEmpty()) {
                        return ResponseEntity.badRequest().body("No se recibieron tags para asociar");
                    }

                    tagsList.forEach(tagObj -> {
                        if (tagObj instanceof String) {
                            tagService.addTagToPost(postId, (String) tagObj);
                        } else if (tagObj instanceof java.util.Map) {
                            @SuppressWarnings("unchecked")
                            java.util.Map<String, Object> tagMap = (java.util.Map<String, Object>) tagObj;
                            String tagName = (String) tagMap.get("name");
                            if (tagName != null) {
                                tagService.addTagToPost(postId, tagName);
                            }
                        }
                    });
                    return ResponseEntity.ok("Tags asociados correctamente al post " + postId);
                }
            }

            return ResponseEntity.badRequest().body("Formato de datos no válido");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error procesando tags: " + e.getMessage());
        }
    }

    // Eliminar TODAS las tags de un post
    @DeleteMapping("/posts/{postId}/tags")
    public ResponseEntity<String> deleteAllTagsFromPost(@PathVariable Long postId) {
        try {
            tagService.removeAllTagsFromPost(postId);
            return ResponseEntity.ok("Todas las tags eliminadas del post " + postId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error eliminando tags: " + e.getMessage());
        }
    }

    // Eliminar TODAS las tags de un evento
    @DeleteMapping("/events/{eventId}/tags")
    public ResponseEntity<String> deleteAllTagsFromEvent(@PathVariable Long eventId) {
        try {
            tagService.removeAllTagsFromEvent(eventId);
            return ResponseEntity.ok("Todas las tags eliminadas del evento " + eventId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error eliminando tags: " + e.getMessage());
        }
    }

    // Asociar múltiples tags a un evento
    @PostMapping("/events/{eventId}/tags")
    public ResponseEntity<String> addTagsToEvent(@PathVariable Long eventId, @RequestBody de.stella.agora_web.tags.dto.TagListDTO tagListDTO) {
        if (tagListDTO.getTags() == null || tagListDTO.getTags().isEmpty()) {
            return ResponseEntity.badRequest().body("No se recibieron tags para asociar");
        }
        tagListDTO.getTags().forEach(tagDto -> tagService.addTagToEvent(eventId, tagDto.getName()));
        return ResponseEntity.ok("Tags asociados correctamente al evento " + eventId);
    }

    @Autowired
    private ITagService tagService;

    // ========== ENDPOINTS DE LECTURA PRIVADOS ==========
    // Obtener posts por tag - REQUIERE AUTENTICACIÓN (OPTIMIZADO)
    @GetMapping("/posts/{tagName}")
    public ResponseEntity<List<PostSummaryDTO>> getPostsByTagName(@PathVariable String tagName) {
        List<PostSummaryDTO> posts = tagService.getPostsSummaryByTagName(tagName);
        return ResponseEntity.ok(posts);
    }

    // Obtener tags de un post específico - REQUERIDO POR FRONTEND
    @GetMapping("/posts/{postId}/tags")
    public ResponseEntity<List<TagSummaryDTO>> getTagsByPost(@PathVariable Long postId) {
        List<TagSummaryDTO> tags = tagService.getTagsByPostId(postId);
        return ResponseEntity.ok(tags);
    }

    // Obtener tags de un evento específico - CONSISTENCIA
    @GetMapping("/events/{eventId}/tags")
    public ResponseEntity<List<TagSummaryDTO>> getTagsByEvent(@PathVariable Long eventId) {
        List<TagSummaryDTO> tags = tagService.getTagsByEventId(eventId);
        return ResponseEntity.ok(tags);
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
