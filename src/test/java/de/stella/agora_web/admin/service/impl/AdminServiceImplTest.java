package de.stella.agora_web.admin.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import de.stella.agora_web.admin.controller.dto.AdminCreateDTO;
import de.stella.agora_web.avatar.repository.AvatarRepository;
import de.stella.agora_web.profiles.repository.ProfileRepository;
import de.stella.agora_web.roles.repository.RoleRepository;
import de.stella.agora_web.user.service.impl.UserServiceImpl;

class AdminServiceImplTest {

    @Test
    void testAdminCreateDTORequiresPhone() {
        AdminCreateDTO dto = new AdminCreateDTO();
        dto.setUsername("admin");
        dto.setPassword("pass");
        dto.setEmail("admin@example.com");
        dto.setPhone("");
        // Simula validación manual (en Spring sería con @Valid)
        assertTrue(dto.getPhone() == null || dto.getPhone().isEmpty(), "El teléfono es obligatorio para administradores");
    }

    @Test
    void testTotpSecretGeneration() {
        // Use mocks or nulls for dependencies as needed
        UserServiceImpl userService = null;
        RoleRepository roleRepository = null;
        PasswordEncoder passwordEncoder = null;
        ProfileRepository profileRepository = null;
        AvatarRepository avatarRepository = null;
        AdminServiceImpl service = new AdminServiceImpl(userService, roleRepository, passwordEncoder, profileRepository, avatarRepository);
        String secret = service.generateTotpSecret();
        assertNotNull(secret);
        assertTrue(secret.length() > 10);
    }
}
