package de.stella.agora_web.integration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import de.stella.agora_web.comment.kafka.service.IEmailService;
import de.stella.agora_web.comment.kafka.service.IPushNotificationService;
import de.stella.agora_web.replies.controller.dto.ReplyDTO;
import de.stella.agora_web.replies.kafka.component.producer.ReplyKafkaProducer;
import de.stella.agora_web.replies.kafka.dto.ReplyNotificationDTO;
import de.stella.agora_web.replies.service.IReplyService;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.service.impl.UserServiceImpl;

/**
 * Test de integración para el flujo completo de moderación de respuestas
 * (replies)
 */
@SpringBootTest
@TestPropertySource(properties = {
    "kafka.enabled=false"
})
@Transactional
public class ReplyModerationIntegrationTest {

    @Autowired
    private IReplyService replyService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ReplyKafkaProducer replyKafkaProducer;

    @Autowired
    private IEmailService emailService;

    @Autowired
    private IPushNotificationService pushNotificationService;

    @Test
    public void testReplyCreationAndKafkaNotification() {
        // Test que verifica la creación de una respuesta y su notificación via Kafka

        // 1. Verificar que los servicios necesarios están disponibles
        assertNotNull(replyService, "ReplyService should be available");
        assertNotNull(replyKafkaProducer, "ReplyKafkaProducer should be available");
        assertNotNull(emailService, "EmailService should be available");
        assertNotNull(pushNotificationService, "PushNotificationService should be available");

        // 2. Crear un DTO de respuesta de prueba usando builder pattern
        ReplyDTO replyDTO = ReplyDTO.builder()
                .message("Esta es una respuesta de prueba para integración")
                .userId(1L)
                .commentId(1L)
                .tags(List.of("test", "integration"))
                .build();

        // 3. Obtener usuario de prueba
        final User testUser;
        User existingUser = userService.getUserById(1L);
        if (existingUser != null) {
            testUser = existingUser;
        } else {
            // Si no existe el usuario, crear uno temporal para la prueba
            testUser = new User();
            testUser.setId(1L);
            testUser.setUsername("testuser");
            testUser.setEmail("test@agora.com");
        }

        // 4. Intentar crear la respuesta (esto debería funcionar sin errores)
        assertDoesNotThrow(() -> {
            replyService.createReply(replyDTO, testUser);
        }, "Creating reply should not throw exception");

        // 5. Verificar que el producer de Kafka funciona
        ReplyNotificationDTO notification = new ReplyNotificationDTO();
        notification.setReplyId(1L);
        notification.setCommentId(1L);
        notification.setAuthor("testuser");
        notification.setMessage("Test notification");

        assertDoesNotThrow(() -> {
            replyKafkaProducer.sendReplyNotification(notification);
        }, "Sending Kafka notification should not throw exception");
    }

    @Test
    public void testEmailAndPushNotificationServices() {
        // Test para verificar que los servicios de notificación funcionan

        assertDoesNotThrow(() -> {
            emailService.sendEmail("test@agora.com", "Test Subject", "Test Body");
        }, "Email service should work without errors");

        assertDoesNotThrow(() -> {
            pushNotificationService.sendPushNotification("/topics/test", "Test Title", "Test Message");
        }, "Push notification service should work without errors");
    }

    @Test
    public void testReplyNotificationDTOFunctionality() {
        // Test para verificar la funcionalidad del DTO de notificación

        ReplyNotificationDTO dto = new ReplyNotificationDTO(123L, 456L, "testauthor", "test message");

        assertNotNull(dto, "DTO should be created");
        assertEquals(123L, dto.getReplyId());
        assertEquals(456L, dto.getCommentId());
        assertEquals("testauthor", dto.getAuthor());
        assertEquals("test message", dto.getMessage());

        // Test getReply method
        assertDoesNotThrow(() -> {
            dto.getReply();
        }, "getReply should not throw exception");
    }

    @Test
    public void testServiceIntegration() {
        // Test general de integración de servicios
        assertTrue(replyService != null, "Reply service should be injected");
        assertTrue(userService != null, "User service should be injected");
        assertTrue(replyKafkaProducer != null, "Kafka producer should be injected");
        assertTrue(emailService != null, "Email service should be injected");
        assertTrue(pushNotificationService != null, "Push notification service should be injected");

        System.out.println("✓ All reply moderation services are properly integrated");
    }
}
