package de.stella.agora_web.tags.service;

import java.util.List;

import de.stella.agora_web.events.model.Event;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.tags.dto.EventSummaryDTO;
import de.stella.agora_web.tags.dto.PostSummaryDTO;
import de.stella.agora_web.tags.dto.TagSummaryDTO;
import de.stella.agora_web.tags.model.Tag;

public interface ITagService {

    /**
     * Crea múltiples tags a partir de un TagListDTO.
     *
     * @param tagListDTO lista de tags a crear
     * @return lista de tags creados
     */
    List<Tag> createTagsBulk(de.stella.agora_web.tags.dto.TagListDTO tagListDTO);

    List<Tag> getAllTags();

    Tag getTagById(Long id);

    Tag getTagByName(String name);

    Tag getOrCreateTagByName(String name);

    List<String> extractHashtags(String text);

    void addTagToPost(Long postId, String tagName);

    void removeTagFromPost(Long postId, String tagName);

    // Métodos para gestionar tags de eventos
    void addTagToEvent(Long eventId, String tagName);

    void removeTagFromEvent(Long eventId, String tagName);

    void addTagToEvent(Long eventId, Long tagId);

    void removeTagFromEvent(Long eventId, Long tagId);

    Tag createTag(String tagName);

    List<Post> getPostsByTagName(String tagName);

    List<Event> getEventsByTagName(String tagName);

    public List<PostSummaryDTO> getPostsSummaryByTagName(String tagName);

    List<EventSummaryDTO> getEventsSummaryByTagName(String tagName);

    List<TagSummaryDTO> getAllTagsSummary();
}
