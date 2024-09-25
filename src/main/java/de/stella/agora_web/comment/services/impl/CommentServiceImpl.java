package de.stella.agora_web.comment.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import de.stella.agora_web.comment.controller.dto.CommentDTO;
import de.stella.agora_web.comment.kafka.component.producer.CommentKafkaProducer;
import de.stella.agora_web.comment.kafka.dto.CommentNotificationDTO;
import de.stella.agora_web.comment.model.Comment;
import de.stella.agora_web.comment.repository.CommentRepository;
import de.stella.agora_web.comment.services.ICommentService;
import de.stella.agora_web.comment.services.IMessageQueueService;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.posts.repository.PostRepository;
import de.stella.agora_web.tags.model.Tag;
import de.stella.agora_web.tags.service.ITagService;
import de.stella.agora_web.user.model.User;

@Service
public class CommentServiceImpl implements ICommentService {

  @Autowired
  private CommentRepository CommentRepository;

  @Autowired
  private IMessageQueueService messageQueue;
  @Autowired
  private CommentKafkaProducer kafkaProducer;
  @Autowired
  private ITagService tagService;

  @Autowired
  private PostRepository postRepository;

  @Override
  public Comment getCommentById(Long id) {
    return CommentRepository.findById(id).orElse(null);
  }

  public Comment createComment(CommentDTO commentDTO, User user) {
    Comment newComment = new Comment();
    newComment.setPost(postRepository.findById(commentDTO.getPostId()).orElse(null));
    newComment.setUser(user);
    newComment.setMessage(commentDTO.getMessage());
    newComment.setCreationDate(LocalDateTime.now());

    List<Tag> tags = new ArrayList<>(commentDTO.getTags().length);
    for (String tagName : commentDTO.getTags()) {
      Tag tag = tagService.getTagByName(tagName);
      if (tag == null) {
        tag = tagService.createTag(tagName);
      }
      tags.add(tag);
    }
    tags.addAll(tagService.extractHashtags(commentDTO.getMessage()).stream().map(tagService::getTagByName)
        .filter(Objects::nonNull).collect(Collectors.toList()));

    newComment.setTags(tags);

    // Enviar notificación a Kafka
    CommentNotificationDTO notification = new CommentNotificationDTO();
    notification.setCommentId(newComment.getId());
    notification.setAuthor(user.getUsername());
    notification.setMessage(newComment.getMessage());
    kafkaProducer.sendCommentNotification(notification);
    return newComment;
  }

  @Scheduled(fixedDelay = 1000)
  public void processMessageQueue() {
    List<Comment> comments = messageQueue.poll();
    CommentRepository.saveAll(comments);
  }

  public List<Comment> getAllComments() {
    return CommentRepository.findAllByOrderByCreationDateAsc();
  }

  @Override
  public Comment updateComment(Long id, CommentDTO CommentDTO) {
    Comment existingComment = CommentRepository.findById(id).orElse(null);
    if (existingComment != null) {
      existingComment.setMessage(CommentDTO.getMessage());

      // Remove existing tags
      existingComment.getTags().clear();

      // Add tags from tag list
      List<Tag> tags = new ArrayList<>();
      for (String tagName : CommentDTO.getTags()) {
        Tag tag = tagService.getTagByName(tagName);
        if (tag == null) {
          tag = tagService.createTag(tagName);
        }
        tags.add(tag);
      }

      // Add tags from hashtags in Comment message
      List<String> hashtags = tagService.extractHashtags(CommentDTO.getMessage());
      for (String hashtag : hashtags) {
        Tag tag = tagService.getTagByName(hashtag);
        if (tag == null) {
          tag = tagService.createTag(hashtag);
        }
        tags.add(tag);
      }

      existingComment.setTags(tags);

      return CommentRepository.save(existingComment);
    }
    return null;
  }

  @SuppressWarnings("")
  @Override
  public void deleteComment(Long id) {
    CommentRepository.deleteById(id);
  }

  public Comment save(CommentDTO CommentDTO) {
    Comment Comment = new Comment();
    return CommentRepository.save(Comment);
  }

  @Override
  public List<Comment> getAllComment() {
    return CommentRepository.findAll();
  }

  @Override
  public Comment createComment(CommentDTO commentDTO, Object object) {
    Comment comment = new Comment();
    comment.setMessage(commentDTO.getMessage());
    comment.setCreationDate(LocalDateTime.now());

    User user = null;
    if (object instanceof User user1) {
      user = user1;
    }
    comment.setUser(user);

    Post post = null;
    if (object instanceof Post post1) {
      post = post1;
    }
    comment.setPost(post);

    CommentRepository.save(comment);
    return comment;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Comment> getCommentsByPostId(Long postId) {
    return ((Collection<Comment>) CommentRepository.findByPostId(postId)).stream().collect(Collectors.toList());
  }

  @Override
  public List<Comment> getCommentsByUserId(Long userId) {
    return CommentRepository.findByUserId(userId);
  }

  @Override
  public List<Comment> getCommentsByTagName(String tagName) {
    return CommentRepository.findByTagsName(tagName);
  }

  @Override
  public Comment findById(Long commentId) {
    return CommentRepository.findById(commentId).orElseGet(() -> null);
  }
}
