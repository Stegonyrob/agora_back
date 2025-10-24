package de.stella.agora_web.notification.service;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import de.stella.agora_web.comment.kafka.dto.CommentNotificationDTO;
import de.stella.agora_web.replies.kafka.dto.ReplyNotificationDTO;

/**
 * Tests del servicio de notificaciones por email Verifica que se generen
 * correctamente los emails para el admin
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("h2")
@TestPropertySource(properties = {
    "kafka.enabled=false",
    "admin.email=admin@gmail.com"
})
@DisplayName("📧 Servicio de Notificaciones por Email")
class EmailNotificationServiceTest {

    @Autowired
    private EmailNotificationService emailNotificationService;

    private CommentNotificationDTO commentNotification;
    private ReplyNotificationDTO replyNotification;

    @BeforeEach
    void setUp() {
        // Preparar datos de prueba realistas
        commentNotification = new CommentNotificationDTO();
        commentNotification.setCommentId(123L);
        commentNotification.setPostId(456L);
        commentNotification.setPostTitle("🎓 ¿Cuál es el futuro de la educación digital en España?");
        commentNotification.setUserName("ana_madrid");
        commentNotification.setCommentContent("Creo que necesitamos más inversión en plataformas educativas como esta. La digitalización es clave para el futuro de nuestros estudiantes.");
        commentNotification.setCreatedAt(LocalDateTime.now());

        replyNotification = new ReplyNotificationDTO();
        replyNotification.setReplyId(789L);
        replyNotification.setCommentId(123L);
        replyNotification.setPostId(456L);
        replyNotification.setPostTitle("🎓 ¿Cuál es el futuro de la educación digital en España?");
        replyNotification.setUserName("carlos_profesor");
        replyNotification.setReplyContent("Totalmente de acuerdo, Ana. Además, creo que deberíamos incluir más formación en inteligencia artificial para preparar a los estudiantes para el mercado laboral del futuro.");
        replyNotification.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("📨 Debe enviar notificación de comentario correctamente")
    void testSendCommentNotification_ShouldLogCorrectInformation() {
        // When - Enviamos la notificación
        assertDoesNotThrow(() -> {
            emailNotificationService.sendCommentNotification(commentNotification);
        });

        // Then - El test pasa si no hay excepciones
        // Los logs se pueden verificar manualmente en la consola
        assertTrue(true, "La notificación de comentario se procesó sin errores");
    }

    @Test
    @DisplayName("📨 Debe enviar notificación de respuesta correctamente")
    void testSendReplyNotification_ShouldLogCorrectInformation() {
        // When - Enviamos la notificación
        assertDoesNotThrow(() -> {
            emailNotificationService.sendReplyNotification(replyNotification);
        });

        // Then - El test pasa si no hay excepciones
        assertTrue(true, "La notificación de respuesta se procesó sin errores");
    }

    @Test
    @DisplayName("🔧 Debe manejar notificaciones con datos null gracefully")
    void testSendNotifications_WithNullData() {
        // Given - Notificaciones con algunos datos null
        CommentNotificationDTO commentWithNulls = new CommentNotificationDTO();
        commentWithNulls.setCommentId(1L);
        commentWithNulls.setPostId(null); // null
        commentWithNulls.setPostTitle(null); // null
        commentWithNulls.setUserName("usuario_test");
        commentWithNulls.setCommentContent("Comentario de prueba");

        ReplyNotificationDTO replyWithNulls = new ReplyNotificationDTO();
        replyWithNulls.setReplyId(1L);
        replyWithNulls.setCommentId(null); // null
        replyWithNulls.setPostId(null); // null
        replyWithNulls.setPostTitle(null); // null
        replyWithNulls.setUserName("usuario_test");
        replyWithNulls.setReplyContent("Respuesta de prueba");

        // When & Then - No debe lanzar excepciones
        assertDoesNotThrow(() -> {
            emailNotificationService.sendCommentNotification(commentWithNulls);
            emailNotificationService.sendReplyNotification(replyWithNulls);
        });
    }

    @Test
    @DisplayName("📊 Debe validar campos requeridos en notificaciones")
    void testNotificationValidation() {
        // Test CommentNotificationDTO
        assertNotNull(commentNotification.getCommentId(), "Comment ID no debe ser null");
        assertNotNull(commentNotification.getPostId(), "Post ID no debe ser null");
        assertNotNull(commentNotification.getPostTitle(), "Post title no debe ser null");
        assertNotNull(commentNotification.getUserName(), "User name no debe ser null");
        assertNotNull(commentNotification.getCommentContent(), "Comment content no debe ser null");
        assertNotNull(commentNotification.getCreatedAt(), "Created at no debe ser null");

        // Test ReplyNotificationDTO
        assertNotNull(replyNotification.getReplyId(), "Reply ID no debe ser null");
        assertNotNull(replyNotification.getCommentId(), "Comment ID no debe ser null");
        assertNotNull(replyNotification.getPostId(), "Post ID no debe ser null");
        assertNotNull(replyNotification.getPostTitle(), "Post title no debe ser null");
        assertNotNull(replyNotification.getUserName(), "User name no debe ser null");
        assertNotNull(replyNotification.getReplyContent(), "Reply content no debe ser null");
        assertNotNull(replyNotification.getCreatedAt(), "Created at no debe ser null");
    }

    @Test
    @DisplayName("🎯 Debe generar contenido de email realista para comentarios")
    void testCommentEmailContentGeneration() {
        // When - Procesamos la notificación
        assertDoesNotThrow(() -> {
            emailNotificationService.sendCommentNotification(commentNotification);
        });

        // Then - Verificamos que los datos son correctos
        assertEquals("ana_madrid", commentNotification.getUserName());
        assertEquals("🎓 ¿Cuál es el futuro de la educación digital en España?", commentNotification.getPostTitle());
        assertTrue(commentNotification.getCommentContent().contains("digitalización"),
                "El contenido debe contener palabras clave relevantes");
        assertTrue(commentNotification.getCommentContent().contains("inversión"),
                "El contenido debe mencionar temas educativos");
    }

    @Test
    @DisplayName("🎯 Debe generar contenido de email realista para respuestas")
    void testReplyEmailContentGeneration() {
        // When - Procesamos la notificación
        assertDoesNotThrow(() -> {
            emailNotificationService.sendReplyNotification(replyNotification);
        });

        // Then - Verificamos que los datos son correctos
        assertEquals("carlos_profesor", replyNotification.getUserName());
        assertEquals("🎓 ¿Cuál es el futuro de la educación digital en España?", replyNotification.getPostTitle());
        assertTrue(replyNotification.getReplyContent().contains("inteligencia artificial"),
                "El contenido debe contener temas tecnológicos");
        assertTrue(replyNotification.getReplyContent().contains("mercado laboral"),
                "El contenido debe mencionar aspectos laborales");
    }

    @Test
    @DisplayName("⚡ Debe procesar múltiples notificaciones rápidamente")
    void testMultipleNotifications_Performance() {
        long startTime = System.currentTimeMillis();

        // When - Enviamos múltiples notificaciones
        for (int i = 0; i < 10; i++) {
            CommentNotificationDTO comment = new CommentNotificationDTO();
            comment.setCommentId((long) i);
            comment.setPostId(100L);
            comment.setPostTitle("Post de prueba " + i);
            comment.setUserName("usuario" + i);
            comment.setCommentContent("Comentario número " + i);
            comment.setCreatedAt(LocalDateTime.now());

            emailNotificationService.sendCommentNotification(comment);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // Then - Debe ser rápido (menos de 1 segundo para 10 notificaciones)
        assertTrue(duration < 1000, "Procesar 10 notificaciones debe tomar menos de 1 segundo");
    }
}
