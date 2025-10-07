package de.stella.agora_web.kafka;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import de.stella.agora_web.replies.kafka.component.producer.ReplyKafkaProducer;
import de.stella.agora_web.replies.kafka.dto.ReplyNotificationDTO;

/**
 * Test para verificar la configuración de Kafka para replies
 */
@SpringBootTest
@ActiveProfiles("h2")
@TestPropertySource(properties = {
    "kafka.enabled=false",
    "spring.h2.console.enabled=false",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class ReplyModerationKafkaTest {

    // Nota: Este test funcionará tanto con Kafka habilitado como deshabilitado
    // Cuando está deshabilitado, usa el bean dummy que no hace nada
    @Autowired
    private ReplyKafkaProducer replyKafkaProducer;

    @Test
    public void testReplyKafkaProducerExists() {
        // Verificar que el bean existe (real o dummy)
        assertNotNull(replyKafkaProducer, "ReplyKafkaProducer bean should exist");
    }

    @Test
    public void testReplyNotificationSending() {
        // Crear una notificación de prueba
        ReplyNotificationDTO notification = new ReplyNotificationDTO();
        notification.setReplyId(1L);
        notification.setCommentId(1L);
        notification.setAuthor("testuser");
        notification.setMessage("Test reply message");

        // Intentar enviar la notificación
        // Esto no fallará independientemente de si Kafka está habilitado o no
        assertDoesNotThrow(() -> {
            replyKafkaProducer.sendReplyNotification(notification);
        }, "Sending reply notification should not throw exception");
    }

    @Test
    public void testReplyNotificationDTOValidation() {
        ReplyNotificationDTO notification = new ReplyNotificationDTO();

        // Test constructor vacío
        assertNotNull(notification, "ReplyNotificationDTO should be instantiable");

        // Test setters y getters
        notification.setReplyId(123L);
        notification.setCommentId(456L);
        notification.setAuthor("testauthor");
        notification.setMessage("Test message");

        assertEquals(123L, notification.getReplyId());
        assertEquals(456L, notification.getCommentId());
        assertEquals("testauthor", notification.getAuthor());
        assertEquals("Test message", notification.getMessage());
    }

    @Test
    public void testReplyNotificationDTOConstructor() {
        ReplyNotificationDTO notification = new ReplyNotificationDTO(1L, 2L, "author", "message");

        assertEquals(1L, notification.getReplyId());
        assertEquals(2L, notification.getCommentId());
        assertEquals("author", notification.getAuthor());
        assertEquals("message", notification.getMessage());
    }
}
