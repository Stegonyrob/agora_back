package de.stella.agora_web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import de.stella.agora_web.posts.controller.PostController;
import de.stella.agora_web.posts.controller.dto.PostDTO;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.posts.services.IPostService;

@ExtendWith(MockitoExtension.class)
public class PostControllerTest {

  @Mock
  private IPostService postService;

  @InjectMocks
  private PostController postController;

  @Test
  public void testIndex_ReturnsListOfPosts() {
    // Arrange
    List<Post> posts = List.of(new Post(), new Post());
    when(postService.getAllPosts()).thenReturn(posts);

    // Act
    List<Post> result = postController.index();

    // Assert
    assertEquals(posts, result);
  }

  @Test
  public void testIndex_ReturnsEmptyList() {
    // Arrange
    List<Post> posts = List.of();
    when(postService.getAllPosts()).thenReturn(posts);

    // Act
    List<Post> result = postController.index();

    // Assert
    assertEquals(posts, result);
  }

  @Test
  public void testIndex_ThrowsException() {
    // Arrange
    when(postService.getAllPosts()).thenThrow(new RuntimeException());

    // Act and Assert
    assertThrows(RuntimeException.class, () -> postController.index());
  }

  @Test
  public void testCreatePost_Success() throws Exception {
    // Arrange
    PostDTO postDTO = new PostDTO(null, "title", "message", null, null, null, null, null, null);
    Post newPost = new Post();
    when(postService.save(any(PostDTO.class))).thenReturn(newPost);

    // Act
    ResponseEntity<Post> response = postController.create(postDTO);

    // Assert
    assertEquals(201, response.getStatusCodeValue());
    assertNotNull(response.getBody());
  }

  @Test
  public void testCreatePost_NullPostDTO() throws Exception {
    // Act
    ResponseEntity<Post> response = postController.create(null);

    // Assert
    assertEquals(400, response.getStatusCodeValue());
    assertNull(response.getBody());
  }

  @Test
  public void testCreatePost_InvalidPostDTO() throws Exception {
    // Arrange
    PostDTO postDTO = new PostDTO(null, null, null, null, null, null, null, null, null);
    when(postService.save(any(PostDTO.class))).thenReturn(null);

    // Act
    ResponseEntity<Post> response = postController.create(postDTO);

    // Assert
    assertEquals(400, response.getStatusCodeValue());
    assertNull(response.getBody());
  }

  @Test
  public void testCreatePost_ExceptionThrown() throws Exception {
    // Arrange
    PostDTO postDTO = new PostDTO(null, "title", "message", null, null, null, null, null, null);
    doThrow(new Exception()).when(postService).save(any(PostDTO.class));

    // Act
    ResponseEntity<Post> response = postController.create(postDTO);

    // Assert
    assertEquals(500, response.getStatusCodeValue());
    assertNull(response.getBody());
  }
}