package de.stella.agora_web.tags.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.stella.agora_web.comment.model.Comment;
import de.stella.agora_web.comment.repository.CommentRepository;
import de.stella.agora_web.events.model.Event;
import de.stella.agora_web.events.repository.EventRepository;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.posts.repository.PostRepository;
import de.stella.agora_web.replies.model.Reply;
import de.stella.agora_web.replies.repository.ReplyRepository;
import de.stella.agora_web.tags.model.Tag;
import de.stella.agora_web.tags.repository.TagRepository;
import de.stella.agora_web.tags.service.ITagService;

@Service
public class TagServiceImpl implements ITagService {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
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
    public void addTagToReply(Long replyId, String tagName) {
        Reply reply = replyRepository.findById(replyId).orElse(null);
        Tag tag = getOrCreateTagByName(tagName);
        if (reply != null && tag != null) {
            reply.getTags().add(tag);
            replyRepository.save(reply);
        }
    }

    public void removeTagFromReply(Long replyId, String tagName) {
        Reply reply = replyRepository.findById(replyId).orElse(null);
        Tag tag = getTagByName(tagName);
        if (reply != null && tag != null) {
            reply.getTags().remove(tag);
            replyRepository.save(reply);
        }
    }

    public void addTagToComment(Long commentId, String tagName) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        Tag tag = getOrCreateTagByName(tagName);
        if (comment != null && tag != null) {
            comment.getTags().add(tag);
            commentRepository.save(comment);
        }
    }

    public void removeTagFromComment(Long commentId, String tagName) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        Tag tag = getTagByName(tagName);
        if (comment != null && tag != null) {
            comment.getTags().remove(tag);
            commentRepository.save(comment);
        }
    }

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

    public void addTagToReply(Long replyId, Long tagId) {
        Reply reply = replyRepository.findById(replyId).orElse(null);
        Tag tag = tagRepository.findById(tagId).orElse(null);
        if (reply != null && tag != null) {
            reply.getTags().add(tag);
            replyRepository.save(reply);
        }
    }

    public void removeTagFromReply(Long replyId, Long tagId) {
        Reply reply = replyRepository.findById(replyId).orElse(null);
        Tag tag = tagRepository.findById(tagId).orElse(null);
        if (reply != null && tag != null) {
            reply.getTags().remove(tag);
            replyRepository.save(reply);
        }
    }

    public void addTagToComment(Long commentId, Long tagId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        Tag tag = tagRepository.findById(tagId).orElse(null);
        if (comment != null && tag != null) {
            comment.getTags().add(tag);
            commentRepository.save(comment);
        }
    }

    public void removeTagFromComment(Long commentId, Long tagId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        Tag tag = tagRepository.findById(tagId).orElse(null);
        if (comment != null && tag != null) {
            comment.getTags().remove(tag);
            commentRepository.save(comment);
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

    // Métodos que necesita la interfaz actualizada
    @Override
    public List<Event> getEventsByTagName(String tagName) {
        return eventRepository.findByTagsName(tagName);
    }

    @Override
    public List<Post> getPostsByTagName(String tagName) {
        return postRepository.findByTagsName(tagName);
    }
}
