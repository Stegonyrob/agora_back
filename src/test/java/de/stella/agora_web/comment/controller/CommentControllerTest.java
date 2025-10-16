package de.stella.agora_web.comment.controller;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import de.stella.agora_web.comment.controller.dto.CommentDTO;
import de.stella.agora_web.comment.model.Comment;
import de.stella.agora_web.comment.repository.CommentRepository;
import de.stella.agora_web.comment.service.ICommentService;
import de.stella.agora_web.config.TestConfig;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.posts.repository.PostRepository;
import de.stella.agora_web.replies.controller.dto.ReplyDTO;
import de.stella.agora_web.replies.model.Reply;
import de.stella.agora_web.replies.service.IReplyService;
import de.stella.agora_web.roles.model.Role;
import de.stella.agora_web.roles.repository.RoleRepository;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.repository.UserRepository;

/**
 * Tests para CommentController - CRUD de comentarios con moderación y sanciones
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
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @MockBean
    private ICommentService commentService;

    @MockBean
    private IReplyService replyService;

    private User testUser;
    private User adminUser;
    private User expelledUser;
    private User suspendedUser;
    private Post testPost;
    private Comment testComment;
    private Role userRole;
    private Role adminRole;

    @BeforeEach
    void setUp() {
        // Limpiar datos
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
        testPost.setMessage("This is a test post for comments.");
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
    }

    // ============ TESTS CREATE COMMENT ============
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testCreateComment_ValidUser_ShouldReturn201() throws Exception {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setMessage("This is a new comment.");
        commentDTO.setPostId(testPost.getId());

        Comment newComment = new Comment();
        newComment.setId(2L);
        newComment.setMessage("This is a new comment.");
        newComment.setUser(testUser);
        newComment.setPost(testPost);
        newComment.setCreationDate(LocalDateTime.now());

        when(commentService.createComment(any(CommentDTO.class), any(User.class)))
                .thenReturn(newComment);

        mockMvc.perform(post("/api/v1/comments/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.message", is("This is a new comment.")))
                .andExpect(jsonPath("$.userId", is(testUser.getId().intValue())))
                .andExpect(jsonPath("$.replies").isArray());
    }

    @Test
    void testCreateComment_WithoutAuthentication_ShouldReturn401() throws Exception {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setMessage("Unauthorized comment.");
        commentDTO.setPostId(testPost.getId());

        mockMvc.perform(post("/api/v1/comments/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "expelled", roles = {"USER"})
    void testCreateComment_ExpelledUser_ShouldReturn403() throws Exception {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setMessage("This should be blocked.");
        commentDTO.setPostId(testPost.getId());

        mockMvc.perform(post("/api/v1/comments/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDTO)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.sanctionType", is("EXPELLED")))
                .andExpect(jsonPath("$.message", is("No puedes participar porque has sido expulsado. Contacta a soporte si crees que es un error.")));
    }

    @Test
    @WithMockUser(username = "suspended", roles = {"USER"})
    void testCreateComment_SuspendedUser_ShouldReturn423() throws Exception {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setMessage("This should be blocked.");
        commentDTO.setPostId(testPost.getId());

        mockMvc.perform(post("/api/v1/comments/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDTO)))
                .andExpect(status().isLocked())
                .andExpect(jsonPath("$.sanctionType", is("SUSPENSION_1WEEK")))
                .andExpect(jsonPath("$.message", is("No puedes participar porque estás suspendido hasta la fecha indicada.")));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testCreateComment_EmptyMessage_ShouldReturn400() throws Exception {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setMessage("");
        commentDTO.setPostId(testPost.getId());

        mockMvc.perform(post("/api/v1/comments/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testCreateComment_TooLongMessage_ShouldReturn400() throws Exception {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setMessage("a".repeat(1001)); // Mensaje demasiado largo
        commentDTO.setPostId(testPost.getId());

        mockMvc.perform(post("/api/v1/comments/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDTO)))
                .andExpect(status().isBadRequest());
    }

    // ============ TESTS UPDATE COMMENT ============
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testUpdateComment_OwnComment_ShouldReturn202() throws Exception {
        CommentDTO updateDTO = new CommentDTO();
        updateDTO.setMessage("Updated comment message.");

        Comment updatedComment = new Comment();
        updatedComment.setId(testComment.getId());
        updatedComment.setMessage("Updated comment message.");
        updatedComment.setUser(testUser);

        when(commentService.getCommentById(testComment.getId())).thenReturn(testComment);
        when(commentService.updateComment(eq(testComment.getId()), any(CommentDTO.class), any(User.class)))
                .thenReturn(updatedComment);

        mockMvc.perform(put("/api/v1/comments/{id}", testComment.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.message", is("Updated comment message.")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateComment_AsAdmin_ShouldReturn202() throws Exception {
        CommentDTO updateDTO = new CommentDTO();
        updateDTO.setMessage("Admin updated comment.");

        Comment updatedComment = new Comment();
        updatedComment.setId(testComment.getId());
        updatedComment.setMessage("Admin updated comment.");
        updatedComment.setUser(testUser);

        when(commentService.getCommentById(testComment.getId())).thenReturn(testComment);
        when(commentService.updateComment(eq(testComment.getId()), any(CommentDTO.class), any(User.class)))
                .thenReturn(updatedComment);

        mockMvc.perform(put("/api/v1/comments/{id}", testComment.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.message", is("Admin updated comment.")));
    }

    @Test
    @WithMockUser(username = "otheruser", roles = {"USER"})
    void testUpdateComment_OtherUsersComment_ShouldReturn403() throws Exception {
        CommentDTO updateDTO = new CommentDTO();
        updateDTO.setMessage("Unauthorized update.");

        when(commentService.getCommentById(testComment.getId())).thenReturn(testComment);

        mockMvc.perform(put("/api/v1/comments/{id}", testComment.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isForbidden());
    }

    // ============ TESTS DELETE COMMENT ============
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testDeleteComment_OwnComment_ShouldReturn204() throws Exception {
        when(commentService.getCommentById(testComment.getId())).thenReturn(testComment);

        mockMvc.perform(delete("/api/v1/comments/{id}", testComment.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteComment_AsAdmin_ShouldReturn204() throws Exception {
        when(commentService.getCommentById(testComment.getId())).thenReturn(testComment);

        mockMvc.perform(delete("/api/v1/comments/{id}", testComment.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "otheruser", roles = {"USER"})
    void testDeleteComment_OtherUsersComment_ShouldReturn403() throws Exception {
        when(commentService.getCommentById(testComment.getId())).thenReturn(testComment);

        mockMvc.perform(delete("/api/v1/comments/{id}", testComment.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeleteComment_WithoutAuthentication_ShouldReturn401() throws Exception {
        mockMvc.perform(delete("/api/v1/comments/{id}", testComment.getId()))
                .andExpect(status().isUnauthorized());
    }

    // ============ TESTS GET COMMENTS WITH REPLIES ============
    @Test
    void testGetCommentsWithReplies_ShouldReturn200() throws Exception {
        Reply reply = new Reply();
        reply.setId(1L);
        reply.setMessage("This is a reply");
        reply.setUser(testUser);
        reply.setComment(testComment);

        ReplyDTO replyDTO = new ReplyDTO();
        replyDTO.setId(1L);
        replyDTO.setMessage("This is a reply");
        replyDTO.setUserId(testUser.getId());

        Page<Comment> commentsPage = new PageImpl<>(Arrays.asList(testComment), PageRequest.of(0, 10), 1);

        when(commentService.getCommentsByPostId(eq(testPost.getId()), any(Pageable.class)))
                .thenReturn(commentsPage);
        when(replyService.getRepliesByCommentId(testComment.getId()))
                .thenReturn(Arrays.asList(reply));

        mockMvc.perform(get("/api/v1/comments/post/{postId}/with-replies", testPost.getId())
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id", is(testComment.getId().intValue())))
                .andExpect(jsonPath("$.content[0].message", is("This is a test comment.")))
                .andExpect(jsonPath("$.content[0].replies").isArray())
                .andExpect(jsonPath("$.totalElements", is(1)));
    }

    @Test
    void testGetCommentsWithReplies_EmptyResult_ShouldReturn200() throws Exception {
        Page<Comment> emptyPage = new PageImpl<>(Arrays.asList(), PageRequest.of(0, 10), 0);

        when(commentService.getCommentsByPostId(eq(testPost.getId()), any(Pageable.class)))
                .thenReturn(emptyPage);

        mockMvc.perform(get("/api/v1/comments/post/{postId}/with-replies", testPost.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()", is(0)))
                .andExpect(jsonPath("$.totalElements", is(0)));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testCreateComment_OffensiveContent_ShouldBeModerated() throws Exception {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setMessage("This contains offensive words that should be moderated.");
        commentDTO.setPostId(testPost.getId());

        Comment moderatedComment = new Comment();
        moderatedComment.setId(2L);
        moderatedComment.setMessage("This contains *** words that should be moderated."); // Censurado
        moderatedComment.setUser(testUser);
        moderatedComment.setPost(testPost);

        when(commentService.createComment(any(CommentDTO.class), any(User.class)))
                .thenReturn(moderatedComment);

        mockMvc.perform(post("/api/v1/comments/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is("This contains *** words that should be moderated.")));
    }
}
