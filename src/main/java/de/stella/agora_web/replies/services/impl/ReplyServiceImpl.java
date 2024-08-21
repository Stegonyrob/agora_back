package de.stella.agora_web.replies.services.impl;

import de.stella.agora_web.posts.repository.PostRepository;
import de.stella.agora_web.replies.controller.dto.ReplyDTO;
import de.stella.agora_web.replies.model.Reply;
import de.stella.agora_web.replies.repository.ReplyRepository;
import de.stella.agora_web.replies.services.IReplyService;
import de.stella.agora_web.tags.model.Tag;
import de.stella.agora_web.tags.repository.TagRepository;
import de.stella.agora_web.tags.service.ITagService;
import de.stella.agora_web.user.model.User;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReplyServiceImpl implements IReplyService {

  @Autowired
  private ReplyRepository replyRepository;

  @Autowired
  private ITagService tagService;

  @Autowired
  private PostRepository postRepository;

  @SuppressWarnings("unused")
  @Autowired
  private TagRepository tagRepository;

  @Override
  public List<Reply> getAllReplys() {
    return replyRepository.findAll();
  }

  @Override
  public Reply getReplyById(Long id) {
    return replyRepository.findById(id).orElse(null);
  }

  @Override
  public Reply createReply(
    @SuppressWarnings("rawtypes") ReplyDTO replyDTO,
    User user
  ) {
    Reply reply = new Reply();
    reply.setPost(postRepository.findById(replyDTO.getPostId()).orElse(null));
    reply.setUser(user);
    reply.setMessage(replyDTO.getMessage());
    reply.setCreationDate(replyDTO.getCreationDate());

    // Add tags from tag list
    List<Tag> tags = new ArrayList<>();
    for (String tagName : replyDTO.getTags()) {
      Tag tag = tagService.getTagByName(tagName);
      if (tag == null) {
        tag = tagService.createTag(tagName);
      }
      tags.add(tag);
    }

    // Add tags from hashtags in reply message
    List<String> hashtags = tagService.extractHashtags(reply.getMessage());
    for (String hashtag : hashtags) {
      Tag tag = tagService.getTagByName(hashtag);
      if (tag == null) {
        tag = tagService.createTag(hashtag);
      }
      tags.add(tag);
    }

    reply.setTags(tags);

    return replyRepository.save(reply);
  }

  @Override
  public Reply updateReply(
    Long id,
    @SuppressWarnings("rawtypes") ReplyDTO replyDTO
  ) {
    Reply existingReply = replyRepository.findById(id).orElse(null);
    if (existingReply != null) {
      existingReply.setMessage(replyDTO.getMessage());

      // Remove existing tags
      existingReply.getTags().clear();

      // Add tags from tag list
      List<Tag> tags = new ArrayList<>();
      for (String tagName : replyDTO.getTags()) {
        Tag tag = tagService.getTagByName(tagName);
        if (tag == null) {
          tag = tagService.createTag(tagName);
        }
        tags.add(tag);
      }

      // Add tags from hashtags in reply message
      List<String> hashtags = tagService.extractHashtags(replyDTO.getMessage());
      for (String hashtag : hashtags) {
        Tag tag = tagService.getTagByName(hashtag);
        if (tag == null) {
          tag = tagService.createTag(hashtag);
        }
        tags.add(tag);
      }

      existingReply.setTags(tags);

      return replyRepository.save(existingReply);
    }
    return null;
  }

  @SuppressWarnings("")
  @Override
  public void deleteReply(Long id) {
    replyRepository.deleteById(id);
  }

  @Override
  public Reply save(@SuppressWarnings("rawtypes") ReplyDTO replyDTO) {
    Reply reply = new Reply();
    return replyRepository.save(reply);
  }
}
