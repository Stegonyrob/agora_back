package de.stella.agora_web;

import de.stella.agora_web.roles.model.Role;
import de.stella.agora_web.user.model.User;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UserTest {

  @Test
  void testUser() {
    Long id = 1L;
    String username = "testUser";
    String password = "testPassword";
    String email = "testUser@test.com";
    Role role = new Role(1L, "TEST_ROLE");
    Set<Role> roles = new HashSet<>();
    roles.add(role);

    User user = new User(
      username,
      password,
      true,
      email,
      "User",
      "User",
      "User",
      "User",
      "User"
    );
    user.setRoles(roles);

    Assertions.assertEquals(id, user.getId());
    Assertions.assertEquals(username, user.getUsername());
    Assertions.assertEquals(password, user.getPassword());
    Assertions.assertEquals(email, user.getEmail());
    Assertions.assertEquals(roles, user.getRoles());
    Assertions.assertTrue(user.hasRole("TEST_ROLE"));
    Assertions.assertEquals("TEST_ROLE", user.getAuthority().getAuthority());
  }
}
