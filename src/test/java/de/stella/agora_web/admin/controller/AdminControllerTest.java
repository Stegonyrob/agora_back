package de.stella.agora_web.admin.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.stella.agora_web.admin.controller.dto.AdminCreateDTO;
import de.stella.agora_web.config.TestConfig;
import de.stella.agora_web.roles.model.Role;
import de.stella.agora_web.roles.repository.RoleRepository;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.repository.UserRepository;

/**
 * Tests para AdminController - Gestión administrativa de usuarios
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
@TestPropertySource(properties = {
    "spring.h2.console.enabled=false",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Import(TestConfig.class)
@Transactional
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private User testAdmin;
    private Role adminRole;
    private Role userRole;

    @BeforeEach
    void setUp() {
        // Limpiar repositorios
        userRepository.deleteAll();
        roleRepository.deleteAll();

        // Crear roles
        adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");
        adminRole = roleRepository.save(adminRole);

        userRole = new Role();
        userRole.setName("ROLE_USER");
        userRole = roleRepository.save(userRole);

        // Crear admin de prueba
        testAdmin = new User();
        testAdmin.setUsername("testadmin");
        testAdmin.setEmail("admin@test.com");
        testAdmin.setPassword("password123");
        testAdmin.getRoles().add(adminRole);
        testAdmin = userRepository.save(testAdmin);
    }

    // ============ TESTS CREATE ADMIN ============
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateAdmin_WithValidData_ShouldReturn200() throws Exception {
        AdminCreateDTO adminCreateDTO = new AdminCreateDTO();
        adminCreateDTO.setUsername("newadmin");
        adminCreateDTO.setEmail("newadmin@test.com");
        adminCreateDTO.setPassword("password123");
        adminCreateDTO.setFirstName("Nuevo");

        mockMvc.perform(post("/api/v1/admin/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adminCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is("newadmin")))
                .andExpect(jsonPath("$.email", is("newadmin@test.com")))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testCreateAdmin_WithUserRole_ShouldReturn403() throws Exception {
        AdminCreateDTO adminCreateDTO = new AdminCreateDTO();
        adminCreateDTO.setUsername("newadmin");
        adminCreateDTO.setEmail("newadmin@test.com");
        adminCreateDTO.setPassword("password123");

        mockMvc.perform(post("/api/v1/admin/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adminCreateDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testCreateAdmin_WithoutAuthentication_ShouldReturn401() throws Exception {
        AdminCreateDTO adminCreateDTO = new AdminCreateDTO();
        adminCreateDTO.setUsername("newadmin");
        adminCreateDTO.setEmail("newadmin@test.com");
        adminCreateDTO.setPassword("password123");

        mockMvc.perform(post("/api/v1/admin/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adminCreateDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateAdmin_WithInvalidData_ShouldReturn400() throws Exception {
        AdminCreateDTO invalidAdminDTO = new AdminCreateDTO();
        // Faltan campos requeridos
        invalidAdminDTO.setUsername(""); // Username vacío
        invalidAdminDTO.setEmail("invalid-email"); // Email inválido

        mockMvc.perform(post("/api/v1/admin/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidAdminDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateAdmin_WithDuplicateUsername_ShouldReturn400() throws Exception {
        AdminCreateDTO adminCreateDTO = new AdminCreateDTO();
        adminCreateDTO.setUsername("testadmin"); // Username ya existe
        adminCreateDTO.setEmail("another@test.com");
        adminCreateDTO.setPassword("password123");

        mockMvc.perform(post("/api/v1/admin/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adminCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    // ============ TESTS GET ALL ADMINS ============
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAllAdmins_ShouldReturn200WithAdminsList() throws Exception {
        mockMvc.perform(get("/api/v1/admin"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].username", is("testadmin")))
                .andExpect(jsonPath("$[0].email", is("admin@test.com")));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetAllAdmins_WithUserRole_ShouldReturn403() throws Exception {
        mockMvc.perform(get("/api/v1/admin"))
                .andExpect(status().isForbidden());
    }

    // ============ TESTS GET ADMIN BY ID ============
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAdminById_ValidId_ShouldReturn200() throws Exception {
        mockMvc.perform(get("/api/v1/admin/{id}", testAdmin.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testAdmin.getId().intValue())))
                .andExpect(jsonPath("$.username", is("testadmin")))
                .andExpect(jsonPath("$.email", is("admin@test.com")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAdminById_InvalidId_ShouldReturn404() throws Exception {
        mockMvc.perform(get("/api/v1/admin/{id}", 99999L))
                .andExpect(status().isNotFound());
    }

    // ============ TESTS UPDATE ADMIN ============
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateAdmin_WithValidData_ShouldReturn200() throws Exception {
        AdminCreateDTO updateDTO = new AdminCreateDTO();
        updateDTO.setUsername("updatedadmin");
        updateDTO.setEmail("updated@test.com");
        updateDTO.setPassword("newpassword123");
        updateDTO.setFirstName("Updated");

        mockMvc.perform(put("/api/v1/admin/{id}", testAdmin.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is("updatedadmin")))
                .andExpect(jsonPath("$.email", is("updated@test.com")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateAdmin_InvalidId_ShouldReturn404() throws Exception {
        AdminCreateDTO updateDTO = new AdminCreateDTO();
        updateDTO.setUsername("nonexistent");
        updateDTO.setEmail("none@test.com");
        updateDTO.setPassword("password123");

        mockMvc.perform(put("/api/v1/admin/{id}", 99999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isNotFound());
    }

    // ============ TESTS DELETE ADMIN ============
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteAdmin_ValidId_ShouldReturn200() throws Exception {
        mockMvc.perform(delete("/api/v1/admin/{id}", testAdmin.getId()))
                .andExpect(status().isOk());

        // Verificar que el admin fue eliminado
        mockMvc.perform(get("/api/v1/admin/{id}", testAdmin.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteAdmin_InvalidId_ShouldReturn404() throws Exception {
        mockMvc.perform(delete("/api/v1/admin/{id}", 99999L))
                .andExpect(status().isNotFound());
    }

    // ============ TESTS DEMOTE ADMIN ============
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDemoteAdminToUser_ValidId_ShouldReturn200() throws Exception {
        mockMvc.perform(post("/api/v1/admin/demote/{id}", testAdmin.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDemoteAdminToUser_InvalidId_ShouldReturn404() throws Exception {
        mockMvc.perform(post("/api/v1/admin/demote/{id}", 99999L))
                .andExpect(status().isNotFound());
    }

    // ============ TESTS 2FA OPERATIONS ============
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAdminTotpSecret_ValidId_ShouldReturn200() throws Exception {
        mockMvc.perform(get("/api/v1/admin/{id}/2fa-secret", testAdmin.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAdminTotpSecret_InvalidId_ShouldReturn404() throws Exception {
        mockMvc.perform(get("/api/v1/admin/{id}/2fa-secret", 99999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testValidateAdminTotp_ValidId_ShouldReturn200() throws Exception {
        String totpCode = "123456"; // Código TOTP de prueba

        mockMvc.perform(post("/api/v1/admin/{id}/2fa-validate", testAdmin.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"" + totpCode + "\""))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testValidateAdminTotp_InvalidId_ShouldReturn404() throws Exception {
        String totpCode = "123456";

        mockMvc.perform(post("/api/v1/admin/{id}/2fa-validate", 99999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"" + totpCode + "\""))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testValidateAdminTotp_WithInvalidCode_ShouldReturn200WithFalse() throws Exception {
        String invalidCode = "000000";

        mockMvc.perform(post("/api/v1/admin/{id}/2fa-validate", testAdmin.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"" + invalidCode + "\""))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(false))); // Debería retornar false
    }

    // ============ TESTS SECURITY ============
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testAllAdminEndpoints_WithUserRole_ShouldReturn403() throws Exception {
        // Test múltiples endpoints con rol USER
        mockMvc.perform(get("/api/v1/admin")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/v1/admin/1")).andExpect(status().isForbidden());
        mockMvc.perform(post("/api/v1/admin/create")).andExpect(status().isForbidden());
        mockMvc.perform(put("/api/v1/admin/1")).andExpect(status().isForbidden());
        mockMvc.perform(delete("/api/v1/admin/1")).andExpect(status().isForbidden());
        mockMvc.perform(post("/api/v1/admin/demote/1")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/v1/admin/1/2fa-secret")).andExpect(status().isForbidden());
        mockMvc.perform(post("/api/v1/admin/1/2fa-validate")).andExpect(status().isForbidden());
    }

    @Test
    void testAllAdminEndpoints_WithoutAuthentication_ShouldReturn401() throws Exception {
        // Test múltiples endpoints sin autenticación
        mockMvc.perform(get("/api/v1/admin")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/v1/admin/1")).andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/v1/admin/create")).andExpect(status().isUnauthorized());
        mockMvc.perform(put("/api/v1/admin/1")).andExpect(status().isUnauthorized());
        mockMvc.perform(delete("/api/v1/admin/1")).andExpect(status().isUnauthorized());
    }
}
