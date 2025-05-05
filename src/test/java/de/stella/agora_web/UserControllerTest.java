package de.stella.agora_web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import de.stella.agora_web.user.controller.UserController;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.services.impl.UserServiceImpl;

public class UserControllerTest {

  @Mock
  private UserServiceImpl userServiceMock;

  private UserController userController;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    this.userController = new UserController(userServiceMock);
  }

  @Test
  public void testIndex_ReturnsUsers() {
    // Arrange
    List<User> users = new ArrayList<>();
    users.add(new User(1L, "testUser1", "test1@example.com"));
    users.add(new User(2L, "testUser2", "test2@example.com"));
    when(userServiceMock.getAll()).thenReturn(users);

    // Act
    ResponseEntity<List<User>> result = userController.index();

    // Assert
    assertNotNull(result);
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertNotNull(result.getBody());
    assertEquals(users.size(), result.getBody().size());
    for (int i = 0; i < users.size(); i++) {
      User expectedUser = users.get(i);
      User actualUser = result.getBody().get(i);
      assertNotNull(actualUser);
      assertEquals(expectedUser.getId(), actualUser.getId());
      assertEquals(expectedUser.getUsername(), actualUser.getUsername());
      assertEquals(expectedUser.getEmail(), actualUser.getEmail());
    }
  }

  @Test
  public void testIndex_ReturnsEmptyList() throws Exception {
    // Arrange
    List<User> users = new ArrayList<>();
    when(userServiceMock.getAll()).thenReturn(users);

    // Act
    ResponseEntity<List<User>> result = userController.index();

    // Assert
    assertNotNull(result);
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertNotNull(result.getBody());
    assertNotNull(result.getBody());
    assertTrue(result.getBody().isEmpty());
  }

  @Test
  public void testIndex_ServiceThrowsException() throws Exception {
    // Arrange
    when(userServiceMock.getAll()).thenThrow(new RuntimeException("Service error"));

    // Act & Assert
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      userController.index();
    });
    assertEquals("Service error", exception.getMessage());
  }
}