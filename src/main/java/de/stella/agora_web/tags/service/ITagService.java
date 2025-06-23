package de.stella.agora_web.tags.service;

import java.util.List;

import de.stella.agora_web.tags.model.Tag;

public interface ITagService {

    List<Tag> getAllTags();

    Tag getTagById(Long id);

    Tag getTagByName(String name);

    Tag getOrCreateTagByName(String name);

    List<String> extractHashtags(String text);

    void addTagToPost(Long postId, String tagName);

    void removeTagFromPost(Long postId, String tagName);

    void addTagToReply(Long replyId, String tagName);

    void removeTagFromReply(Long replyId, String tagName);

    void addTagToComment(Long commentId, String tagName);

    void removeTagFromComment(Long commentId, String tagName);

    Tag createTag(String tagName);
}
