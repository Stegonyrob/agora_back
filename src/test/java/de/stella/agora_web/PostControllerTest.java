package de.stella.agora_web;

import static org.mockito.Mockito.*;

import de.stella.agora_web.generics.IGenericFullService;
import de.stella.agora_web.posts.controller.PostController;
import de.stella.agora_web.posts.controller.dto.PostDTO;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.replys.model.Reply;
import de.stella.agora_web.user.model.User;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(PostController.class)
public class PostControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private IGenericFullService<Post, PostDTO> postService;

  @Test
  public void getAllPosts() throws Exception {
    List<Post> posts = List.of(
      new Post(
        1L,
        "title",
        "message",
        LocalDateTime.now(),
        new User(),
        new Reply()
      )
    );
    when(postService.getAll()).thenReturn(posts);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .get("/any/posts")
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
  }

  @Test
  public void getPostById() throws Exception {
    Long id = 1L;
    Post post = new Post(
      id,
      "title",
      "message",
      LocalDateTime.now(),
      new User(),
      new Reply()
    );
    when(postService.getById(id)).thenReturn(post);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .get("/any/posts/" + id)
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id));
  }
  // Other tests...
}
