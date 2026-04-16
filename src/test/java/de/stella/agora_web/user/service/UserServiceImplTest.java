package de.stella.agora_web.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.repository.UserRepository;
import de.stella.agora_web.user.service.impl.UserServiceImpl;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // La creación de usuario se prueba en RegisterServiceTest
    @Test
    void testFindUserById() {
        User user = new User();
        user.setId(2L);
        user.setUsername("user2");
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        Optional<User> result = service.findUserById(2L);
        assertTrue(result.isPresent());
        assertEquals("user2", result.get().getUsername());
    }

    @Test
    void testFindUserByIdNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.findUserById(99L));
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById(3L);
        service.deleteById(3L);
        verify(userRepository).deleteById(3L);
    }
}
