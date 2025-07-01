package de.stella.agora_web.comment.kafka.component.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import de.stella.agora_web.censured.model.CensuredComment;
import de.stella.agora_web.censured.service.impl.CensuredCommentServiceImpl;
import de.stella.agora_web.comment.kafka.dto.CommentNotificationDTO;
import de.stella.agora_web.comment.kafka.service.IEmailService;
import de.stella.agora_web.comment.kafka.service.IPushNotificationService;
import de.stella.agora_web.moderation.service.impl.ModerationServiceImpl;

/**
 * Consumidor de notificaciones de comentarios desde Kafka
 */
@Component // Habilitado para trabajar con Kafka
public class CommentNotificationConsumer {

    @Autowired // Habilitado para trabajar con Kafka
    private IEmailService emailService;

    @Autowired // Habilitado para trabajar con Kafka  
    private CensuredCommentServiceImpl censuredCommentService;

    @Autowired // Habilitado para trabajar con Kafka
    private ModerationServiceImpl moderationService;

    @Autowired // Habilitado para trabajar con Kafka
    private IPushNotificationService pushNotificationService;

    @KafkaListener(topics = "comments") // Habilitado para trabajar con Kafka
    public void consume(CommentNotificationDTO notification) {
        System.out.println("Processing comment notification: " + notification.getMessage());

        try {
            // Moderamos el comentario
            CensuredComment censuredComment = moderationService.moderateComment(notification.getComment());

            if (censuredComment != null) {
                // Si el comentario es censurado, enviamos un correo al autor
                emailService.sendEmail(notification.getAuthor(),
                        "Notificación de censura de comentario",
                        "Su comentario ha sido censurado por incumplir las normas del blog debido a: "
                        + censuredComment.getReason());
                // Guardamos el comentario censurado
                censuredCommentService.save(censuredComment);
            } else {
                // Si el comentario no es censurado, enviamos notificaciones al autor y al administrador
                pushNotificationService.sendPushNotification("/topics/" + notification.getAuthor(),
                        "Notificación de comentario", "Su comentario ha sido publicado");

                // Notificar al administrador sobre el nuevo comentario
                emailService.sendEmail("admin@agora-blog.com",
                        "Nuevo Comentario en el Post",
                        "El post '" + notification.getPostTitle()
                        + "' ha recibido un nuevo comentario de " + notification.getAuthor()
                        + ": \"" + notification.getMessage() + "\"");
            }
        } catch (Exception e) {
            System.err.println("Error processing comment notification: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
