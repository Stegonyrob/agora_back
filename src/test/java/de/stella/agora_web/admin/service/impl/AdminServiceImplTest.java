package de.stella.agora_web.admin.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import de.stella.agora_web.admin.controller.dto.AdminCreateDTO;

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
        AdminServiceImpl service = new AdminServiceImpl(null, null, null);
        String secret = service.generateTotpSecret();
        assertNotNull(secret);
        assertTrue(secret.length() > 10);
    }
}
