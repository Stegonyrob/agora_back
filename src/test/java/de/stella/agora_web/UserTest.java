package de.stella.agora_web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import de.stella.agora_web.profiles.model.Profile;
import de.stella.agora_web.roles.model.Role;
import de.stella.agora_web.user.model.User;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.security.core.GrantedAuthority;

public class UserTest {

  private User user;

  @Mock
  private Profile profile;

  @BeforeEach
  public void setUp() {
    user = new User("test", "password", "test@example.com");
    Set<Role> roles = new HashSet<>();
    Role role1 = mock(Role.class);
    Role role2 = mock(Role.class);
    roles.add(role1);
    roles.add(role2);
    user.setRoles(roles);
  }

  @Test
  public void testGetId() {
    assertEquals(1L, user.getId());
  }

  @Test
  public void testGetUsername() {
    assertEquals("testUser", user.getUsername());
  }

  @Test
  public void testGetPassword() {
    assertEquals("password", user.getPassword());
  }

  @Test
  public void testGetEmail() {
    assertEquals("test@example.com", user.getEmail());
  }

  @Test
  public void testGetRoles() {
    Set<Role> roles = new HashSet<>();
    Role role1 = mock(Role.class);
    Role role2 = mock(Role.class);
    roles.add(role1);
    roles.add(role2);
    user.setRoles(roles);
    assertEquals(2, user.getRoles().size());
  }

  @Test
  public void testHasRole() {
    Role role1 = mock(Role.class);
    Role role2 = mock(Role.class);
    when(role1.getName()).thenReturn("ROLE_USER");
    when(role2.getName()).thenReturn("ROLE_ADMIN");
    Set<Role> roles = new HashSet<>();
    roles.add(role1);
    roles.add(role2);
    user.setRoles(roles);
    assertTrue(user.hasRole("ROLE_USER"));
    assertFalse(user.hasRole("ROLE_GUEST"));
  }

  @Test
  public void testGetAuthority() {
    Set<Role> roles = user.getRoles();
    if (!roles.isEmpty()) {
      Role firstRole = roles.iterator().next();
      String roleName = firstRole.getName();
      if (roleName != null && !roleName.isBlank()) {
        GrantedAuthority authority = user.getAuthority();
        assertNotNull(authority);
        assertEquals(roleName, authority.getAuthority());
      } else {
        throw new AssertionError("Role name is empty or null");
      }
    } else {
      assertThrows(
        NoSuchBeanDefinitionException.class,
        () -> user.getAuthority()
      );
    }
  }
}
