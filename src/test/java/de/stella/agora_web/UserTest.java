package de.stella.agora_web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.replys.model.Reply;
import de.stella.agora_web.roles.model.Role;
import de.stella.agora_web.user.controller.dto.UserDTO;
import de.stella.agora_web.user.model.User;

public class UserTest {

    @Mock
    private Role role;

    @Mock
    private Reply reply;

    @Mock
    private Post post;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
    }

    @Test
    public void testHasRole() {
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        when(role.getName()).thenReturn("admin");

        assertEquals(true, user.hasRole("admin"));
        assertEquals(false, user.hasRole("user"));
    }

    @Test
    public void testGetFavorites() {
        Set<Reply> favorites = new HashSet<>();
        favorites.add(reply);

        user.setFavorites(favorites);

        assertEquals(favorites, user.getFavorites());
    }

    @Test
    public void testAddFavorite() {
        user.addFavorite(reply);

        assertEquals(1, user.getFavorites().size());
        assertEquals(reply, user.getFavorites().iterator().next());
    }

    @Test
    public void testRemoveFavorite() {
        Set<Reply> favorites = new HashSet<>();
        favorites.add(reply);

        user.setFavorites(favorites);
        user.removeFavorite(reply);

        assertEquals(0, user.getFavorites().size());
    }

    @Test
    public void testToBuilder() {
        Long id = 1L;
        String username = "testUser";
        String password = "password";
        String firstName = "John";
        String firstLastName = "Doe";
        String secondLastName = "Smith";

        String relationship = "single";
        String email = "test@example.com";
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setFirstLastName(firstLastName);
        user.setSecondLastName(secondLastName);
   
        user.setRelationship(relationship);
        user.setEmail(email);
        user.setRoles(roles);

        UserDTO userDTO = user.toBuilder();

        assertEquals(id, userDTO.getId(id));
        assertEquals(username, userDTO.getUsername());
        assertEquals(email, userDTO.getEmail());
        assertEquals(firstName, userDTO.getFirstName());
        assertEquals(firstLastName, userDTO.getFirstLastName());
        assertEquals(secondLastName, userDTO.getSecondLastName());
        assertEquals(roles, userDTO.getRoles(userDTO));
    }
}