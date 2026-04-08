package de.stella.agora_web.comment.controller;

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

import de.stella.agora_web.comment.controller.dto.CommentDTO;
import de.stella.agora_web.config.TestConfig;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.posts.repository.PostRepository;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.repository.UserRepository;

/**
 * Tests para CommentController - CRUD de comentarios con autenticación
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
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Post testPost;

    @BeforeEach
    void setUp() {
        // Crear usuario de prueba
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("hashedpassword");
        testUser = userRepository.save(testUser);

        // Crear post de prueba
        testPost = new Post();
        testPost.setTitle("Test Post");
        testPost.setMessage("Test content");
        testPost.setUser(testUser);
        testPost = postRepository.save(testPost);
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testCreateComment_ShouldSucceedWithValidData() throws Exception {
        CommentDTO commentDTO = CommentDTO.builder()
                .message("This is a test comment")
                .postId(testPost.getId())
                .build();

        mockMvc.perform(post("/api/comments/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("This is a test comment")))
                .andExpect(jsonPath("$.postId", is(testPost.getId().intValue())));
    }

    @Test
    void testCreateComment_ShouldRequireAuthentication() throws Exception {
        CommentDTO commentDTO = CommentDTO.builder()
                .message("This is a test comment")
                .postId(testPost.getId())
                .build();

        mockMvc.perform(post("/api/comments/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testCreateComment_ShouldRejectEmptyContent() throws Exception {
        CommentDTO commentDTO = CommentDTO.builder()
                .message("")
                .postId(testPost.getId())
                .build();

        mockMvc.perform(post("/api/comments/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testGetCommentsWithReplies_ShouldReturnCommentsForPost() throws Exception {
        mockMvc.perform(get("/api/comments/post/{postId}/with-replies", testPost.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testUpdateComment_ShouldSucceedWithValidData() throws Exception {
        // Primero crear un comentario
        CommentDTO createDTO = CommentDTO.builder()
                .message("Original comment")
                .postId(testPost.getId())
                .build();

        mockMvc.perform(post("/api/comments/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        // Para el update, necesitamos usar el endpoint con ID en path
        CommentDTO updateDTO = CommentDTO.builder()
                .message("Updated comment content")
                .build();

        // Como el DTO no tiene ID, usaremos un ID de prueba
        Long commentId = 1L; // Asumimos que el primer comentario tiene ID 1

        mockMvc.perform(put("/api/comments/{id}", commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testDeleteComment_ShouldSucceedWhenOwner() throws Exception {
        // Primero crear un comentario
        CommentDTO createDTO = CommentDTO.builder()
                .message("Comment to delete")
                .postId(testPost.getId())
                .build();

        mockMvc.perform(post("/api/comments/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Usar un ID de prueba para eliminar
        Long commentId = 1L;

        // Eliminar el comentario
        mockMvc.perform(delete("/api/comments/{id}", commentId))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteComment_ShouldRequireAuthentication() throws Exception {
        mockMvc.perform(delete("/api/comments/{id}", 999L))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testCreateComment_ShouldRejectInvalidPostId() throws Exception {
        CommentDTO commentDTO = CommentDTO.builder()
                .message("Valid comment content")
                .postId(99999L) // Non-existent post ID
                .build();

        mockMvc.perform(post("/api/comments/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDTO)))
                .andExpect(status().isNotFound());
    }
}
