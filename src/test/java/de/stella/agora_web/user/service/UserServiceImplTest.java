package de.stella.agora_web.user.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.repository.UserRepository;
import de.stella.agora_web.user.service.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl service;

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
