package de.stella.agora_web.replies.kafka.component.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import de.stella.agora_web.censured.service.impl.CensuredCommentServiceImpl;
import de.stella.agora_web.comment.kafka.service.IEmailService;
import de.stella.agora_web.comment.kafka.service.IPushNotificationService;
import de.stella.agora_web.moderation.service.impl.ModerationServiceImpl;
import de.stella.agora_web.replies.kafka.dto.ReplyNotificationDTO;

/**
 * Consumidor de notificaciones de respuestas (replies) desde Kafka
 */
@Component // Habilitado para trabajar con Kafka
public class ReplyNotificationConsumerEnabled {

    @Autowired // Habilitado para trabajar con Kafka
    private IEmailService emailService;

    @Autowired // Habilitado para trabajar con Kafka
    private CensuredCommentServiceImpl censuredCommentService;

    @Autowired // Habilitado para trabajar con Kafka
    private ModerationServiceImpl moderationService;

    @Autowired // Habilitado para trabajar con Kafka
    private IPushNotificationService pushNotificationService;

    @KafkaListener(topics = "replies") // Habilitado para trabajar con Kafka
    public void consume(ReplyNotificationDTO notification) {
        System.out.println("Processing reply notification: " + notification.getMessage());

        try {
            // Moderamos la respuesta usando el servicio de moderación (basado en comentarios)
            // Note: Necesitaríamos un servicio específico para replies o adaptar el existente

            // Por ahora, asumimos que todas las respuestas no son censuradas
            // En un entorno real, aquí iría la lógica de moderación específica para replies
            // Si la respuesta no es censurada, enviamos notificaciones
            pushNotificationService.sendPushNotification("/topics/" + notification.getAuthor(),
                    "Notificación de respuesta", "Su respuesta ha sido publicada");

            // Notificar al administrador sobre la nueva respuesta
            emailService.sendEmail("admin@agora-blog.com",
                    "Nueva Respuesta en el Post",
                    "El post '" + notification.getPostTitle()
                    + "' ha recibido una nueva respuesta de " + notification.getAuthor()
                    + ": \"" + notification.getMessage() + "\"");

        } catch (Exception e) {
            // Log de error en caso de fallo al procesar la notificación
            System.err.println("Error processing reply notification: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
