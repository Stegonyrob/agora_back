package de.stella.agora_web.comment.kafka.component.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import de.stella.agora_web.censured.model.CensuredComment;
import de.stella.agora_web.censured.service.impl.CensuredCommentServiceImpl;
import de.stella.agora_web.comment.kafka.dto.CommentNotificationDTO;
import de.stella.agora_web.comment.kafka.service.IEmailService;
import de.stella.agora_web.comment.kafka.service.IPushNotificationService;
import de.stella.agora_web.moderation.service.impl.ModerationServiceImpl;

/**
 * Consumidor Kafka del topic "comments".
 *
 * Escenarios cubiertos: 1. Comentario censurado por moderación automática →
 * email al autor (rechazado) → email al admin (para revisión manual) 2.
 * Comentario aprobado → email al autor (publicado) → email al admin (nuevo
 * comentario pendiente de lectura)
 */
@Component
public class CommentNotificationConsumer {

    private static final Logger log = LoggerFactory.getLogger(CommentNotificationConsumer.class);

    private final IEmailService emailService;
    private final CensuredCommentServiceImpl censuredCommentService;
    private final ModerationServiceImpl moderationService;
    private final IPushNotificationService pushNotificationService;

    @Value("${app.admin.email:admin@agora.es}")
    private String adminEmail;

    public CommentNotificationConsumer(IEmailService emailService,
            CensuredCommentServiceImpl censuredCommentService,
            ModerationServiceImpl moderationService,
            IPushNotificationService pushNotificationService) {
        this.emailService = emailService;
        this.censuredCommentService = censuredCommentService;
        this.moderationService = moderationService;
        this.pushNotificationService = pushNotificationService;
    }

    @KafkaListener(topics = "comments")
    public void consume(CommentNotificationDTO notification) {
        log.info("Procesando notificación de comentario del autor: {}", notification.getAuthor());

        try {
            CensuredComment censuredComment = moderationService.moderateComment(notification.getComment());

            if (censuredComment != null) {
                // ── Escenario 1: comentario censurado ──────────────────────────────────

                // 1a. Notificar al autor que su comentario fue rechazado
                emailService.sendEmail(
                        notification.getAuthorEmail(),
                        "Tu comentario ha sido rechazado — Ágora",
                        """
                        Hola %s,

                        Tu comentario en el post "%s" ha sido rechazado por incumplir
                        las normas de la comunidad.

                        Motivo: %s

                        Si crees que esto es un error, puedes contactar con la moderación
                        respondiendo a este email.

                        Equipo de Ágora
                        """.formatted(
                                notification.getAuthor(),
                                notification.getPostTitle(),
                                censuredComment.getReason()));

                // 1b. Notificar al admin para que pueda revisar la decisión automática
                emailService.sendEmail(
                        adminEmail,
                        "[Moderación automática] Comentario rechazado en «" + notification.getPostTitle() + "»",
                        """
                        Se ha rechazado automáticamente un comentario. Revísalo por si necesita acción manual.

                        Post:    %s
                        Autor:   %s (%s)
                        Contenido: %s
                        Motivo de rechazo: %s

                        Accede al panel de administración para aprobar o confirmar el rechazo.
                        """.formatted(
                                notification.getPostTitle(),
                                notification.getAuthor(),
                                notification.getAuthorEmail(),
                                notification.getMessage(),
                                censuredComment.getReason()));

                censuredCommentService.save(censuredComment);

            } else {
                // ── Escenario 2: comentario aprobado ───────────────────────────────────

                // 2a. Notificar al autor que su comentario fue publicado
                emailService.sendEmail(
                        notification.getAuthorEmail(),
                        "Tu comentario ha sido publicado — Ágora",
                        """
                        Hola %s,

                        Tu comentario en el post "%s" ha sido publicado correctamente.

                        Otros usuarios ya pueden verlo y responderte.

                        Equipo de Ágora
                        """.formatted(notification.getAuthor(), notification.getPostTitle()));

                // 2b. Notificar al admin sobre el nuevo comentario
                emailService.sendEmail(
                        adminEmail,
                        "[Nuevo comentario] «" + notification.getPostTitle() + "» — " + notification.getAuthor(),
                        """
                        Hay un nuevo comentario publicado en el blog.

                        Post:    %s
                        Autor:   %s (%s)
                        Mensaje: %s

                        Revísalo en el panel de administración si necesitas moderarlo manualmente.
                        """.formatted(
                                notification.getPostTitle(),
                                notification.getAuthor(),
                                notification.getAuthorEmail(),
                                notification.getMessage()));

                // 2c. Push notification al autor (para apps móviles / PWA futuras)
                pushNotificationService.sendPushNotification(
                        "/topics/" + notification.getAuthor(),
                        "Comentario publicado",
                        "Tu comentario en «" + notification.getPostTitle() + "» ya está visible");
            }

        } catch (Exception e) {
            log.error("Error procesando notificación de comentario de {}: {}", notification.getAuthor(), e.getMessage(), e);
        }
    }
}
