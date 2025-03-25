package de.stella.agora_web;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
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
    @SuppressWarnings("unchecked")
    ResponseEntity<List<Post>> result = (ResponseEntity<List<Post>>) postController.index();

    // Assert
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertEquals(posts, result.getBody());
  }

  @Test
  public void testIndex_ReturnsEmptyList() {
    // Arrange
    List<Post> posts = List.of();
    when(postService.getAllPosts()).thenReturn(posts);

    // Act
    @SuppressWarnings("unchecked")
    ResponseEntity<List<Post>> result = (ResponseEntity<List<Post>>) postController.index();

    // Assert
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertEquals(posts, result.getBody());
  }

  @Test
  public void testIndex_ThrowsException() {
    // Arrange
    when(postService.getAllPosts()).thenThrow(new RuntimeException());

    // Act and Assert
    @SuppressWarnings("unchecked")
    ResponseEntity<List<Post>> response = (ResponseEntity<List<Post>>) postController.index();
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  public void testCreatePost_Success() throws Exception {
    // Arrange
    PostDTO postDto = new PostDTO((Post) null);
    Post expectedPost = new Post();
    when(postService.save(postDto)).thenReturn(expectedPost);

    // Act
    ResponseEntity<Post> response = postController.create(postDto);

    // Assert
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(expectedPost, response.getBody());
  }

  @Test
  public void testCreatePost_NullPostDTO() throws Exception {
    // Act
    ResponseEntity<Post> response = postController.create(null);

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  public void testCreatePost_InvalidPostDTO() throws Exception {
    // Arrange
    PostDTO postDTO = new PostDTO((Post) null);
    when(postService.save(any(PostDTO.class))).thenReturn(null);

    // Act
    ResponseEntity<Post> response = postController.create(postDTO);

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  public void testCreatePost_ExceptionThrown() throws Exception {
    // Arrange
    PostDTO postDTO = new PostDTO((Post) null);
    when(postService.save(any(PostDTO.class))).thenReturn(null);
    doThrow(new RuntimeException()).when(postService).save(any(PostDTO.class));

    // Act
    ResponseEntity<Post> response = postController.create(postDTO);

    // Assert
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertNull(response.getBody());
  }
}
