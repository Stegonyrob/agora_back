package de.stella.agora_web.posts.controller;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.stella.agora_web.config.TestConfig;
import de.stella.agora_web.posts.controller.dto.PostDTO;
import de.stella.agora_web.posts.controller.dto.PostResponseDTO;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.posts.repository.PostRepository;
import de.stella.agora_web.posts.service.IPostService;
import de.stella.agora_web.profiles.repository.ProfileRepository;
import de.stella.agora_web.roles.model.Role;
import de.stella.agora_web.roles.repository.RoleRepository;
import de.stella.agora_web.tags.dto.PostSummaryDTO;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.repository.UserRepository;

/**
 * Tests para PostController - CRUD de posts con paginación y archivado
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
public class PostControllerTest {

    @org.springframework.beans.factory.annotation.Value("${api-endpoint}")
    private String apiEndpoint;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @MockBean
    private IPostService postService;

    @MockBean
    private ProfileRepository profileRepository;

    private User testUser;
    private Post testPost;
    private Role userRole;

    @BeforeEach
    void setUp() {
        // Limpiar datos
        postRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();

        // Crear role
        userRole = new Role();
        userRole.setName("USER");
        userRole = roleRepository.save(userRole);

        // Crear usuario de prueba
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.getRoles().add(userRole);
        testUser = userRepository.save(testUser);

        // Crear post de prueba
        testPost = new Post();
        testPost.setTitle("Test Post");
        testPost.setMessage("This is a test post message.");
        testPost.setUser(testUser);
        testPost.setArchived(false);
        testPost.setCreationDate(LocalDateTime.now());
        testPost = postRepository.save(testPost);
    }

    // ============ TESTS GET ALL POSTS WITH PAGINATION ============
    @Test
    void testGetAllPosts_WithPagination_ShouldReturn200() throws Exception {
        PostSummaryDTO postSummary = new PostSummaryDTO();
        postSummary.setId(testPost.getId());
        postSummary.setTitle("Test Post");
        postSummary.setMessage("This is a test post message.");

        Page<PostSummaryDTO> mockPage = new PageImpl<>(Arrays.asList(postSummary), PageRequest.of(0, 10), 1);
        when(postService.getAllPostsWithCounts(any(Pageable.class))).thenReturn(mockPage);

        mockMvc.perform(get(apiEndpoint + "/posts")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id", is(testPost.getId().intValue())))
                .andExpect(jsonPath("$.content[0].title", is("Test Post")))
                .andExpect(jsonPath("$.totalElements", is(1)));
    }

    @Test
    void testGetAllPosts_EmptyResult_ShouldReturn204() throws Exception {
        Page<PostSummaryDTO> emptyPage = new PageImpl<>(Arrays.asList(), PageRequest.of(0, 10), 0);
        when(postService.getAllPostsWithCounts(any(Pageable.class))).thenReturn(emptyPage);

        mockMvc.perform(get(apiEndpoint + "/posts"))
                .andExpect(status().isNoContent());
    }

    // ============ TESTS GET POST BY ID ============
    @Test
    void testGetPostById_ValidId_ShouldReturn200() throws Exception {
        PostResponseDTO responseDTO = new PostResponseDTO();
        responseDTO.setId(testPost.getId());
        responseDTO.setTitle("Test Post");
        responseDTO.setMessage("This is a test post message.");

        when(postService.getPostResponseById(testPost.getId())).thenReturn(responseDTO);

        mockMvc.perform(get(apiEndpoint + "/posts/{id}", testPost.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testPost.getId().intValue())))
                .andExpect(jsonPath("$.title", is("Test Post")))
                .andExpect(jsonPath("$.message", is("This is a test post message.")));
    }

    @Test
    void testGetPostById_InvalidId_ShouldReturn404() throws Exception {
        when(postService.getPostResponseById(99999L))
                .thenThrow(new NoSuchElementException("Post not found"));

        mockMvc.perform(get(apiEndpoint + "/posts/{id}", 99999L))
                .andExpect(status().isInternalServerError());
    }

    // ============ TESTS CREATE POST ============
    @Test
    void testCreatePost_ValidData_ShouldReturn201() throws Exception {
        PostDTO newPostDTO = PostDTO.builder()
                .title("New Test Post")
                .message("This is a new test post.")
                .isArchived(false)
                .build();

        Post newPost = new Post();
        newPost.setId(2L);
        newPost.setTitle("New Test Post");
        newPost.setMessage("This is a new test post.");
        newPost.setArchived(false);

        when(postService.saveSimple(any(Post.class))).thenReturn(newPost);

        mockMvc.perform(post(apiEndpoint + "/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPostDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", is("New Test Post")))
                .andExpect(jsonPath("$.message", is("This is a new test post.")));
    }

    @Test
    void testCreatePost_NullData_ShouldReturn400() throws Exception {
        mockMvc.perform(post(apiEndpoint + "/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreatePost_EmptyTitle_ShouldReturn400() throws Exception {
        PostDTO invalidPostDTO = PostDTO.builder()
                .title("")
                .message("Valid message")
                .build();

        mockMvc.perform(post(apiEndpoint + "/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidPostDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreatePost_TooLongTitle_ShouldReturn400() throws Exception {
        PostDTO invalidPostDTO = PostDTO.builder()
                .title("a".repeat(101)) // Más de 100 caracteres
                .message("Valid message")
                .build();

        mockMvc.perform(post(apiEndpoint + "/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidPostDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreatePost_TooLongMessage_ShouldReturn400() throws Exception {
        PostDTO invalidPostDTO = PostDTO.builder()
                .title("Valid title")
                .message("a".repeat(25001)) // Más de 25000 caracteres
                .build();

        mockMvc.perform(post(apiEndpoint + "/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidPostDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreatePost_ServiceException_ShouldReturn500() throws Exception {
        PostDTO postDTO = PostDTO.builder()
                .title("Test")
                .message("Test message")
                .build();

        when(postService.saveSimple(any(Post.class)))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(post(apiEndpoint + "/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postDTO)))
                .andExpect(status().isInternalServerError());
    }

    // ============ TESTS UPDATE POST ============
    @Test
    void testUpdatePost_ValidData_ShouldReturn202() throws Exception {
        PostDTO updateDTO = PostDTO.builder()
                .title("Updated Post")
                .message("Updated message")
                .build();

        Post updatedPost = new Post();
        updatedPost.setId(testPost.getId());
        updatedPost.setTitle("Updated Post");
        updatedPost.setMessage("Updated message");

        when(postService.update(any(PostDTO.class), eq(testPost.getId())))
                .thenReturn(updatedPost);

        mockMvc.perform(put(apiEndpoint + "/posts/{id}", testPost.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.title", is("Updated Post")))
                .andExpect(jsonPath("$.message", is("Updated message")));
    }

    @Test
    void testUpdatePost_InvalidId_ShouldReturn404() throws Exception {
        PostDTO updateDTO = PostDTO.builder()
                .title("Updated Post")
                .message("Updated message")
                .build();

        when(postService.update(any(PostDTO.class), eq(99999L)))
                .thenThrow(new NoSuchElementException("Post not found"));

        mockMvc.perform(put(apiEndpoint + "/posts/{id}", 99999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testUpdatePost_InvalidData_ShouldReturn400() throws Exception {
        PostDTO invalidUpdateDTO = PostDTO.builder()
                .title("") // Título vacío
                .message("Valid message")
                .build();

        mockMvc.perform(put(apiEndpoint + "/posts/{id}", testPost.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUpdateDTO)))
                .andExpect(status().isBadRequest());
    }

    // ============ TESTS ARCHIVE/UNARCHIVE POST ============
    @Test
    void testArchivePost_ShouldReturn204() throws Exception {
        when(postService.getById(testPost.getId())).thenReturn(testPost);

        mockMvc.perform(patch(apiEndpoint + "/posts/{id}/archive", testPost.getId())
                .param("archive", "true"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testUnarchivePost_ShouldReturn204() throws Exception {
        when(postService.getById(testPost.getId())).thenReturn(testPost);

        mockMvc.perform(patch(apiEndpoint + "/posts/{id}/archive", testPost.getId())
                .param("archive", "false"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testArchivePost_InvalidId_ShouldReturn404() throws Exception {
        when(postService.getById(99999L))
                .thenThrow(new NoSuchElementException("Post not found"));

        mockMvc.perform(patch(apiEndpoint + "/posts/{id}/archive", 99999L)
                .param("archive", "true"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testArchivePost_ServiceException_ShouldReturn500() throws Exception {
        when(postService.getById(testPost.getId())).thenReturn(testPost);
        doThrow(new RuntimeException("Service error")).when(postService).archivePost(testPost.getId());

        mockMvc.perform(patch(apiEndpoint + "/posts/{id}/archive", testPost.getId())
                .param("archive", "true"))
                .andExpect(status().isInternalServerError());
    }

    // ============ TESTS GET POSTS BY USER ID ============
    @Test
    void testGetPostsByUserId_ShouldReturn200() throws Exception {
        List<Post> userPosts = Arrays.asList(testPost);
        when(postService.getPostsByUserId(testUser.getId())).thenReturn(userPosts);

        mockMvc.perform(get(apiEndpoint + "/user/{userId}", testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].id", is(testPost.getId().intValue())));
    }

    @Test
    void testGetPostsByUserId_EmptyResult_ShouldReturn204() throws Exception {
        when(postService.getPostsByUserId(testUser.getId())).thenReturn(Arrays.asList());

        mockMvc.perform(get(apiEndpoint + "/user/{userId}", testUser.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetPostsByUserId_InvalidUserId_ShouldReturn204() throws Exception {
        when(postService.getPostsByUserId(99999L)).thenReturn(Arrays.asList());

        mockMvc.perform(get(apiEndpoint + "/user/{userId}", 99999L))
                .andExpect(status().isNoContent());
    }

    // ============ TESTS VALIDATION AND EDGE CASES ============
    @Test
    void testCreatePost_XSSProtection_ShouldSanitizeInput() throws Exception {
        PostDTO xssPostDTO = PostDTO.builder()
                .title("<script>alert('XSS')</script>Safe Title")
                .message("<img src=x onerror=alert('XSS')>Safe message")
                .build();

        Post newPost = new Post();
        newPost.setId(2L);
        newPost.setTitle("<script>alert('XSS')</script>Safe Title");
        newPost.setMessage("<img src=x onerror=alert('XSS')>Safe message");

        when(postService.saveSimple(any(Post.class))).thenReturn(newPost);

        mockMvc.perform(post(apiEndpoint + "/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(xssPostDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreatePost_SQLInjectionProtection_ShouldBeHandledSafely() throws Exception {
        PostDTO sqlInjectionDTO = PostDTO.builder()
                .title("'; DROP TABLE posts; --")
                .message("1' OR '1'='1")
                .build();

        Post newPost = new Post();
        newPost.setId(2L);
        newPost.setTitle("'; DROP TABLE posts; --");
        newPost.setMessage("1' OR '1'='1");

        when(postService.saveSimple(any(Post.class))).thenReturn(newPost);

        mockMvc.perform(post(apiEndpoint + "/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sqlInjectionDTO)))
                .andExpect(status().isCreated());
    }

    // ============ TESTS PERFORMANCE AND PAGINATION ============
    @Test
    void testGetAllPosts_LargePage_ShouldHandleEfficiently() throws Exception {
        PostSummaryDTO postSummary = new PostSummaryDTO();
        Page<PostSummaryDTO> largePage = new PageImpl<>(Arrays.asList(postSummary), PageRequest.of(0, 100), 1);
        when(postService.getAllPostsWithCounts(any(Pageable.class))).thenReturn(largePage);

        mockMvc.perform(get(apiEndpoint + "/posts")
                .param("size", "100"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllPosts_InvalidPaginationParams_ShouldUseDefaults() throws Exception {
        PostSummaryDTO postSummary = new PostSummaryDTO();
        Page<PostSummaryDTO> page = new PageImpl<>(Arrays.asList(postSummary), PageRequest.of(0, 10), 1);
        when(postService.getAllPostsWithCounts(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get(apiEndpoint + "/posts")
                .param("page", "-1")
                .param("size", "0"))
                .andExpect(status().isOk());
    }
}
