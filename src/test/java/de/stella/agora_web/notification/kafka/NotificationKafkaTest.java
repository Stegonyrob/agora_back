package de.stella.agora_web.notification.kafka;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import de.stella.agora_web.comment.kafka.dto.CommentNotificationDTO;
import de.stella.agora_web.notification.kafka.consumer.NotificationKafkaConsumer;
import de.stella.agora_web.notification.service.EmailNotificationService;
import de.stella.agora_web.replies.kafka.dto.ReplyNotificationDTO;

/**
 * Tests del sistema de notificaciones por email via Kafka Verifica que cuando
 * se crean comentarios/replies, se envíen notificaciones al admin
 */
@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {
    "kafka.enabled=false", // Para evitar problemas en tests
    "admin.email=admin@gmail.com"
})
@DisplayName("🔔 Sistema de Notificaciones Kafka → Email")
class NotificationKafkaTest {

    @Mock
    private EmailNotificationService emailNotificationService;

    @InjectMocks
    private NotificationKafkaConsumer notificationKafkaConsumer;

    private CommentNotificationDTO commentNotification;
    private ReplyNotificationDTO replyNotification;

    @BeforeEach
    void setUp() {
        // Preparar notificación de comentario de prueba
        commentNotification = new CommentNotificationDTO();
        commentNotification.setCommentId(1L);
        commentNotification.setPostId(100L);
        commentNotification.setPostTitle("¿Cómo mejorar la educación en España?");
        commentNotification.setUserName("maria_estudiante");
        commentNotification.setCommentContent("Creo que deberíamos invertir más en tecnología educativa y formación del profesorado.");
        commentNotification.setCreatedAt(LocalDateTime.now());

        // Preparar notificación de respuesta de prueba
        replyNotification = new ReplyNotificationDTO();
        replyNotification.setReplyId(1L);
        replyNotification.setCommentId(1L);
        replyNotification.setPostId(100L);
        replyNotification.setPostTitle("¿Cómo mejorar la educación en España?");
        replyNotification.setUserName("juan_profesor");
        replyNotification.setReplyContent("Estoy de acuerdo, además necesitamos reducir el ratio profesor-alumno.");
        replyNotification.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("📨 Debe procesar notificación de comentario y enviar email al admin")
    void testHandleCommentNotification_ShouldSendEmailToAdmin() {
        // When - El consumer procesa la notificación de comentario
        notificationKafkaConsumer.handleCommentNotification(commentNotification);

        // Then - Debe llamar al servicio de email con la notificación
        verify(emailNotificationService, times(1))
                .sendCommentNotification(eq(commentNotification));

        // Verificar que la notificación tiene todos los datos necesarios
        assertNotNull(commentNotification.getCommentId(), "ID del comentario no debe ser null");
        assertNotNull(commentNotification.getPostId(), "ID del post no debe ser null");
        assertNotNull(commentNotification.getPostTitle(), "Título del post no debe ser null");
        assertNotNull(commentNotification.getUserName(), "Nombre del usuario no debe ser null");
        assertNotNull(commentNotification.getCommentContent(), "Contenido del comentario no debe ser null");
        assertNotNull(commentNotification.getCreatedAt(), "Fecha de creación no debe ser null");
    }

    @Test
    @DisplayName("📨 Debe procesar notificación de respuesta y enviar email al admin")
    void testHandleReplyNotification_ShouldSendEmailToAdmin() {
        // When - El consumer procesa la notificación de respuesta
        notificationKafkaConsumer.handleReplyNotification(replyNotification);

        // Then - Debe llamar al servicio de email con la notificación
        verify(emailNotificationService, times(1))
                .sendReplyNotification(eq(replyNotification));

        // Verificar que la notificación tiene todos los datos necesarios
        assertNotNull(replyNotification.getReplyId(), "ID de la respuesta no debe ser null");
        assertNotNull(replyNotification.getCommentId(), "ID del comentario no debe ser null");
        assertNotNull(replyNotification.getPostId(), "ID del post no debe ser null");
        assertNotNull(replyNotification.getPostTitle(), "Título del post no debe ser null");
        assertNotNull(replyNotification.getUserName(), "Nombre del usuario no debe ser null");
        assertNotNull(replyNotification.getReplyContent(), "Contenido de la respuesta no debe ser null");
        assertNotNull(replyNotification.getCreatedAt(), "Fecha de creación no debe ser null");
    }

    @Test
    @DisplayName("🔧 Debe manejar errores del servicio de email gracefully")
    void testHandleNotifications_ShouldHandleEmailServiceErrors() {
        // Given - El servicio de email lanza excepción
        doThrow(new RuntimeException("Error enviando email"))
                .when(emailNotificationService).sendCommentNotification(any());

        // When & Then - No debe propagarse la excepción
        assertDoesNotThrow(() -> {
            notificationKafkaConsumer.handleCommentNotification(commentNotification);
        });

        // Verificar que se intentó enviar el email
        verify(emailNotificationService, times(1))
                .sendCommentNotification(eq(commentNotification));
    }

    @Test
    @DisplayName("📊 DTO de comentario debe tener formato toString correcto")
    void testCommentNotificationDTO_ToString() {
        String result = commentNotification.toString();

        assertTrue(result.contains("CommentNotificationDTO"), "ToString debe incluir el nombre de la clase");
        assertTrue(result.contains("commentId=1"), "ToString debe incluir el ID del comentario");
        assertTrue(result.contains("postId=100"), "ToString debe incluir el ID del post");
        assertTrue(result.contains("maria_estudiante"), "ToString debe incluir el nombre del usuario");
        assertTrue(result.contains("¿Cómo mejorar la educación en España?"), "ToString debe incluir el título del post");
    }

    @Test
    @DisplayName("📊 DTO de respuesta debe tener formato toString correcto")
    void testReplyNotificationDTO_ToString() {
        String result = replyNotification.toString();

        assertTrue(result.contains("ReplyNotificationDTO"), "ToString debe incluir el nombre de la clase");
        assertTrue(result.contains("replyId=1"), "ToString debe incluir el ID de la respuesta");
        assertTrue(result.contains("commentId=1"), "ToString debe incluir el ID del comentario");
        assertTrue(result.contains("postId=100"), "ToString debe incluir el ID del post");
        assertTrue(result.contains("juan_profesor"), "ToString debe incluir el nombre del usuario");
        assertTrue(result.contains("¿Cómo mejorar la educación en España?"), "ToString debe incluir el título del post");
    }

    @Test
    @DisplayName("🏗️ DTOs deben construirse correctamente con constructor completo")
    void testNotificationDTOs_FullConstructor() {
        LocalDateTime now = LocalDateTime.now();

        // Test CommentNotificationDTO
        CommentNotificationDTO comment = new CommentNotificationDTO(
                1L, 100L, "Test Post", "testuser", "Test comment", now
        );

        assertEquals(1L, comment.getCommentId());
        assertEquals(100L, comment.getPostId());
        assertEquals("Test Post", comment.getPostTitle());
        assertEquals("testuser", comment.getUserName());
        assertEquals("Test comment", comment.getCommentContent());
        assertEquals(now, comment.getCreatedAt());

        // Test ReplyNotificationDTO
        ReplyNotificationDTO reply = new ReplyNotificationDTO(
                1L, 1L, 100L, "Test Post", "testuser", "Test reply", now
        );

        assertEquals(1L, reply.getReplyId());
        assertEquals(1L, reply.getCommentId());
        assertEquals(100L, reply.getPostId());
        assertEquals("Test Post", reply.getPostTitle());
        assertEquals("testuser", reply.getUserName());
        assertEquals("Test reply", reply.getReplyContent());
        assertEquals(now, reply.getCreatedAt());
    }
}
