package de.stella.agora_web.comment.kafka.component.consumer;

import de.stella.agora_web.censured.service.impl.CensuredCommentServiceImpl;
import de.stella.agora_web.comment.kafka.service.IEmailService;
import de.stella.agora_web.comment.kafka.service.IPushNotificationService;
import de.stella.agora_web.moderation.service.impl.ModerationServiceImpl;

// @Component // TEMPORALMENTE DESHABILITADO - Habilitar cuando Kafka esté configurado
public class CommentNotificationConsumer {

    // @Autowired // TEMPORALMENTE DESHABILITADO
    private IEmailService emailService;

    // @Autowired // TEMPORALMENTE DESHABILITADO
    private CensuredCommentServiceImpl censuredCommentService;

    // @Autowired // TEMPORALMENTE DESHABILITADO
    private ModerationServiceImpl moderationService;
    // @Autowired // TEMPORALMENTE DESHABILITADO
    private IPushNotificationService pushNotificationService;

    // @KafkaListener(topics = "comments") // TEMPORALMENTE DESHABILITADO - Habilitar cuando Kafka esté configurado
    /*
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
     */
}
