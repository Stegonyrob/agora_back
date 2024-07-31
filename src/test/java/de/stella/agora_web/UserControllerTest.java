package de.stella.agora_web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import de.stella.agora_web.user.controller.UserController;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.services.impl.UserServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class UserControllerTest {

  private MockMvc mockMvc;

  @Mock
  private UserServiceImpl userServiceMock;

  private UserController userController;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    this.mockMvc =
      MockMvcBuilders
        .standaloneSetup(new UserController(userServiceMock))
        .build();
    this.userController = new UserController(userServiceMock);
  }

  @Test
  public void testIndex() throws Exception {
    // Arrange
    List<User> users = new ArrayList<>();
    when(userServiceMock.getAll()).thenReturn(users);

    // Act
    List<User> result = userController.index();

    // Assert
    assertEquals(result, users);
  }

  @Test
  public void testGetById() throws Exception {
    // Arrange
    Long userId = 1L;
    User user = new User(
      "name",
      "email",
      true,
      "password",
      "User",
      "User",
      "User",
      "User",
      "User"
    );
    when(userServiceMock.findById(userId)).thenReturn(Optional.of(user));

    // Act
    ResponseEntity<User> result = userController.getById(userId);

    // Assert
    assertEquals(ResponseEntity.ok(user), result);
  }

  @Test
  public void testCreate() throws Exception {
    // Arrange
    User user = new User(
      "name",
      "email",
      true,
      "password",
      "User",
      "User",
      "User",
      "User",
      "User"
    );
    when(userServiceMock.save(user)).thenReturn(user);

    // Act
    ResponseEntity<User> result = userController.create(user);

    // Assert
    assertEquals(ResponseEntity.status(201).body(user), result);
  }
}
