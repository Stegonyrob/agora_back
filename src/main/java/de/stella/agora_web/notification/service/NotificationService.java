package de.stella.agora_web.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final boolean kafkaEnabled;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public NotificationService(@Value("${kafka.enabled:false}") boolean kafkaEnabled,
            KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaEnabled = kafkaEnabled;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Envía notificación por email solo si Kafka está disponible
     */
    public void sendEmailNotification(String toAddress, String subject, String body) {
        if (kafkaTemplate != null && kafkaEnabled) {
            try {
                EmailNotification email = new EmailNotification(toAddress, subject, body);
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
        String body = ("""
                Se ha recibido un nuevo comentario que requiere moderación:
                
                📝 Post: %s
                💬 Comentario: %s
                👤 Autor: %s
                
                Por favor, revisa el panel de administración para moderar este contenido.""").formatted(
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
        String body = ("""
                Se ha recibido una nueva respuesta que requiere moderación:
                
                📝 Post: %s
                💬 Comentario original: %s
                ↳ Respuesta: %s
                👤 Autor: %s
                
                Por favor, revisa el panel de administración para moderar este contenido.""").formatted(
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
    @SuppressWarnings("unused")
    private static class EmailNotification {

        private final String toAddress;
        private final String subject;
        private final String body;
        private final long timestamp;

        public EmailNotification(String toAddress, String subject, String body) {
            this.toAddress = toAddress;
            this.subject = subject;
            this.body = body;
            this.timestamp = System.currentTimeMillis();
        }

        public String getToAddress() {
            return toAddress;
        }

        public String getSubject() {
            return subject;
        }

        public String getBody() {
            return body;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}
