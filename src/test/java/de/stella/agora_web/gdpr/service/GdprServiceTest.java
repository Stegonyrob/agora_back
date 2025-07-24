package de.stella.agora_web.gdpr.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.stella.agora_web.banned.service.IBannedService;
import de.stella.agora_web.comment.service.ICommentService;
import de.stella.agora_web.posts.service.IPostService;
import de.stella.agora_web.replies.service.IReplyService;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.service.impl.UserServiceImpl;

class GdprServiceTest {

    @Mock
    private UserServiceImpl userService;
    @Mock
    private IBannedService bannedService;
    @Mock
    private ICommentService commentService;
    @Mock
    private IPostService postService;
    @Mock
    private IReplyService replyService;
    @Mock
    private de.stella.agora_web.profiles.model.Profile profile;

    @InjectMocks
    private GdprService gdprService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDeleteAllUserData_UserExists() {
        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(user.getUsername()).thenReturn("user1");
        when(user.getProfile()).thenReturn(profile);
        when(userService.findById(1L)).thenReturn(Optional.of(user));
        when(commentService.getCommentsByUserId(1L)).thenReturn(Collections.emptyList());
        when(replyService.getRepliesByUserId(1L)).thenReturn(Collections.emptyList());
        when(postService.getPostsByUserId(1L)).thenReturn(Collections.emptyList());
        doNothing().when(userService).deleteById(1L);

        boolean result = gdprService.deleteAllUserData(1L);
        assertTrue(result);
        verify(userService).deleteById(1L);
        // Verifica que el perfil está asociado y se procesa (aunque se elimina en cascada)
        verify(user, atLeastOnce()).getProfile();
    }

    @Test
    void testDeleteAllUserData_UserNotFound() {
        when(userService.findById(99L)).thenReturn(Optional.empty());
        boolean result = gdprService.deleteAllUserData(99L);
        assertFalse(result);
        verify(userService, never()).deleteById(anyLong());
    }

    @Test
    void testCanDeleteUser_LastAdmin() {
        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(user.getUsername()).thenReturn("admin");
        when(user.getProfile()).thenReturn(profile);
        when(profile.hasRole("ROLE_ADMIN")).thenReturn(true);
        when(userService.findById(1L)).thenReturn(Optional.of(user));
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(user));

        boolean result = gdprService.canDeleteUser(1L);
        assertFalse(result);
        verify(profile).hasRole("ROLE_ADMIN");
    }

    @Test
    void testCanDeleteUser_NotAdmin() {
        User user = mock(User.class);
        when(user.getId()).thenReturn(2L);
        when(user.getUsername()).thenReturn("user2");
        when(user.getProfile()).thenReturn(profile);
        when(profile.hasRole("ROLE_ADMIN")).thenReturn(false);
        when(userService.findById(2L)).thenReturn(Optional.of(user));

        boolean result = gdprService.canDeleteUser(2L);
        assertTrue(result);
        verify(profile).hasRole("ROLE_ADMIN");
    }
}
