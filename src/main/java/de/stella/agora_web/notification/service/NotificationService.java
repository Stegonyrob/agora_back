package de.stella.agora_web.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired(required = false) // No es obligatorio si Kafka no está disponible
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.enabled:false}")
    private boolean kafkaEnabled;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Envía notificación por email solo si Kafka está disponible
     */
    public void sendEmailNotification(String toAddress, String subject, String body) {
        if (kafkaTemplate != null && kafkaEnabled) {
            try {
                EmailNotification email = new EmailNotification(toAddress, subject, body, System.currentTimeMillis());
                String emailJson = objectMapper.writeValueAsString(email);
                kafkaTemplate.send("admin-notifications", emailJson);
                logger.info("Notificación enviada al administrador: {}", subject);
            } catch (JsonProcessingException e) {
                logger.error("Error al enviar notificación por email", e);
            }
        } else {
            logger.info("Kafka no disponible. Notificación no enviada: {}", subject);
        }
    }

    /**
     * Notifica al administrador sobre nuevos comentarios que requieren
     * moderación
     */
    public void notifyNewCommentForModeration(String postTitle, String commentContent, String authorEmail) {
        String subject = "🔍 Nuevo comentario para moderar";
        String body = String.format(
                "Se ha recibido un nuevo comentario que requiere moderación:\n\n"
                + "📝 Post: %s\n"
                + "💬 Comentario: %s\n"
                + "👤 Autor: %s\n\n"
                + "Por favor, revisa el panel de administración para moderar este contenido.",
                postTitle, commentContent, authorEmail
        );

        sendEmailNotification("admin@agora-educativo.com", subject, body);
    }

    /**
     * Notifica al administrador sobre nuevas respuestas que requieren
     * moderación
     */
    public void notifyNewReplyForModeration(String postTitle, String commentContent, String replyContent, String authorEmail) {
        String subject = "🔍 Nueva respuesta para moderar";
        String body = String.format(
                "Se ha recibido una nueva respuesta que requiere moderación:\n\n"
                + "📝 Post: %s\n"
                + "💬 Comentario original: %s\n"
                + "↳ Respuesta: %s\n"
                + "👤 Autor: %s\n\n"
                + "Por favor, revisa el panel de administración para moderar este contenido.",
                postTitle, commentContent, replyContent, authorEmail
        );

        sendEmailNotification("admin@agora-educativo.com", subject, body);
    }

    /**
     * Verifica si el servicio de notificaciones está activo
     */
    public boolean isActive() {
        return kafkaTemplate != null && kafkaEnabled;
    }

    // Clase interna para la estructura del email
    private record EmailNotification(String toAddress, String subject, String body, long timestamp) {

    }
}
