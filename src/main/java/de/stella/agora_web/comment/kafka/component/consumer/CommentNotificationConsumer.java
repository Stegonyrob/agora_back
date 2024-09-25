package de.stella.agora_web.comment.kafka.component.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import de.stella.agora_web.censured.model.CensuredComment;
import de.stella.agora_web.censured.service.ICensuredCommentService;
import de.stella.agora_web.comment.kafka.dto.CommentNotificationDTO;
import de.stella.agora_web.moderation.services.IModerationService;

// Componente de Spring que escucha los mensajes de Kafka relacionados con comentarios
@Component
public class CommentNotificationConsumer {

    @Autowired
    private IModerationService moderationService;

    @Autowired
    private ICensuredCommentService censuredCommentService;

    // Método que consume los mensajes del tópico "comments"
    @KafkaListener(topics = "comments")
    public void consume(CommentNotificationDTO notification) {
        // Si la notificación es nula, no hacemos nada
        if (notification == null) {
            return;
        }

        // Moderamos el comentario
        CensuredComment censuredComment = moderationService.moderateComment(notification.getComment());

        try {
            if (censuredComment != null) {
                // Si el comentario es censurado, enviamos un correo al autor
                sendEmailToAuthor(notification, censuredComment);
            } else {
                // Si el comentario no es censurado, lo publicamos y notificamos al autor y al
                // administrador
                publishComment(notification);
                sendPushNotificationToUser(notification);
                notifyAdmin(notification);
            }

            // Actualizamos un contador de comentarios en la base de datos
            updateCommentsCounter(notification);
        } catch (Exception e) {
            // Manejo de excepciones en caso de error
            // ...
        }
    }

    // Método para enviar un correo electrónico al autor del comentario si es
    // censurado
    private void sendEmailToAuthor(CommentNotificationDTO notification, CensuredComment censuredComment) {
        // Lógica para enviar el correo al autor
        String toAddress = notification.getAuthor();
        String subject = "Notificación de censura de comentario";
        String body = "Su comentario ha sido censurado por incumplir las normas del blog debido a: "
                + censuredComment.getReason();

        // Implementación del envío de correo...
    }

    // Método para publicar el comentario y notificar al autor y al administrador
    private void publishComment(CommentNotificationDTO notification) {
        // Lógica para publicar el comentario
        // ...
    }

    // Método para enviar una notificación push al autor del comentario
    private void sendPushNotificationToUser(CommentNotificationDTO notification) {
        // Lógica para enviar una notificación push al autor
        // Ejemplo: "Tu comentario ha sido publicado"
        // ...
    }

    // Método para notificar al administrador sobre el nuevo comentario
    private void notifyAdmin(CommentNotificationDTO notification) {
        // Lógica para notificar al administrador
        // Ejemplo: "El post 'titulo del post' ha recibido un nuevo comentario"
        // ...
    }

    // Método para actualizar un contador de comentarios en la base de datos
    private void updateCommentsCounter(CommentNotificationDTO notification) {
        // Lógica para actualizar el contador de comentarios
        // ...
    }
}
