package de.stella.agora_web.replies.kafka.component.consumer;

import de.stella.agora_web.censured.service.impl.CensuredCommentServiceImpl;
import de.stella.agora_web.comment.kafka.service.IEmailService;
import de.stella.agora_web.comment.kafka.service.IPushNotificationService;
import de.stella.agora_web.moderation.service.impl.ModerationServiceImpl;

/**
 * Consumidor de notificaciones de respuestas (replies) desde Kafka
 */
// @Component // TEMPORALMENTE DESHABILITADO - Habilitar cuando Kafka esté configurado
@SuppressWarnings("all")
public class ReplyNotificationConsumer {

    // @Autowired // TEMPORALMENTE DESHABILITADO
    private IEmailService emailService;

    // @Autowired // TEMPORALMENTE DESHABILITADO
    private CensuredCommentServiceImpl censuredCommentService;

    // @Autowired // TEMPORALMENTE DESHABILITADO
    private ModerationServiceImpl moderationService;

    // @Autowired // TEMPORALMENTE DESHABILITADO
    private IPushNotificationService pushNotificationService;

    // @KafkaListener(topics = "replies") // TEMPORALMENTE DESHABILITADO - Habilitar cuando Kafka esté configurado
    /*
    public void consume(ReplyNotificationDTO notification) {
        // Moderamos la respuesta (reply)
        Reply reply = notification.getReply();
        CensuredComment censuredReply = moderationService.moderateComment(reply);

        try {
            if (censuredReply != null) {
                // Si la respuesta es censurada, enviamos un correo al autor
                emailService.sendEmail(notification.getAuthor(), 
                    "Notificación de censura de respuesta",
                    "Su respuesta ha sido censurada por incumplir las normas del blog debido a: "
                            + censuredReply.getReason());
                // Guardamos la respuesta censurada
                censuredCommentService.save(censuredReply);
            } else {
                // Si la respuesta no es censurada, enviamos notificaciones
                pushNotificationService.sendPushNotification("/topics/" + notification.getAuthor(),
                        "Notificación de respuesta", "Su respuesta ha sido publicada");

                // Notificar al administrador sobre la nueva respuesta
                emailService.sendEmail("admin@agora-blog.com", 
                    "Nueva Respuesta en el Post",
                    "El post '" + notification.getPostTitle() + 
                    "' ha recibido una nueva respuesta de " + notification.getAuthor() + 
                    ": \"" + notification.getMessage() + "\"");
            }
        } catch (Exception e) {
            // Log de error en caso de fallo al procesar la notificación
            System.err.println("Error processing reply notification: " + e.getMessage());
            e.printStackTrace();
        }
    }
     */
}
