package de.stella.agora_web.replies.controller;

import static org.hamcrest.Matchers.is;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.stella.agora_web.comment.model.Comment;
import de.stella.agora_web.comment.repository.CommentRepository;
import de.stella.agora_web.config.TestConfig;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.posts.repository.PostRepository;
import de.stella.agora_web.replies.controller.dto.ReplyDTO;
import de.stella.agora_web.replies.repository.ReplyRepository;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.repository.UserRepository;

/**
 * Tests para ReplyController - CRUD de replies con autenticación
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
public class ReplyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Post testPost;
    private Comment testComment;

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

        // Crear comentario de prueba
        testComment = new Comment();
        testComment.setMessage("Test comment");
        testComment.setPost(testPost);
        testComment.setUser(testUser);
        testComment = commentRepository.save(testComment);
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testCreateReply_ShouldSucceedWithValidData() throws Exception {
        ReplyDTO replyDTO = new ReplyDTO();
        replyDTO.setMessage("This is a test reply");
        replyDTO.setCommentId(testComment.getId());

        mockMvc.perform(post("/api/replies/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(replyDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("This is a test reply")))
                .andExpect(jsonPath("$.commentId", is(testComment.getId().intValue())));
    }

    @Test
    void testCreateReply_ShouldRequireAuthentication() throws Exception {
        ReplyDTO replyDTO = new ReplyDTO();
        replyDTO.setMessage("This is a test reply");
        replyDTO.setCommentId(testComment.getId());

        mockMvc.perform(post("/api/replies/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(replyDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testCreateReply_ShouldRejectEmptyMessage() throws Exception {
        ReplyDTO replyDTO = new ReplyDTO();
        replyDTO.setMessage("");
        replyDTO.setCommentId(testComment.getId());

        mockMvc.perform(post("/api/replies/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(replyDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testGetReplyById_ShouldReturnReply() throws Exception {
        // Asumimos que existe una reply con ID 1
        mockMvc.perform(get("/api/replies/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testGetRepliesByCommentId_ShouldReturnReplies() throws Exception {
        mockMvc.perform(get("/api/replies/comment/{commentId}", testComment.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testGetRepliesByUserId_ShouldReturnReplies() throws Exception {
        mockMvc.perform(get("/api/replies/user/{userId}", testUser.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testUpdateReply_ShouldSucceedWithValidData() throws Exception {
        // Usar un ID de reply de prueba
        Long replyId = 1L;
        
        ReplyDTO updateDTO = new ReplyDTO();
        updateDTO.setMessage("Updated reply message");

        mockMvc.perform(put("/api/replies/{id}", replyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testDeleteReply_ShouldSucceedWhenOwner() throws Exception {
        // Usar un ID de reply de prueba
        Long replyId = 1L;

        mockMvc.perform(delete("/api/replies/{id}", replyId))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteReply_ShouldRequireAuthentication() throws Exception {
        mockMvc.perform(delete("/api/replies/{id}", 999L))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testCreateReply_ShouldRejectInvalidCommentId() throws Exception {
        ReplyDTO replyDTO = new ReplyDTO();
        replyDTO.setMessage("Valid reply message");
        replyDTO.setCommentId(99999L); // Non-existent comment ID

        mockMvc.perform(post("/api/replies/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(replyDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetReplyById_ShouldReturnNotFoundForInvalidId() throws Exception {
        mockMvc.perform(get("/api/replies/{id}", 99999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}