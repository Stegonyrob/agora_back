package de.stella.agora_web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.replys.model.Reply;
import de.stella.agora_web.user.model.User;

public class PostTest {

    @Mock
    private Reply reply;

    @Mock
    private User user;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPost() {
        // Given
        Long id = 1L;
        String title = "Test Title";
        String message = "Test Message";
        LocalDateTime creationDate = LocalDateTime.now();

        // When
        Post post = new Post(id, title, message, creationDate, reply, user);

        // Then
        assertEquals(id, post.getId());
        assertEquals(title, post.getTitle());
        assertEquals(message, post.getMessage());
        assertEquals(creationDate, post.getCreationDate());
        assertEquals(reply, post.getReply_id());
        assertEquals(user, post.getUser());
    }

    @Test
    public void testSetUser() {
        // Given
        Post post = new Post();
        User newUser = mock(User.class);

        // When
        post.setUser(newUser);

        // Then
        assertEquals(newUser, post.getUser());
    }
}