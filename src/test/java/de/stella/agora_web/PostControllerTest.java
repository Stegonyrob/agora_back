package de.stella.agora_web;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.stella.agora_web.generics.IGenericFullService;
import de.stella.agora_web.generics.IGenericSearchService;
import de.stella.agora_web.messages.Message;
import de.stella.agora_web.posts.controller.PostController;
import de.stella.agora_web.posts.controller.dto.PostDTO;
import de.stella.agora_web.posts.model.Post;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

// ...

// Doc: https://docs.spring.io/spring-framework/docs/6.0.2/reference/html/testing.html#spring-mvc-test-framework

@WebMvcTest(controllers = PostController.class)
@AutoConfigureMockMvc(addFilters = false) // disable security
public class PostControllerTest {

  ObjectMapper mapper = new ObjectMapper();

  @Autowired
  MockMvc mockMvc;

  @MockBean
  IGenericFullService<Post, PostDTO> service;

  @MockBean
  IGenericSearchService<Post> searchService;

  @Test
  @DisplayName("Should return a list of Posts")
  void testIndex() throws Exception {
    List<Post> posts = List.of(new Post());
    when(service.getAll()).thenReturn(posts);

    MockHttpServletResponse response = mockMvc
      .perform(
        get("/any/posts")
          .accept(MediaType.APPLICATION_JSON)
          .content("application/json")
      )
      .andExpect(status().isOk())
      .andReturn()
      .getResponse();

    assertThat(response.getStatus(), is(HttpStatus.OK.value()));
    assertThat(
      response.getContentAsString(),
      equalTo(mapper.writeValueAsString(posts))
    );
  }

  @Test
  @DisplayName("Should return Post with id 1")
  void testGetPost() throws Exception {
    Post post = new Post();
    post.setId(1L);

    when(service.get(1L)).thenReturn(post);

    MockHttpServletResponse response = mockMvc
      .perform(
        get("/any/posts/{id}", 1L)
          .accept(MediaType.APPLICATION_JSON)
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andReturn()
      .getResponse();

    assertThat(response.getStatus(), is(HttpStatus.OK.value()));
    assertThat(
      response.getContentAsString(),
      equalTo(mapper.writeValueAsString(post))
    );
  }

  @Test
  @DisplayName("Should return created Post")
  void testCreatePost() throws Exception {
    Post postToCreate = new Post(
      null,
      "Test Post",
      "This is a test post",
      LocalDateTime.now(),
      null,
      null
    );

    when(service.save()).thenReturn(postToCreate);

    MockHttpServletResponse response = mockMvc
      .perform(
        post("/admin/posts")
          .accept(MediaType.APPLICATION_JSON)
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(postToCreate))
      )
      .andExpect(status().isCreated())
      .andReturn()
      .getResponse();

    assertThat(response.getStatus(), is(HttpStatus.CREATED.value()));
  }

  @Test
  @DisplayName("Should return updated Post")
  void testUpdatePost() throws Exception {
    PostDTO postDTO = new PostDTO(null, null, null, null, null, null);
    postDTO.setTitle("Post 1");

    Post post = new Post();
    post.setId(1L);
    post.setTitle("Post 1");

    when(service.update(1L, postDTO)).thenReturn(post);

    MockHttpServletResponse response = mockMvc
      .perform(
        put("/admin/posts/{id}", 1L)
          .accept(MediaType.APPLICATION_JSON)
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(postDTO))
      )
      .andExpect(status().isOk())
      .andReturn()
      .getResponse();

    assertThat(response.getStatus(), is(HttpStatus.OK.value()));
  }

  @Test
  @DisplayName("Should return deleted Post")
  void testDeletePost() throws Exception {
    when(service.delete(1L)).thenReturn(new Message());

    MockHttpServletResponse response = mockMvc
      .perform(
        delete("/admin/posts/{id}", 1L)
          .accept(MediaType.APPLICATION_JSON)
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andReturn()
      .getResponse();

    assertThat(response.getStatus(), is(HttpStatus.OK.value()));
  }

  @Test
  @DisplayName("Should return posts by userId")
  void testGetPostsByUserId() throws Exception {
    List<Post> posts = List.of(new Post());
    when(service.findPostsByUserId(1L)).thenReturn(posts);

    MockHttpServletResponse response = mockMvc
      .perform(
        get("/user/{userId}", 1L)
          .accept(MediaType.APPLICATION_JSON)
          .content("application/json")
      )
      .andExpect(status().isOk())
      .andReturn()
      .getResponse();

    assertThat(response.getStatus(), is(HttpStatus.OK.value()));
    assertThat(
      response.getContentAsString(),
      equalTo(mapper.writeValueAsString(posts))
    );
  }
}
