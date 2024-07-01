package de.stella.agora_web.tags.service;

import java.util.List;

import de.stella.agora_web.tags.module.Tag;

public interface ITagService {
    List<Tag> getAllTags();

    Tag getTagById(Long id);

    Tag createTag(String name);

    void addTagToPost(Long postId, Long tagId);

    void removeTagFromPost(Long postId, Long tagId);

    Tag getTagByName(String tagName);

    void addTagToReply(Long replyId, Long tagId); // added

    void removeTagFromReply(Long replyId, Long tagId); // added

    List<String> extractHashtags(String message);
}