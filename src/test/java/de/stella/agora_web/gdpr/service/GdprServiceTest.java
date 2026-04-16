package de.stella.agora_web.gdpr.service;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import de.stella.agora_web.banned.service.IBannedService;
import de.stella.agora_web.comment.service.ICommentService;
import de.stella.agora_web.posts.service.IPostService;
import de.stella.agora_web.replies.service.IReplyService;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.service.impl.UserServiceImpl;

@ActiveProfiles("h2")
@TestPropertySource(properties = {
    "spring.h2.console.enabled=false"
})
@ExtendWith(MockitoExtension.class)
class GdprServiceTest {

    @Mock
    private UserServiceImpl userService;
    @Mock
    @SuppressWarnings("unused") // required for @InjectMocks to inject into GdprService
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
