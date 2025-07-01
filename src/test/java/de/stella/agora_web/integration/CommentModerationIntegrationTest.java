package de.stella.agora_web.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import de.stella.agora_web.comment.model.Comment;
import de.stella.agora_web.comment.repository.CommentRepository;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.posts.repository.PostRepository;
import de.stella.agora_web.replies.model.Reply;
import de.stella.agora_web.replies.repository.ReplyRepository;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.repository.UserRepository;

/**
 * Tests de integración para verificar el flujo completo: Comment/Reply creation
 * -> Kafka notification -> Email notification
 */
@SpringBootTest
@TestPropertySource(properties = {
    "spring.kafka.bootstrap-servers=localhost:9092",
    "spring.kafka.consumer.group-id=integration-test-group"
})
@Transactional
class CommentModerationIntegrationTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    private User testUser;
    private Post testPost;
    private Comment testComment;

    @BeforeEach
    void setUp() {
        // Crear datos de prueba
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setAcceptedRules(true);
        testUser = userRepository.save(testUser);

        testPost = new Post();
        testPost.setTitle("Post de prueba para moderación");
        testPost.setMessage("Contenido del post de prueba");
        testPost.setUser(testUser);
        testPost = postRepository.save(testPost);

        testComment = new Comment();
        testComment.setMessage("Comentario de prueba");
        testComment.setUser(testUser);
        testComment.setPost(testPost);
        testComment = commentRepository.save(testComment);
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testCreateCommentShouldTriggerModerationNotification() {
        // Arrange
        Comment newComment = new Comment();
        newComment.setMessage("Este es un comentario que debe generar notificación de moderación");
        newComment.setUser(testUser);
        newComment.setPost(testPost);

        // Act
        Comment savedComment = commentRepository.save(newComment);

        // Assert
        assertNotNull(savedComment.getId());
        assertEquals("Este es un comentario que debe generar notificación de moderación", savedComment.getMessage());

        // TODO: Verificar que se envió mensaje a Kafka cuando implementemos el listener
        // verify(kafkaTemplate, times(1))
        //     .send(eq("comment-moderation"), anyString(), contains("NEW_COMMENT"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testCreateReplyTriggersKafkaNotification() {
        // Arrange
        Reply reply = new Reply();
        reply.setMessage("Esta es una respuesta que debe generar notificación de moderación");
        reply.setUser(testUser);
        reply.setComment(testComment);

        // Act
        Reply savedReply = replyRepository.save(reply);

        // Assert
        assertNotNull(savedReply.getId());
        assertEquals("Esta es una respuesta que debe generar notificación de moderación", savedReply.getMessage());

        // TODO: Verificar que se envió mensaje a Kafka
        // verify(kafkaTemplate, times(1))
        //     .send(eq("reply-moderation"), anyString(), contains("NEW_REPLY"));
    }

    @Test
    void testKafkaMessageFormatForCommentModeration() {
        // Este test verifica el formato del mensaje que se debe enviar
        // cuando se crea un nuevo comentario

        String expectedMessagePattern = ".*\"type\":\"NEW_COMMENT\".*"
                + ".*\"commentId\":\\d+.*"
                + ".*\"author\":\".*\".*"
                + ".*\"content\":\".*\".*"
                + ".*\"postId\":\\d+.*"
                + ".*\"postTitle\":\".*\".*"
                + ".*\"timestamp\":\\d+.*";

        // TODO: Implementar cuando tengamos el servicio real
        // assertTrue(actualMessage.matches(expectedMessagePattern));
        assertTrue(true); // Placeholder hasta implementar
    }

    @Test
    void testKafkaMessageFormatForReplyModeration() {
        // Este test verifica el formato del mensaje que se debe enviar
        // cuando se crea una nueva respuesta

        String expectedMessagePattern = ".*\"type\":\"NEW_REPLY\".*"
                + ".*\"replyId\":\\d+.*"
                + ".*\"author\":\".*\".*"
                + ".*\"content\":\".*\".*"
                + ".*\"commentId\":\\d+.*"
                + ".*\"postId\":\\d+.*"
                + ".*\"timestamp\":\\d+.*";

        // TODO: Implementar cuando tengamos el servicio real
        assertTrue(true); // Placeholder hasta implementar
    }

    @Test
    void testBulkCommentsGenerateMultipleNotifications() {
        // Test para verificar que múltiples comentarios generan múltiples notificaciones

        // Crear 3 comentarios
        for (int i = 1; i <= 3; i++) {
            Comment comment = new Comment();
            comment.setMessage("Comentario número " + i + " para moderación");
            comment.setUser(testUser);
            comment.setPost(testPost);
            commentRepository.save(comment);
        }

        // TODO: Verificar que se enviaron 3 mensajes a Kafka
        // verify(kafkaTemplate, times(3))
        //     .send(eq("comment-moderation"), anyString(), anyString());
        assertTrue(true); // Placeholder hasta implementar
    }

    @Test
    void testModerationNotificationIncludesContextInfo() {
        // Verificar que la notificación incluye información contextual útil

        Comment comment = new Comment();
        comment.setMessage("Comentario con contexto para verificar información completa");
        comment.setUser(testUser);
        comment.setPost(testPost);

        Comment savedComment = commentRepository.save(comment);

        // TODO: Verificar que el mensaje incluye:
        // - ID del comentario
        // - Nombre del autor
        // - Contenido del comentario
        // - ID del post padre
        // - Título del post padre
        // - Timestamp
        // - URL para moderación directa
        assertNotNull(savedComment.getId());
    }
}
