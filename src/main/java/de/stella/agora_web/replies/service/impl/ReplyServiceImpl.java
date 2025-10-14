package de.stella.agora_web.replies.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.stella.agora_web.comment.repository.CommentRepository;
import de.stella.agora_web.moderation.service.IModerationService;
import de.stella.agora_web.replies.controller.dto.ReplyDTO;
import de.stella.agora_web.replies.kafka.component.producer.ReplyKafkaProducer;
import de.stella.agora_web.replies.kafka.dto.ReplyNotificationDTO;
import de.stella.agora_web.replies.model.ModeratableReply;
import de.stella.agora_web.replies.model.Reply;
import de.stella.agora_web.replies.repository.ReplyRepository;
import de.stella.agora_web.replies.service.IReplyService;
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

    @Autowired // Siempre disponible (real o dummy según kafka.enabled)
    private ReplyKafkaProducer kafkaProducer;

    @Autowired
    private IModerationService moderationService;

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
        if (replyDTO.getCreationDate() != null) {
            reply.setCreationDate(replyDTO.getCreationDate());
        } else {
            reply.setCreationDate(LocalDateTime.now());
        }
        reply.setArchived(false);
        User replyUser = userRepository.findById(replyDTO.getUserId()).orElse(null);
        reply.setUser(replyUser);
        reply.setComment(commentRepository.findById(replyDTO.getCommentId()).orElse(null));

        // --- MODERACIÓN: Verificar contenido inapropiado antes de guardar ---
        ModeratableReply moderatableReply = new ModeratableReply(reply.getMessage(), replyUser);
        var censured = moderationService.moderateComment(moderatableReply);
        if (censured != null) {
            throw new IllegalArgumentException("Respuesta rechazada por contenido inapropiado");
        }
        // --- FIN MODERACIÓN ---

        Reply savedReply = replyRepository.save(reply);

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
            // ✅ MODERACIÓN: Verificar contenido inapropiado ANTES de actualizar
            existingReply.setMessage(replyDTO.getMessage());
            ModeratableReply moderatableReply = new ModeratableReply(existingReply.getMessage(), existingReply.getUser());
            var censured = moderationService.moderateComment(moderatableReply);
            if (censured != null) {
                throw new IllegalArgumentException("Respuesta editada rechazada por contenido inapropiado");
            }

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
    public List<Reply> getRepliesByTagId(Long tagId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
