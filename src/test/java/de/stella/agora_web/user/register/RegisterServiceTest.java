package de.stella.agora_web.user.register;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import de.stella.agora_web.auth.SignUpDTO;
import de.stella.agora_web.roles.model.Role;
import de.stella.agora_web.roles.service.RoleService;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class RegisterServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleService roleService;
    @Mock
    private de.stella.agora_web.encryptations.EncoderFacade encoder;

    @InjectMocks
    private RegisterService registerService;

    @Test
    void testRegisterUser() {
        SignUpDTO dto = new SignUpDTO();
        dto.setUsername("user1");
        dto.setEmail("user1@gmail.com");
        dto.setPassword("password");
        when(encoder.encode(eq("bcrypt"), anyString())).thenReturn("encodedPassword");
        when(roleService.getById(2L)).thenReturn(new Role(2L, "ROLE_USER"));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User user = registerService.registerUser(dto);
        assertEquals("user1", user.getUsername());
        assertEquals("user1@gmail.com", user.getEmail());
        assertEquals("encodedPassword", user.getPassword());
        assertTrue(user.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_USER")));
    }

    @Test
    void testCreateUserAlreadyExists() {
        SignUpDTO dto = new SignUpDTO();
        dto.setUsername("user1");
        dto.setEmail("user1@gmail.com");
        dto.setPassword("password");
        User existing = new User();
        existing.setUsername("user1");
        existing.setEmail("user1@gmail.com");
        when(userRepository.findAll()).thenReturn(java.util.List.of(existing));

        String result = registerService.createUser(dto);
        assertTrue(result.contains("Error: Ya existe un usuario"));
    }
}
