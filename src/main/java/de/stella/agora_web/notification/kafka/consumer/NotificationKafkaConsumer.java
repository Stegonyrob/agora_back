package de.stella.agora_web.notification.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import de.stella.agora_web.comment.kafka.dto.CommentNotificationDTO;
import de.stella.agora_web.notification.service.EmailNotificationService;
import de.stella.agora_web.replies.kafka.dto.ReplyNotificationDTO;

/**
 * Consumer de Kafka que escucha notificaciones y envía emails al admin
 */
@Component
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true", matchIfMissing = false)
public class NotificationKafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(NotificationKafkaConsumer.class);

    @Autowired
    private EmailNotificationService emailNotificationService;

    /**
     * Escucha notificaciones de comentarios y envía email al admin
     */
    @KafkaListener(topics = "comments", groupId = "notification-service")
    public void handleCommentNotification(CommentNotificationDTO notification) {
        logger.info("🎧 Recibida notificación de comentario via Kafka: {}", notification);

        try {
            emailNotificationService.sendCommentNotification(notification);
            logger.info("✅ Notificación de comentario procesada exitosamente");
        } catch (Exception e) {
            logger.error("❌ Error procesando notificación de comentario", e);
        }
    }

    /**
     * Escucha notificaciones de respuestas y envía email al admin
     */
    @KafkaListener(topics = "replies", groupId = "notification-service")
    public void handleReplyNotification(ReplyNotificationDTO notification) {
        logger.info("🎧 Recibida notificación de respuesta via Kafka: {}", notification);

        try {
            emailNotificationService.sendReplyNotification(notification);
            logger.info("✅ Notificación de respuesta procesada exitosamente");
        } catch (Exception e) {
            logger.error("❌ Error procesando notificación de respuesta", e);
        }
    }
}
