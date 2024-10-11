package de.stella.agora_web.comment.kafka.component.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import de.stella.agora_web.censured.model.CensuredComment;
import de.stella.agora_web.censured.service.impl.CensuredCommentServiceImpl;
import de.stella.agora_web.comment.kafka.dto.CommentNotificationDTO;
import de.stella.agora_web.comment.kafka.services.IEmailService;
import de.stella.agora_web.comment.kafka.services.IPushNotificationService;
import de.stella.agora_web.moderation.services.impl.ModerationServiceImpl;

@Component
public class CommentNotificationConsumer {
    @Autowired
    private IEmailService emailService;

    @Autowired
    private CensuredCommentServiceImpl censuredCommentService;

    @Autowired
    private ModerationServiceImpl moderationService;
    @Autowired
    private IPushNotificationService pushNotificationService;

    @KafkaListener(topics = "comments")
    public void consume(CommentNotificationDTO notification) {
        // Moderamos el comentario
        CensuredComment censuredComment = moderationService.moderateComment(notification.getComment());

        try {
            if (censuredComment != null) {
                // Si el comentario es censurado, enviamos un correo al autor
                emailService.sendEmail(notification.getAuthor(), "Notificación de censura de comentario",
                        "Su comentario ha sido censurado por incumplir las normas del blog debido a: "
                                + censuredComment.getReason());
                // Guardamos el comentario censurado
                censuredCommentService.save(censuredComment);
            } else {
                // Si el comentario no es censurado, enviamos notificaciones al autor y al
                // administrador
                pushNotificationService.sendPushNotification("/topics/" + notification.getAuthor(),
                        "Notificación de comentario", "Su comentario ha sido publicado");
                emailService.sendEmail("admin@tu-blog.com", "Nuevo Comentario en el Post",
                        "El post '" + notification.getPostTitle() + "' ha recibido un nuevo comentario.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}