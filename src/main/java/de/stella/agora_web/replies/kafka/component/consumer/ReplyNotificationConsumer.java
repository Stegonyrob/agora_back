package de.stella.agora_web.replies.kafka.component.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import de.stella.agora_web.censured.model.CensuredComment;
import de.stella.agora_web.censured.service.impl.CensuredCommentServiceImpl;
import de.stella.agora_web.comment.kafka.service.IEmailService;
import de.stella.agora_web.comment.kafka.service.IPushNotificationService;
import de.stella.agora_web.moderation.service.impl.ModerationServiceImpl;
import de.stella.agora_web.replies.kafka.dto.ReplyNotificationDTO;
import de.stella.agora_web.replies.model.ModeratableReply;
import de.stella.agora_web.replies.model.Reply;
import de.stella.agora_web.replies.repository.ReplyRepository;

/**
 * Consumidor de notificaciones de respuestas (replies) desde Kafka
 */
@Component
public class ReplyNotificationConsumer {

    private static final Logger log = LoggerFactory.getLogger(ReplyNotificationConsumer.class);

    private final IEmailService emailService;
    private final CensuredCommentServiceImpl censuredCommentService;
    private final ModerationServiceImpl moderationService;
    private final IPushNotificationService pushNotificationService;
    private final ReplyRepository replyRepository;

    public ReplyNotificationConsumer(IEmailService emailService,
            CensuredCommentServiceImpl censuredCommentService,
            ModerationServiceImpl moderationService,
            IPushNotificationService pushNotificationService,
            ReplyRepository replyRepository) {
        this.emailService = emailService;
        this.censuredCommentService = censuredCommentService;
        this.moderationService = moderationService;
        this.pushNotificationService = pushNotificationService;
        this.replyRepository = replyRepository;
    }

    @KafkaListener(topics = "replies")
    public void consume(ReplyNotificationDTO notification) {
        log.info("Processing reply notification: {}", notification.getMessage());

        try {
            Reply reply = replyRepository.findById(notification.getReplyId()).orElse(null);
            if (reply == null) {
                log.warn("Reply not found with id: {}", notification.getReplyId());
                return;
            }

            CensuredComment censuredReply = moderationService.moderateComment(
                    new ModeratableReply(reply.getMessage(), reply.getUser()));

            if (censuredReply != null) {
                emailService.sendEmail(notification.getAuthor(),
                        "Notificación de censura de respuesta",
                        "Su respuesta ha sido censurada por incumplir las normas del blog debido a: "
                        + censuredReply.getReason());
                censuredCommentService.save(censuredReply);
            } else {
                pushNotificationService.sendPushNotification("/topics/" + notification.getAuthor(),
                        "Notificación de respuesta", "Su respuesta ha sido publicada");

                emailService.sendEmail("admin@agora-blog.com",
                        "Nueva Respuesta en el Post",
                        "El post '" + notification.getPostTitle()
                        + "' ha recibido una nueva respuesta de " + notification.getAuthor()
                        + ": \"" + notification.getMessage() + "\"");
            }
        } catch (Exception e) {
            log.error("Error processing reply notification: {}", e.getMessage(), e);
        }
    }
}
