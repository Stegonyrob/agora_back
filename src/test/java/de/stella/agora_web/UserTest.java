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

}