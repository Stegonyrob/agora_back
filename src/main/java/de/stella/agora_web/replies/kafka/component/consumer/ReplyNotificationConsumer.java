package de.stella.agora_web.replies.kafka.component.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import de.stella.agora_web.censured.model.CensuredComment;
import de.stella.agora_web.censured.service.impl.CensuredCommentServiceImpl;
import de.stella.agora_web.comment.kafka.service.IEmailService;
import de.stella.agora_web.comment.kafka.service.IPushNotificationService;
import de.stella.agora_web.moderation.service.impl.ModerationServiceImpl;
import de.stella.agora_web.replies.kafka.dto.ReplyNotificationDTO;
import de.stella.agora_web.replies.model.ModeratableReply;
import de.stella.agora_web.replies.model.Reply;
import de.stella.agora_web.replies.repository.ReplyRepository;

/**
 * Consumidor Kafka del topic "replies".
 *
 * Escenarios cubiertos: 1. Respuesta censurada → email al autor de la respuesta
 * (rechazada) → email al admin (para revisión manual) 2. Respuesta aprobada →
 * email al autor del comentario original (tienes una respuesta) → email al
 * admin (nueva respuesta publicada)
 */
@Component
public class ReplyNotificationConsumer {

    private static final Logger log = LoggerFactory.getLogger(ReplyNotificationConsumer.class);

    private final IEmailService emailService;
    private final CensuredCommentServiceImpl censuredCommentService;
    private final ModerationServiceImpl moderationService;
    private final IPushNotificationService pushNotificationService;
    private final ReplyRepository replyRepository;

    @Value("${app.admin.email:admin@agora.es}")
    private String adminEmail;

    public ReplyNotificationConsumer(IEmailService emailService,
            CensuredCommentServiceImpl censuredCommentService,
            ModerationServiceImpl moderationService,
            IPushNotificationService pushNotificationService,
            ReplyRepository replyRepository) {
        this.emailService = emailService;
        this.censuredCommentService = censuredCommentService;
        this.moderationService = moderationService;
        this.pushNotificationService = pushNotificationService;
        this.replyRepository = replyRepository;
    }

    @KafkaListener(topics = "replies")
    public void consume(ReplyNotificationDTO notification) {
        log.info("Procesando notificación de respuesta del autor: {}", notification.getAuthor());

        try {
            Reply reply = replyRepository.findById(notification.getReplyId()).orElse(null);
            if (reply == null) {
                log.warn("Respuesta no encontrada con id: {}", notification.getReplyId());
                return;
            }

            CensuredComment censuredReply = moderationService.moderateComment(
                    new ModeratableReply(reply.getMessage(), reply.getUser()));

            if (censuredReply != null) {
                // ── Escenario 1: respuesta censurada ──────────────────────────────────

                // 1a. Notificar al autor de la respuesta
                String replyAuthorEmail = reply.getUser() != null ? reply.getUser().getEmail() : null;
                emailService.sendEmail(
                        replyAuthorEmail,
                        "Tu respuesta ha sido rechazada — Ágora",
                        """
                        Hola %s,

                        Tu respuesta en el post "%s" ha sido rechazada por incumplir
                        las normas de la comunidad.

                        Motivo: %s

                        Si crees que esto es un error, contacta con la moderación.

                        Equipo de Ágora
                        """.formatted(
                                notification.getAuthor(),
                                notification.getPostTitle(),
                                censuredReply.getReason()));

                // 1b. Notificar al admin para revisión manual
                emailService.sendEmail(
                        adminEmail,
                        "[Moderación automática] Respuesta rechazada en «" + notification.getPostTitle() + "»",
                        """
                        Se ha rechazado automáticamente una respuesta. Revísala por si necesita acción manual.

                        Post:    %s
                        Autor de la respuesta: %s
                        Contenido: %s
                        Motivo: %s
                        """.formatted(
                                notification.getPostTitle(),
                                notification.getAuthor(),
                                notification.getMessage(),
                                censuredReply.getReason()));

                censuredCommentService.save(censuredReply);

            } else {
                // ── Escenario 2: respuesta aprobada ───────────────────────────────────

                // 2a. Notificar al autor del comentario original (alguien le respondió)
                if (reply.getComment() != null && reply.getComment().getUser() != null) {
                    String commentAuthorEmail = reply.getComment().getUser().getEmail();
                    String commentAuthorName = reply.getComment().getUser().getUsername();

                    if (commentAuthorEmail != null && !commentAuthorEmail.isBlank()) {
                        emailService.sendEmail(
                                commentAuthorEmail,
                                "Tienes una nueva respuesta en Ágora",
                                """
                                Hola %s,

                                %s ha respondido a tu comentario en el post "%s":

                                "%s"

                                Entra en Ágora para verla y contestar.

                                Equipo de Ágora
                                """.formatted(
                                        commentAuthorName,
                                        notification.getAuthor(),
                                        notification.getPostTitle(),
                                        notification.getMessage()));

                        pushNotificationService.sendPushNotification(
                                "/topics/" + commentAuthorName,
                                "Nueva respuesta",
                                notification.getAuthor() + " ha respondido a tu comentario");
                    }
                }

                // 2b. Notificar al admin
                emailService.sendEmail(
                        adminEmail,
                        "[Nueva respuesta] «" + notification.getPostTitle() + "» — " + notification.getAuthor(),
                        """
                        Hay una nueva respuesta publicada en el blog.

                        Post:    %s
                        Autor:   %s
                        Mensaje: %s
                        """.formatted(
                                notification.getPostTitle(),
                                notification.getAuthor(),
                                notification.getMessage()));
            }

        } catch (Exception e) {
            log.error("Error procesando notificación de respuesta de {}: {}", notification.getAuthor(), e.getMessage(), e);
        }
    }
}
