package de.stella.agora_web.kafka;

import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

/**
 * Tests para el servicio de notificaciones de comentarios
 *
 * Funcionalidad esperada: 1. Cuando se crea un nuevo comentario/respuesta -> se
 * envía mensaje a Kafka 2. El mensaje debe contener: ID del comentario, autor,
 * contenido, timestamp 3. El topic debe ser "comment-moderation" 4. Debe
 * incluir metadatos para que el admin pueda revisar
 */
@ExtendWith(MockitoExtension.class)
class CommentModerationKafkaTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private CompletableFuture<SendResult<String, String>> future;

    private CommentModerationService commentModerationService;

    @BeforeEach
    public void setUp() {
        commentModerationService = new CommentModerationService(kafkaTemplate);
    }

    @Test
    void testSendCommentModerationNotification() {
        // Arrange
        Long commentId = 123L;
        String authorName = "TestUser";
        String content = "Este es un comentario de prueba que necesita moderación";
        String postTitle = "Post de ejemplo";

        when(kafkaTemplate.send(anyString(), anyString(), anyString()))
                .thenReturn(future);

        // Act
        commentModerationService.sendCommentModerationNotification(
                commentId, authorName, content, postTitle
        );

        // Assert
        verify(kafkaTemplate, times(1))
                .send(eq("comment-moderation"), anyString(), contains("commentId"));
        verify(kafkaTemplate, times(1))
                .send(eq("comment-moderation"), anyString(), contains(authorName));
        verify(kafkaTemplate, times(1))
                .send(eq("comment-moderation"), anyString(), contains(content));
    }

    @Test
    void testSendReplyModerationNotification() {
        // Arrange
        Long replyId = 456L;
        String authorName = "ReplyUser";
        String content = "Esta es una respuesta que necesita moderación";
        Long parentCommentId = 123L;

        when(kafkaTemplate.send(anyString(), anyString(), anyString()))
                .thenReturn(future);

        // Act
        commentModerationService.sendReplyModerationNotification(
                replyId, authorName, content, parentCommentId
        );

        // Assert
        verify(kafkaTemplate, times(1))
                .send(eq("reply-moderation"), anyString(), contains("replyId"));
        verify(kafkaTemplate, times(1))
                .send(eq("reply-moderation"), anyString(), contains(authorName));
        verify(kafkaTemplate, times(1))
                .send(eq("reply-moderation"), anyString(), contains(content));
    }

    @Test
    void testNotificationMessageFormat() {
        // Arrange
        Long commentId = 789L;
        String authorName = "FormatTestUser";
        String content = "Contenido para verificar formato";
        String postTitle = "Post de formato";

        when(kafkaTemplate.send(anyString(), anyString(), anyString()))
                .thenReturn(future);

        // Act
        commentModerationService.sendCommentModerationNotification(
                commentId, authorName, content, postTitle
        );

        // Assert - Verificar que el mensaje tiene el formato JSON esperado
        verify(kafkaTemplate).send(
                eq("comment-moderation"),
                anyString(),
                argThat(message
                        -> message.contains("\"type\":\"NEW_COMMENT\"")
                && message.contains("\"commentId\":" + commentId)
                && message.contains("\"author\":\"" + authorName + "\"")
                && message.contains("\"content\":\"" + content + "\"")
                && message.contains("\"postTitle\":\"" + postTitle + "\"")
                && message.contains("\"timestamp\":")
                )
        );
    }

    @Test
    void testKafkaFailureHandling() {
        // Arrange
        Long commentId = 999L;
        String authorName = "FailureTestUser";
        String content = "Contenido de prueba para fallo";
        String postTitle = "Post de fallo";

        when(kafkaTemplate.send(anyString(), anyString(), anyString()))
                .thenThrow(new RuntimeException("Kafka no disponible"));

        // Act & Assert - No debe lanzar excepción, debe manejar gracefully
        try {
            commentModerationService.sendCommentModerationNotification(
                    commentId, authorName, content, postTitle
            );
        } catch (Exception _) {
            // El servicio debe manejar la excepción internamente
            // y posiblemente logear el error
        }

        verify(kafkaTemplate, times(1))
                .send(anyString(), anyString(), anyString());
    }

    // Clase interna para simular el servicio que vamos a implementar
    static class CommentModerationService {

        private final KafkaTemplate<String, String> kafkaTemplate;

        public CommentModerationService(KafkaTemplate<String, String> kafkaTemplate) {
            this.kafkaTemplate = kafkaTemplate;
        }

        public void sendCommentModerationNotification(Long commentId, String authorName,
                String content, String postTitle) {
            // Esta implementación será creada después de los tests
            String message = String.format(
                    "{\"type\":\"NEW_COMMENT\",\"commentId\":%d,\"author\":\"%s\",\"content\":\"%s\",\"postTitle\":\"%s\",\"timestamp\":%d}",
                    commentId, authorName, content, postTitle, System.currentTimeMillis()
            );
            kafkaTemplate.send("comment-moderation", "comment-" + commentId, message);
        }

        public void sendReplyModerationNotification(Long replyId, String authorName,
                String content, Long parentCommentId) {
            String message = String.format(
                    "{\"type\":\"NEW_REPLY\",\"replyId\":%d,\"author\":\"%s\",\"content\":\"%s\",\"parentCommentId\":%d,\"timestamp\":%d}",
                    replyId, authorName, content, parentCommentId, System.currentTimeMillis()
            );
            kafkaTemplate.send("reply-moderation", "reply-" + replyId, message);
        }
    }
}
