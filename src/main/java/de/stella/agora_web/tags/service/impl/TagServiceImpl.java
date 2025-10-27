package de.stella.agora_web.tags.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import de.stella.agora_web.comment.repository.CommentRepository;
import de.stella.agora_web.events.model.Event;
import de.stella.agora_web.events.repository.EventRepository;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.posts.repository.PostRepository;
import de.stella.agora_web.replies.repository.ReplyRepository;
import de.stella.agora_web.tags.dto.EventSummaryDTO;
import de.stella.agora_web.tags.dto.PostSummaryDTO;
import de.stella.agora_web.tags.dto.TagSummaryDTO;
import de.stella.agora_web.tags.model.Tag;
import de.stella.agora_web.tags.repository.TagRepository;
import de.stella.agora_web.tags.service.ITagService;
import jakarta.persistence.EntityNotFoundException;

@Service
@SuppressWarnings("unused")
public class TagServiceImpl implements ITagService {

    @Override
    public List<Tag> createTagsBulk(de.stella.agora_web.tags.dto.TagListDTO tagListDTO) {
        List<Tag> createdTags = new ArrayList<>();
        if (tagListDTO == null || tagListDTO.getTags() == null) {
            return createdTags;
        }
        for (de.stella.agora_web.tags.dto.TagSummaryDTO tagDto : tagListDTO.getTags()) {
            if (tagDto.getName() != null && !tagDto.getName().isBlank()) {
                Tag tag = getOrCreateTagByName(tagDto.getName());
                createdTags.add(tag);
            }
        }
        return createdTags;
    }

    private TagRepository tagRepository;

    private PostRepository postRepository;

    private EventRepository eventRepository;

    private ReplyRepository replyRepository;

    private CommentRepository commentRepository;

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public Tag getTagById(Long id) {
        return tagRepository.findById(id).orElse(null);
    }

    @Override
    public Tag getOrCreateTagByName(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        List<Tag> tags = tagRepository.findByName(name);
        if (!tags.isEmpty()) {
            return tags.get(0);
        }
        Tag tag = new Tag();
        tag.setName(name);
        return tagRepository.save(tag);
    }

    @Override
    public Tag getTagByName(String name) {
        List<Tag> tags = tagRepository.findByName(name);
        return tags.isEmpty() ? null : tags.get(0);
    }

    @Override
    public List<String> extractHashtags(String text) {
        List<String> hashtags = new ArrayList<>();
        if (text == null) {
            return hashtags;
        }
        Pattern pattern = Pattern.compile("#(\\w+)");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            hashtags.add(matcher.group(1));
        }
        return hashtags;
    }

    // Métodos genéricos para asociar tags a entidades
    @Override
    public void addTagToPost(Long postId, String tagName) {
        Post post = postRepository.findById(postId).orElse(null);
        Tag tag = getOrCreateTagByName(tagName);
        if (post != null && tag != null) {
            post.getTags().add(tag);
            postRepository.save(post);
        }
    }

    @Override
    public void removeTagFromPost(Long postId, String tagName) {
        Post post = postRepository.findById(postId).orElse(null);
        Tag tag = getTagByName(tagName);
        if (post != null && tag != null) {
            post.getTags().remove(tag);
            postRepository.save(post);
        }
    }

    @Override
    public void removeAllTagsFromPost(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post != null) {
            post.getTags().clear();
            postRepository.save(post);
        }
    }

    @Override
    public Tag createTag(String name) {
        Tag tag = new Tag();
        tag.setName(name);
        return tagRepository.save(tag);
    }

    public void addTagToPost(Long postId, Long tagId) {
        Post post = postRepository.findById(postId).orElse(null);
        Tag tag = tagRepository.findById(tagId).orElse(null);
        if (post != null && tag != null) {
            post.getTags().add(tag);
            postRepository.save(post);
        }
    }

    public void removeTagFromPost(Long postId, Long tagId) {
        Post post = postRepository.findById(postId).orElse(null);
        Tag tag = tagRepository.findById(tagId).orElse(null);
        if (post != null && tag != null) {
            post.getTags().remove(tag);
            postRepository.save(post);
        }
    }

    // ========== NUEVOS MÉTODOS PARA EVENTOS ==========
    @Override
    public void addTagToEvent(Long eventId, String tagName) {
        Event event = eventRepository.findById(eventId).orElse(null);
        Tag tag = getOrCreateTagByName(tagName);
        if (event != null && tag != null) {
            event.getTags().add(tag);
            eventRepository.save(event);
        }
    }

    @Override
    public void removeTagFromEvent(Long eventId, String tagName) {
        Event event = eventRepository.findById(eventId).orElse(null);
        Tag tag = getTagByName(tagName);
        if (event != null && tag != null) {
            event.getTags().remove(tag);
            eventRepository.save(event);
        }
    }

    @Override
    public void addTagToEvent(Long eventId, Long tagId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        Tag tag = tagRepository.findById(tagId).orElse(null);
        if (event != null && tag != null) {
            event.getTags().add(tag);
            eventRepository.save(event);
        }
    }

    @Override
    public void removeTagFromEvent(Long eventId, Long tagId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        Tag tag = tagRepository.findById(tagId).orElse(null);
        if (event != null && tag != null) {
            event.getTags().remove(tag);
            eventRepository.save(event);
        }
    }

    @Override
    public void removeAllTagsFromEvent(Long eventId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event != null) {
            event.getTags().clear();
            eventRepository.save(event);
        }
    }

    // Métodos que necesita la interfaz actualizada
    @Override
    public List<Event> getEventsByTagName(String tagName) {
        return eventRepository.findByTagsName(tagName);
    }

    @Override
    public List<Post> getPostsByTagName(String tagName) {
        return postRepository.findByTagsName(tagName);
    }

    // ========== MÉTODOS DE CONVERSIÓN A DTO ==========
    private TagSummaryDTO convertToTagSummaryDTO(Tag tag) {
        boolean archived = tag.getArchived() != null && tag.getArchived();
        return new TagSummaryDTO(tag.getId(), tag.getName(), archived);
    }

    private EventSummaryDTO convertToEventSummaryDTO(Event event) {
        List<TagSummaryDTO> tagDTOs = event.getTags().stream()
                .map(this::convertToTagSummaryDTO)
                .toList();

        return new EventSummaryDTO(
                event.getId(),
                event.getTitle(),
                event.getMessage(),
                event.getCreationDate(),
                event.getCapacity(),
                tagDTOs
        );
    }

    private PostSummaryDTO convertToPostSummaryDTO(Post post) {
        List<TagSummaryDTO> tagDTOs = post.getTags().stream()
                .map(this::convertToTagSummaryDTO)
                .toList();

        String username = post.getUser() != null ? post.getUser().getUsername() : "Usuario Anónimo";

        return new PostSummaryDTO(
                post.getId(),
                post.getTitle(),
                post.getMessage(),
                post.getCreationDate(),
                post.isArchived(),
                username,
                tagDTOs
        );
    }

    @Override
    // ========== MÉTODOS OPTIMIZADOS PARA CONTROLADORES ==========
    public List<TagSummaryDTO> getAllTagsSummary() {
        return tagRepository.findAll().stream()
                .filter(java.util.Objects::nonNull) // Añadir filtro para evitar NullPointerException
                .map(this::convertToTagSummaryDTO)
                .toList();
    }

    @Override
    public List<EventSummaryDTO> getEventsSummaryByTagName(String tagName) {
        return eventRepository.findByTagsName(tagName).stream()
                .map(this::convertToEventSummaryDTO)
                .toList();
    }

    @Override
    public List<PostSummaryDTO> getPostsSummaryByTagName(String tagName) {
        return postRepository.findByTagsName(tagName).stream()
                .map(this::convertToPostSummaryDTO)
                .toList();
    }

    // ========== MÉTODOS PARA OBTENER TAGS POR ENTIDAD ==========
    @Override
    public List<TagSummaryDTO> getTagsByPostId(Long postId) {
        // Verificar que el post existe sin cargar toda la entidad
        if (!postRepository.existsById(postId)) {

            throw new EntityNotFoundException("Post not found with id: " + postId);
        }
        // Usar consulta directa para evitar problemas de serialización circular
        return tagRepository.findByPostId(postId).stream()
                .map(this::convertToTagSummaryDTO)
                .toList();
    }

    @Override
    public List<TagSummaryDTO> getTagsByEventId(Long eventId) {
        // Verificar que el evento existe sin cargar toda la entidad
        if (!eventRepository.existsById(eventId)) {
            throw new EntityNotFoundException("Event not found with id: " + eventId);
        }
        // Usar consulta directa para evitar problemas de serialización circular
        return tagRepository.findByEventId(eventId).stream()
                .map(this::convertToTagSummaryDTO)
                .toList();
    }
}
