package de.stella.agora_web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import de.stella.agora_web.user.controller.UserController;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.services.impl.UserServiceImpl;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class UserControllerTest {

  @Mock
  private UserServiceImpl userServiceMock;

  private UserController userController;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
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
}
