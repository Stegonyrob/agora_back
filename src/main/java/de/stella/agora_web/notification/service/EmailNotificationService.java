package de.stella.agora_web.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.stella.agora_web.comment.kafka.dto.CommentNotificationDTO;
import de.stella.agora_web.replies.kafka.dto.ReplyNotificationDTO;

/**
 * Servicio para enviar notificaciones por email a los administradores
 */
@Service
public class EmailNotificationService {

    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationService.class);

    @Value("${admin.email:admin@gmail.com}")
    private String adminEmail;

    /**
     * Envía notificación por email cuando se crea un nuevo comentario
     */
    public void sendCommentNotification(CommentNotificationDTO notification) {
        try {
            logger.info("📧 Enviando notificación de comentario a admin: {}", adminEmail);
            logger.info("📄 Post: {}", notification.getPostTitle());
            logger.info("👤 Usuario: {}", notification.getUserName());
            logger.info("💬 Comentario: {}", notification.getCommentContent());
            logger.info("🔗 URL: /posts/{}", notification.getPostId());

            // TODO: Implementar envío real de email con Spring Mail
            // Por ahora solo loggeamos para testing
            sendEmailToAdmin(
                    "Nuevo comentario en Ágora",
                    buildCommentEmailContent(notification)
            );

            logger.info("✅ Notificación de comentario enviada exitosamente");

        } catch (Exception e) {
            logger.error("❌ Error enviando notificación de comentario", e);
        }
    }

    /**
     * Envía notificación por email cuando se crea una nueva respuesta
     */
    public void sendReplyNotification(ReplyNotificationDTO notification) {
        try {
            logger.info("📧 Enviando notificación de respuesta a admin: {}", adminEmail);
            logger.info("📄 Post: {}", notification.getPostTitle());
            logger.info("👤 Usuario: {}", notification.getUserName());
            logger.info("💬 Respuesta: {}", notification.getReplyContent());
            logger.info("🔗 URL: /posts/{}", notification.getPostId());

            // TODO: Implementar envío real de email con Spring Mail
            sendEmailToAdmin(
                    "Nueva respuesta en Ágora",
                    buildReplyEmailContent(notification)
            );

            logger.info("✅ Notificación de respuesta enviada exitosamente");

        } catch (Exception e) {
            logger.error("❌ Error enviando notificación de respuesta", e);
        }
    }

    /**
     * Construye el contenido del email para comentarios
     */
    private String buildCommentEmailContent(CommentNotificationDTO notification) {
        return String.format("""
            ¡Hola Admin!
            
            Se ha creado un nuevo comentario en Ágora:
            
            📄 Post: %s
            👤 Usuario: %s
            💬 Comentario: %s
            🕒 Fecha: %s
            
            🔗 Ver post: /posts/%d
            
            ¡No olvides responder para mantener la comunidad activa!
            
            Saludos,
            Sistema Ágora
            """,
                notification.getPostTitle(),
                notification.getUserName(),
                notification.getCommentContent(),
                notification.getCreatedAt(),
                notification.getPostId()
        );
    }

    /**
     * Construye el contenido del email para respuestas
     */
    private String buildReplyEmailContent(ReplyNotificationDTO notification) {
        return String.format("""
            ¡Hola Admin!
            
            Se ha creado una nueva respuesta en Ágora:
            
            📄 Post: %s
            👤 Usuario: %s
            💬 Respuesta: %s
            🕒 Fecha: %s
            
            🔗 Ver post: /posts/%d
            
            ¡No olvides responder para mantener la conversación!
            
            Saludos,
            Sistema Ágora
            """,
                notification.getPostTitle(),
                notification.getUserName(),
                notification.getReplyContent(),
                notification.getCreatedAt(),
                notification.getPostId()
        );
    }

    /**
     * Simula envío de email (por ahora solo logging) TODO: Reemplazar con
     * implementación real usando Spring Boot Mail
     */
    private void sendEmailToAdmin(String subject, String content) {
        logger.info("📨 EMAIL ENVIADO:");
        logger.info("📧 Para: {}", adminEmail);
        logger.info("📋 Asunto: {}", subject);
        logger.info("📄 Contenido:\n{}", content);
        logger.info("📨 FIN EMAIL");
    }
}
