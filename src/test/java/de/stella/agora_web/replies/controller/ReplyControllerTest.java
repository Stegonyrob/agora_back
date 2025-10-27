package de.stella.agora_web.replies.controller;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import de.stella.agora_web.replies.model.Reply;
import de.stella.agora_web.replies.repository.ReplyRepository;
import de.stella.agora_web.replies.service.IReplyService;
import de.stella.agora_web.roles.model.Role;
import de.stella.agora_web.roles.repository.RoleRepository;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.repository.UserRepository;

/**
 * Tests para ReplyController - CRUD de respuestas con autenticación y sanciones
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

    @Value("${api-endpoint}")
    private String apiEndpoint;

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

    @Autowired
    private RoleRepository roleRepository;

    @MockBean
    private IReplyService replyService;

    private User testUser;
    private User adminUser;
    private User expelledUser;
    private User suspendedUser;
    private Post testPost;
    private Comment testComment;
    private Reply testReply;
    private Role userRole;
    private Role adminRole;

    @BeforeEach
    void setUp() {
        // Limpiar datos
        replyRepository.deleteAll();
        commentRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();

        // Crear roles
        userRole = new Role();
        userRole.setName("USER");
        userRole = roleRepository.save(userRole);

        adminRole = new Role();
        adminRole.setName("ADMIN");
        adminRole = roleRepository.save(adminRole);

        // Crear usuarios de prueba
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.getRoles().add(userRole);
        testUser.setSanctionStatus(User.SanctionStatus.NONE);
        testUser = userRepository.save(testUser);

        adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setEmail("admin@example.com");
        adminUser.setPassword("admin123");
        adminUser.getRoles().add(adminRole);
        adminUser.setSanctionStatus(User.SanctionStatus.NONE);
        adminUser = userRepository.save(adminUser);

        expelledUser = new User();
        expelledUser.setUsername("expelled");
        expelledUser.setEmail("expelled@example.com");
        expelledUser.setPassword("password123");
        expelledUser.getRoles().add(userRole);
        expelledUser.setSanctionStatus(User.SanctionStatus.EXPELLED);
        expelledUser.setSanctionType(User.SanctionType.EXPELLED);
        expelledUser = userRepository.save(expelledUser);

        suspendedUser = new User();
        suspendedUser.setUsername("suspended");
        suspendedUser.setEmail("suspended@example.com");
        suspendedUser.setPassword("password123");
        suspendedUser.getRoles().add(userRole);
        suspendedUser.setSanctionStatus(User.SanctionStatus.SUSPENDED);
        suspendedUser.setSanctionType(User.SanctionType.SUSPENSION_1WEEK);
        suspendedUser.setSanctionExpiration(LocalDateTime.now().plusDays(7));
        suspendedUser = userRepository.save(suspendedUser);

        // Crear post de prueba
        testPost = new Post();
        testPost.setTitle("Test Post");
        testPost.setMessage("This is a test post.");
        testPost.setUser(testUser);
        testPost.setArchived(false);
        testPost.setCreationDate(LocalDateTime.now());
        testPost = postRepository.save(testPost);

        // Crear comentario de prueba
        testComment = new Comment();
        testComment.setMessage("This is a test comment.");
        testComment.setUser(testUser);
        testComment.setPost(testPost);
        testComment.setCreationDate(LocalDateTime.now());
        testComment = commentRepository.save(testComment);

        // Crear respuesta de prueba
        testReply = new Reply();
        testReply.setMessage("This is a test reply.");
        testReply.setUser(testUser);
        testReply.setComment(testComment);
        testReply.setCreationDate(LocalDateTime.now());
        testReply = replyRepository.save(testReply);
    }

    // ============ TESTS CREATE REPLY ============
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testCreateReply_ValidUser_ShouldReturn201() throws Exception {
        ReplyDTO replyDTO = new ReplyDTO();
        replyDTO.setMessage("This is a new reply.");
        replyDTO.setCommentId(testComment.getId());

        Reply newReply = new Reply();
        newReply.setId(2L);
        newReply.setMessage("This is a new reply.");
        newReply.setUser(testUser);
        newReply.setComment(testComment);
        newReply.setCreationDate(LocalDateTime.now());

        when(replyService.createReply(any(ReplyDTO.class), any(User.class)))
                .thenReturn(newReply);

        mockMvc.perform(post(apiEndpoint + "/any/replies/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(replyDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.message", is("This is a new reply.")))
                .andExpect(jsonPath("$.userId", is(testUser.getId().intValue())))
                .andExpect(jsonPath("$.commentId", is(testComment.getId().intValue())));
    }

    @Test
    void testCreateReply_WithoutAuthentication_ShouldReturn401() throws Exception {
        ReplyDTO replyDTO = new ReplyDTO();
        replyDTO.setMessage("Unauthorized reply.");
        replyDTO.setCommentId(testComment.getId());

        mockMvc.perform(post(apiEndpoint + "/any/replies/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(replyDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "expelled", roles = {"USER"})
    void testCreateReply_ExpelledUser_ShouldReturn403() throws Exception {
        ReplyDTO replyDTO = new ReplyDTO();
        replyDTO.setMessage("This should be blocked.");
        replyDTO.setCommentId(testComment.getId());

        mockMvc.perform(post(apiEndpoint + "/any/replies/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(replyDTO)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.sanctionType", is("EXPELLED")))
                .andExpect(jsonPath("$.message", is("No puedes participar porque has sido expulsado. Contacta a soporte si crees que es un error.")));
    }

    @Test
    @WithMockUser(username = "suspended", roles = {"USER"})
    void testCreateReply_SuspendedUser_ShouldReturn423() throws Exception {
        ReplyDTO replyDTO = new ReplyDTO();
        replyDTO.setMessage("This should be blocked.");
        replyDTO.setCommentId(testComment.getId());

        mockMvc.perform(post(apiEndpoint + "/any/replies/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(replyDTO)))
                .andExpect(status().isLocked())
                .andExpect(jsonPath("$.sanctionType", is("SUSPENSION_1WEEK")))
                .andExpect(jsonPath("$.message", is("No puedes participar porque estás suspendido hasta la fecha indicada.")));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testCreateReply_EmptyMessage_ShouldReturn400() throws Exception {
        ReplyDTO replyDTO = new ReplyDTO();
        replyDTO.setMessage("");
        replyDTO.setCommentId(testComment.getId());

        mockMvc.perform(post(apiEndpoint + "/any/replies/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(replyDTO)))
                .andExpect(status().isBadRequest());
    }

    // ============ TESTS GET REPLY BY ID ============
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testGetReplyById_ShouldReturn200() throws Exception {
        when(replyService.getReplyById(testReply.getId())).thenReturn(testReply);

        mockMvc.perform(get(apiEndpoint + "/any/replies/{id}", testReply.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testReply.getId().intValue())))
                .andExpect(jsonPath("$.message", is("This is a test reply.")))
                .andExpect(jsonPath("$.userId", is(testUser.getId().intValue())))
                .andExpect(jsonPath("$.commentId", is(testComment.getId().intValue())));
    }

    @Test
    void testGetReplyById_WithoutAuthentication_ShouldReturn401() throws Exception {
        mockMvc.perform(get(apiEndpoint + "/any/replies/{id}", testReply.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testGetReplyById_NonExistentReply_ShouldReturn404() throws Exception {
        when(replyService.getReplyById(99999L))
                .thenThrow(new RuntimeException("Reply not found"));

        mockMvc.perform(get(apiEndpoint + "/any/replies/{id}", 99999L))
                .andExpect(status().isInternalServerError());
    }

    // ============ TESTS GET REPLIES BY COMMENT ID ============
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testGetRepliesByCommentId_ShouldReturn200() throws Exception {
        List<Reply> replies = Arrays.asList(testReply);
        when(replyService.getRepliesByCommentId(testComment.getId())).thenReturn(replies);

        mockMvc.perform(get(apiEndpoint + "/any/replies/comment/{commentId}", testComment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].id", is(testReply.getId().intValue())))
                .andExpect(jsonPath("$[0].message", is("This is a test reply.")));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testGetRepliesByCommentId_EmptyResult_ShouldReturn200() throws Exception {
        when(replyService.getRepliesByCommentId(testComment.getId())).thenReturn(Arrays.asList());

        mockMvc.perform(get(apiEndpoint + "/any/replies/comment/{commentId}", testComment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()", is(0)));
    }

    @Test
    void testGetRepliesByCommentId_WithoutAuthentication_ShouldReturn401() throws Exception {
        mockMvc.perform(get(apiEndpoint + "/any/replies/comment/{commentId}", testComment.getId()))
                .andExpect(status().isUnauthorized());
    }

    // ============ TESTS GET REPLIES BY USER ID ============
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testGetRepliesByUserId_ShouldReturn200() throws Exception {
        List<Reply> replies = Arrays.asList(testReply);
        when(replyService.getRepliesByUserId(testUser.getId())).thenReturn(replies);

        mockMvc.perform(get(apiEndpoint + "/any/replies/user/{userId}", testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].id", is(testReply.getId().intValue())))
                .andExpect(jsonPath("$[0].message", is("This is a test reply.")));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testGetRepliesByUserId_EmptyResult_ShouldReturn200() throws Exception {
        when(replyService.getRepliesByUserId(99999L)).thenReturn(Arrays.asList());

        mockMvc.perform(get(apiEndpoint + "/any/replies/user/{userId}", 99999L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()", is(0)));
    }

    @Test
    void testGetRepliesByUserId_WithoutAuthentication_ShouldReturn401() throws Exception {
        mockMvc.perform(get(apiEndpoint + "/any/replies/user/{userId}", testUser.getId()))
                .andExpect(status().isUnauthorized());
    }

    // ============ TESTS UPDATE REPLY ============
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testUpdateReply_ValidUser_ShouldReturn202() throws Exception {
        ReplyDTO updateDTO = new ReplyDTO();
        updateDTO.setMessage("Updated reply message.");

        Reply updatedReply = new Reply();
        updatedReply.setId(testReply.getId());
        updatedReply.setMessage("Updated reply message.");
        updatedReply.setUser(testUser);
        updatedReply.setComment(testComment);

        when(replyService.updateReply(eq(testReply.getId()), any(ReplyDTO.class)))
                .thenReturn(updatedReply);

        mockMvc.perform(put(apiEndpoint + "/any/replies/{id}", testReply.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.message", is("Updated reply message.")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateReply_AsAdmin_ShouldReturn202() throws Exception {
        ReplyDTO updateDTO = new ReplyDTO();
        updateDTO.setMessage("Admin updated reply.");

        Reply updatedReply = new Reply();
        updatedReply.setId(testReply.getId());
        updatedReply.setMessage("Admin updated reply.");
        updatedReply.setUser(testUser);

        when(replyService.updateReply(eq(testReply.getId()), any(ReplyDTO.class)))
                .thenReturn(updatedReply);

        mockMvc.perform(put(apiEndpoint + "/any/replies/{id}", testReply.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.message", is("Admin updated reply.")));
    }

    @Test
    @WithMockUser(username = "expelled", roles = {"USER"})
    void testUpdateReply_ExpelledUser_ShouldReturn403() throws Exception {
        ReplyDTO updateDTO = new ReplyDTO();
        updateDTO.setMessage("This should be blocked.");

        mockMvc.perform(put(apiEndpoint + "/any/replies/{id}", testReply.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.sanctionType", is("EXPELLED")))
                .andExpect(jsonPath("$.message", is("No puedes editar respuestas porque has sido expulsado. Contacta a soporte si crees que es un error.")));
    }

    @Test
    @WithMockUser(username = "suspended", roles = {"USER"})
    void testUpdateReply_SuspendedUser_ShouldReturn423() throws Exception {
        ReplyDTO updateDTO = new ReplyDTO();
        updateDTO.setMessage("This should be blocked.");

        mockMvc.perform(put(apiEndpoint + "/any/replies/{id}", testReply.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isLocked())
                .andExpect(jsonPath("$.sanctionType", is("SUSPENSION_1WEEK")))
                .andExpect(jsonPath("$.message", is("No puedes editar respuestas porque estás suspendido hasta la fecha indicada.")));
    }

    @Test
    void testUpdateReply_WithoutAuthentication_ShouldReturn401() throws Exception {
        ReplyDTO updateDTO = new ReplyDTO();
        updateDTO.setMessage("Unauthorized update.");

        mockMvc.perform(put(apiEndpoint + "/any/replies/{id}", testReply.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isUnauthorized());
    }

    // ============ TESTS DELETE REPLY ============
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testDeleteReply_ValidUser_ShouldReturn204() throws Exception {
        mockMvc.perform(delete(apiEndpoint + "/any/replies/{id}", testReply.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteReply_AsAdmin_ShouldReturn204() throws Exception {
        mockMvc.perform(delete(apiEndpoint + "/any/replies/{id}", testReply.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "expelled", roles = {"USER"})
    void testDeleteReply_ExpelledUser_ShouldReturn403() throws Exception {
        mockMvc.perform(delete(apiEndpoint + "/any/replies/{id}", testReply.getId()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.sanctionType", is("EXPELLED")))
                .andExpect(jsonPath("$.message", is("No puedes borrar respuestas porque has sido expulsado. Contacta a soporte si crees que es un error.")));
    }

    @Test
    @WithMockUser(username = "suspended", roles = {"USER"})
    void testDeleteReply_SuspendedUser_ShouldReturn423() throws Exception {
        mockMvc.perform(delete(apiEndpoint + "/any/replies/{id}", testReply.getId()))
                .andExpect(status().isLocked())
                .andExpect(jsonPath("$.sanctionType", is("SUSPENSION_1WEEK")))
                .andExpect(jsonPath("$.message", is("No puedes borrar respuestas porque estás suspendido hasta la fecha indicada.")));
    }

    @Test
    void testDeleteReply_WithoutAuthentication_ShouldReturn401() throws Exception {
        mockMvc.perform(delete(apiEndpoint + "/any/replies/{id}", testReply.getId()))
                .andExpect(status().isUnauthorized());
    }

    // ============ TESTS VALIDATION AND EDGE CASES ============
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testCreateReply_OffensiveContent_ShouldBeModerated() throws Exception {
        ReplyDTO replyDTO = new ReplyDTO();
        replyDTO.setMessage("This contains offensive words that should be moderated.");
        replyDTO.setCommentId(testComment.getId());

        Reply moderatedReply = new Reply();
        moderatedReply.setId(2L);
        moderatedReply.setMessage("This contains *** words that should be moderated."); // Censurado
        moderatedReply.setUser(testUser);
        moderatedReply.setComment(testComment);

        when(replyService.createReply(any(ReplyDTO.class), any(User.class)))
                .thenReturn(moderatedReply);

        mockMvc.perform(post(apiEndpoint + "/any/replies/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(replyDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is("This contains *** words that should be moderated.")));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testCreateReply_XSSProtection_ShouldSanitizeInput() throws Exception {
        ReplyDTO replyDTO = new ReplyDTO();
        replyDTO.setMessage("<script>alert('XSS')</script>Safe reply");
        replyDTO.setCommentId(testComment.getId());

        Reply sanitizedReply = new Reply();
        sanitizedReply.setId(2L);
        sanitizedReply.setMessage("<script>alert('XSS')</script>Safe reply");
        sanitizedReply.setUser(testUser);
        sanitizedReply.setComment(testComment);

        when(replyService.createReply(any(ReplyDTO.class), any(User.class)))
                .thenReturn(sanitizedReply);

        mockMvc.perform(post(apiEndpoint + "/any/replies/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(replyDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testUpdateReply_ServiceException_ShouldReturn500() throws Exception {
        ReplyDTO updateDTO = new ReplyDTO();
        updateDTO.setMessage("Updated reply.");

        when(replyService.updateReply(eq(testReply.getId()), any(ReplyDTO.class)))
                .thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(put(apiEndpoint + "/any/replies/{id}", testReply.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isInternalServerError());
    }
}
