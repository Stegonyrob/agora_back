package de.stella.agora_web.moderation.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.stella.agora_web.comment.model.Comment;
import de.stella.agora_web.comment.repository.CommentRepository;
import de.stella.agora_web.config.TestConfig;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.posts.repository.PostRepository;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.repository.UserRepository;

/**
 * Tests para ModerationController - Sistema de moderación de contenido
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
@TestPropertySource(properties = {
    "spring.h2.console.enabled=false",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Import(TestConfig.class)
@Transactional
public class ModerationControllerTest {

    @org.springframework.beans.factory.annotation.Value("${api-endpoint}")
    private String apiEndpoint;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    private User testUser;
    private Post testPost;
    private Comment testComment;

    @BeforeEach
    void setUp() {
        // Limpiar repositorios
        commentRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();

        // Crear usuario de prueba
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser = userRepository.save(testUser);

        // Crear post de prueba
        testPost = new Post();
        testPost.setTitle("Post de Prueba");
        testPost.setMessage("Contenido del post de prueba");
        testPost.setUser(testUser);
        testPost = postRepository.save(testPost);

        // Crear comentario de prueba
        testComment = new Comment();
        testComment.setMessage("Comentario de prueba");
        testComment.setUser(testUser);
        testComment.setPost(testPost);
        testComment = commentRepository.save(testComment);
    }

    // ============ TESTS MODERATE COMMENT ============
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testModerateComment_WithSafeContent_ShouldReturnNull() throws Exception {
        // Comentario con contenido seguro
        testComment.setMessage("Este es un comentario muy educativo y respetuoso");
        commentRepository.save(testComment);

        mockMvc.perform(post(apiEndpoint + "/moderation/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testComment)))
                .andExpect(status().isOk())
                .andExpect(content().string(""));  // Null response body para contenido seguro
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testModerateComment_WithOffensiveContent_ShouldReturnCensuredComment() throws Exception {
        // Comentario con contenido ofensivo
        testComment.setMessage("Eres un estúpido idiota");
        commentRepository.save(testComment);

        mockMvc.perform(post(apiEndpoint + "/moderation/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testComment)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId", is(testUser.getId().intValue())))
                .andExpect(jsonPath("$.reason", is("Contenido ofensivo detectado")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testModerateComment_WithOffensiveWords_ShouldDetectAndCensor() throws Exception {
        String[] offensiveWords = {
            "estúpido",
            "idiota",
            "imbécil",
            "tonto",
            "mierda"
        };

        for (String word : offensiveWords) {
            testComment.setMessage("Este comentario contiene la palabra " + word);

            mockMvc.perform(post(apiEndpoint + "/moderation/comment")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testComment)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.reason", is("Contenido ofensivo detectado")));
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testModerateComment_WithEdgeCases_ShouldNotCensor() throws Exception {
        String[] edgeCases = {
            "matar el tiempo", // Expresión idiomática válida
            "estoy muerto de cansancio", // Expresión idiomática válida
            "esto es malo", // Opinión negativa legítima
            "no me gusta" // Opinión personal válida
        };

        for (String phrase : edgeCases) {
            testComment.setMessage(phrase);

            mockMvc.perform(post(apiEndpoint + "/moderation/comment")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testComment)))
                    .andExpect(status().isOk())
                    .andExpect(content().string(""));  // No debería censurar
        }
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testModerateComment_WithUserRole_ShouldReturn403() throws Exception {
        mockMvc.perform(post(apiEndpoint + "/moderation/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testComment)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testModerateComment_WithoutAuthentication_ShouldReturn401() throws Exception {
        mockMvc.perform(post(apiEndpoint + "/moderation/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testComment)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testModerateComment_WithNullComment_ShouldReturn400() throws Exception {
        mockMvc.perform(post(apiEndpoint + "/moderation/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testModerateComment_WithEmptyMessage_ShouldReturn400() throws Exception {
        testComment.setMessage("");

        mockMvc.perform(post(apiEndpoint + "/moderation/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testComment)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testModerateComment_WithNullMessage_ShouldReturn400() throws Exception {
        testComment.setMessage(null);

        mockMvc.perform(post(apiEndpoint + "/moderation/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testComment)))
                .andExpect(status().isBadRequest());
    }

    // ============ TESTS MULTIPLE MODERATION SCENARIOS ============
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testModerateComment_WithMixedContent_ShouldHandleCorrectly() throws Exception {
        // Test con contenido mixto (algunas palabras ofensivas en contexto)
        testComment.setMessage("Aunque algunos pueden pensar que es tonto, yo creo que es educativo");

        mockMvc.perform(post(apiEndpoint + "/moderation/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testComment)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.reason", is("Contenido ofensivo detectado")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testModerateComment_WithLongText_ShouldProcess() throws Exception {
        // Test con texto largo
        StringBuilder longMessage = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            longMessage.append("Este es un texto muy largo y repetitivo que debería ser procesado correctamente. ");
        }

        testComment.setMessage(longMessage.toString());

        mockMvc.perform(post(apiEndpoint + "/moderation/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testComment)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testModerateComment_WithSpecialCharacters_ShouldProcess() throws Exception {
        // Test con caracteres especiales
        testComment.setMessage("¡Hola! ¿Cómo estás? Muy bien, gracias. ñáéíóú @#$%^&*()");

        mockMvc.perform(post(apiEndpoint + "/moderation/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testComment)))
                .andExpect(status().isOk())
                .andExpect(content().string(""));  // No debería censurar
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testModerateComment_WithUppercaseOffensive_ShouldDetect() throws Exception {
        // Test con palabras ofensivas en mayúsculas
        testComment.setMessage("ERES UN ESTÚPIDO");

        mockMvc.perform(post(apiEndpoint + "/moderation/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testComment)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.reason", is("Contenido ofensivo detectado")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testModerateComment_WithCombinedOffensive_ShouldDetect() throws Exception {
        // Test con múltiples palabras ofensivas
        testComment.setMessage("Eres un idiota estúpido");

        mockMvc.perform(post(apiEndpoint + "/moderation/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testComment)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.reason", is("Contenido ofensivo detectado")));
    }
}
