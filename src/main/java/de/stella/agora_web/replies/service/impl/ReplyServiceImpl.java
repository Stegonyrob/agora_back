package de.stella.agora_web.replies.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.stella.agora_web.comment.repository.CommentRepository;
import de.stella.agora_web.replies.controller.dto.ReplyDTO;
import de.stella.agora_web.replies.kafka.component.producer.ReplyKafkaProducer;
import de.stella.agora_web.replies.kafka.dto.ReplyNotificationDTO;
import de.stella.agora_web.replies.model.Reply;
import de.stella.agora_web.replies.repository.ReplyRepository;
import de.stella.agora_web.replies.service.IReplyService;
import de.stella.agora_web.tags.model.Tag;
import de.stella.agora_web.tags.service.ITagService;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.repository.UserRepository;

@Service
public class ReplyServiceImpl implements IReplyService {

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ITagService tagService;

    @Autowired // Siempre disponible (real o dummy según kafka.enabled)
    private ReplyKafkaProducer kafkaProducer;

    @Override
    public List<Reply> getAllReplies() {
        return replyRepository.findAll();
    }

    @Override
    public Reply getReplyById(Long id) {
        return replyRepository.findById(id).orElse(null);
    }

    @Override
    public Reply createReply(ReplyDTO replyDTO, User user) {
        Reply reply = new Reply();

        reply.setMessage(replyDTO.getMessage());

        // Si el DTO trae fecha, úsala; si no, pon la actual
        if (replyDTO.getCreationDate() != null) {
            reply.setCreationDate(replyDTO.getCreationDate());
        } else {
            reply.setCreationDate(java.time.LocalDateTime.now());
        }

        // Siempre marca como no archivado al crear
        reply.setArchived(false);

        reply.setUser(userRepository.findById(replyDTO.getUserId()).orElse(null));
        reply.setComment(commentRepository.findById(replyDTO.getCommentId()).orElse(null));

        // Asigna tags
        List<Tag> tags = new ArrayList<>();
        for (String tagName : replyDTO.getTags()) {
            Tag tag = tagService.getTagByName(tagName);
            if (tag == null) {
                tag = tagService.createTag(tagName);
            }
            tags.add(tag);
        }
        List<String> hashtags = tagService.extractHashtags(reply.getMessage());
        for (String hashtag : hashtags) {
            Tag tag = tagService.getTagByName(hashtag);
            if (tag == null) {
                tag = tagService.createTag(hashtag);
            }
            tags.add(tag);
        }
        reply.setTags(tags);

        // Guardar la respuesta
        Reply savedReply = replyRepository.save(reply);

        // Enviar notificación Kafka - Siempre disponible (dummy si kafka está deshabilitado)
        ReplyNotificationDTO notification = new ReplyNotificationDTO();
        notification.setReplyId(savedReply.getId());
        notification.setCommentId(savedReply.getComment().getId());
        notification.setAuthor(user.getUsername());
        notification.setMessage(savedReply.getMessage());

        kafkaProducer.sendReplyNotification(notification);

        return savedReply;
    }

    @Override
    public Reply updateReply(Long id, ReplyDTO replyDTO) {
        Reply existingReply = replyRepository.findById(id).orElse(null);
        if (existingReply != null) {
            existingReply.setMessage(replyDTO.getMessage());
            existingReply.getTags().clear();

            List<Tag> tags = new ArrayList<>();
            for (String tagName : replyDTO.getTags()) {
                Tag tag = tagService.getTagByName(tagName);
                if (tag == null) {
                    tag = tagService.createTag(tagName);
                }
                tags.add(tag);
            }
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

    @Override
    public void deleteReply(Long id) {
        replyRepository.deleteById(id);
    }

    @Override
    public List<Reply> getRepliesByCommentId(Long commentId) {
        return replyRepository.findByCommentId(commentId);
    }

    @Override
    public List<Reply> getRepliesByUserId(Long userId) {
        return replyRepository.findByUserId(userId);
    }

    @Override
    public List<Reply> getRepliesByTagName(String tagName) {
        return replyRepository.findAllByTagsName(tagName);
    }

    @Override
    public List<Reply> getRepliesByTagId(Long tagId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
