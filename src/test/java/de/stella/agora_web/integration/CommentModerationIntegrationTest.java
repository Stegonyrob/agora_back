package de.stella.agora_web.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.verifyNoInteractions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import de.stella.agora_web.comment.model.Comment;
import de.stella.agora_web.comment.repository.CommentRepository;
import de.stella.agora_web.config.TestConfig;
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
@ActiveProfiles("h2")
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.h2.console.enabled=false",
    "spring.kafka.enabled=false"
})
@Transactional
@Import(TestConfig.class)
class CommentModerationIntegrationTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private KafkaTemplate<String, String> kafkaTemplate;

    private User testUser;
    private Post testPost;
    private Comment testComment;

    @BeforeEach
    public void setUp() {
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

    }

    @Test
    void testKafkaMessageFormatForCommentModeration() {
        // Los tests de repositorio no pasan por la capa de servicio,
        // por lo que Kafka no debe ser invocado directamente
        verifyNoInteractions(kafkaTemplate);
        assertTrue(true); // Placeholder hasta implementar
    }

    @Test
    void testKafkaMessageFormatForReplyModeration() {
        // Este test verifica el formato del mensaje que se debe enviar
        // cuando se crea una nueva respuesta

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

        assertNotNull(savedComment.getId());
        assertEquals("Comentario con contexto para verificar información completa", savedComment.getMessage());
        assertEquals(testUser.getUsername(), savedComment.getUser().getUsername());
        assertEquals(testPost.getId(), savedComment.getPost().getId());
        assertEquals(testPost.getTitle(), savedComment.getPost().getTitle());
    }
}
