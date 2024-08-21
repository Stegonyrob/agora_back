package de.stella.agora_web.tags.service;

import de.stella.agora_web.tags.model.Tag;
import java.util.List;

public interface ITagService {
  List<Tag> getAllTags();

  Tag getTagById(Long id);

  Tag createTag(String name);

  void addTagToPost(Long postId, Long tagId);

  void removeTagFromPost(Long postId, Long tagId);

  Tag getTagByName(String tagName);

  void addTagToReply(Long replyId, Long tagId); // added

  void removeTagFromReply(Long replyId, Long tagId); // added

  void addTagToComment(Long commentId, Long tagId); // added

  void removeTagFromComment(Long commentId, Long tagId); // added
  List<String> extractHashtags(String message);
}
