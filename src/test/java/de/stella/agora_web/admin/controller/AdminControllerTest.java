// Removed misplaced @MockBean fields from top of file. All @MockBean fields are now inside the class body.
package de.stella.agora_web.admin.controller;

import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
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

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${api-endpoint}")
    private String apiEndpoint;

    @Value("${admin.email}")
    private String adminEmail;

    @MockBean
    private de.stella.agora_web.admin.service.impl.AdminServiceImpl adminService;
    private String adminUsername;
    private String adminPassword;
    private Long validAdminId;

    @BeforeEach
    void setUp() {
        // Usar datos de entorno/configuración para el usuario admin
        adminUsername = "admin_test";
        adminPassword = "securePassword123";
        validAdminId = 1L; // Simulación, en entorno real se obtiene de la BD
    }

    @Test
    @WithMockUser(username = "admin_test", roles = {"ADMIN"})
    void testCreateAdmin_WithValidData_ShouldReturn200() throws Exception {
        AdminCreateDTO adminCreateDTO = new AdminCreateDTO();
        adminCreateDTO.setUsername("nuevo_admin");
        adminCreateDTO.setPassword("password123");
        adminCreateDTO.setConfirmPassword("password123");
        adminCreateDTO.setEmail(adminEmail);
        adminCreateDTO.setPhone("600123456");
        adminCreateDTO.setFirstName("Nuevo");
        adminCreateDTO.setLastName1("Admin");
        adminCreateDTO.setCity("Madrid");
        adminCreateDTO.setCountry("España");

        mockMvc.perform(post(apiEndpoint + "/admin/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adminCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.profile.username", is("nuevo_admin")))
                .andExpect(jsonPath("$.profile.email", is(adminEmail)))
                .andExpect(jsonPath("$.admin", is(true)));
    }

    @Test
    @WithMockUser(username = "admin_test", roles = {"ADMIN"})
    void testGetAllAdmins_ShouldReturn200WithAdminsList() throws Exception {
        mockMvc.perform(get(apiEndpoint + "/admin"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(username = "admin_test", roles = {"ADMIN"})
    void testGetAdminById_ValidId_ShouldReturn200() throws Exception {
        mockMvc.perform(get(apiEndpoint + "/admin/{id}", validAdminId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username = "admin_test", roles = {"ADMIN"})
    void testUpdateAdmin_WithValidData_ShouldReturn200() throws Exception {
        AdminCreateDTO updateDTO = new AdminCreateDTO();
        updateDTO.setUsername("admin_actualizado");
        updateDTO.setPassword("newPassword123");
        updateDTO.setConfirmPassword("newPassword123");
        updateDTO.setEmail(adminEmail);
        updateDTO.setPhone("600654321");
        updateDTO.setFirstName("Actualizado");
        updateDTO.setLastName1("Admin");
        updateDTO.setCity("Barcelona");
        updateDTO.setCountry("España");

        mockMvc.perform(put(apiEndpoint + "/admin/{id}", validAdminId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.profile.username", is("admin_actualizado")))
                .andExpect(jsonPath("$.profile.email", is(adminEmail)));
    }

    @Test
    @WithMockUser(username = "admin_test", roles = {"ADMIN"})
    void testDeleteAdmin_ValidId_ShouldReturn200() throws Exception {
        mockMvc.perform(delete(apiEndpoint + "/admin/{id}", validAdminId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin_test", roles = {"ADMIN"})
    void testDemoteAdminToUser_ValidId_ShouldReturn200() throws Exception {
        mockMvc.perform(post(apiEndpoint + "/admin/demote/{id}", validAdminId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin_test", roles = {"ADMIN"})
    void testGetAdminTotpSecret_ValidId_ShouldReturn200() throws Exception {
        mockMvc.perform(get(apiEndpoint + "/admin/{id}/2fa-secret", validAdminId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "admin_test", roles = {"ADMIN"})
    void testValidateAdminTotp_ValidId_ShouldReturn200() throws Exception {
        String totpCode = "123456";
        mockMvc.perform(post(apiEndpoint + "/admin/{id}/2fa-validate", validAdminId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"" + totpCode + "\""))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username = "user_test", roles = {"USER"})
    void testAllAdminEndpoints_WithUserRole_ShouldReturn403() throws Exception {
        mockMvc.perform(get(apiEndpoint + "/admin")).andExpect(status().isForbidden());
        mockMvc.perform(get(apiEndpoint + "/admin/1")).andExpect(status().isForbidden());
        mockMvc.perform(post(apiEndpoint + "/admin/create")).andExpect(status().isForbidden());
        mockMvc.perform(put(apiEndpoint + "/admin/1")).andExpect(status().isForbidden());
        mockMvc.perform(delete(apiEndpoint + "/admin/1")).andExpect(status().isForbidden());
        mockMvc.perform(post(apiEndpoint + "/admin/demote/1")).andExpect(status().isForbidden());
        mockMvc.perform(get(apiEndpoint + "/admin/1/2fa-secret")).andExpect(status().isForbidden());
        mockMvc.perform(post(apiEndpoint + "/admin/1/2fa-validate")).andExpect(status().isForbidden());
    }

    @Test
    void testAllAdminEndpoints_WithoutAuthentication_ShouldReturn401() throws Exception {
        mockMvc.perform(get(apiEndpoint + "/admin")).andExpect(status().isUnauthorized());
        mockMvc.perform(get(apiEndpoint + "/admin/1")).andExpect(status().isUnauthorized());
        mockMvc.perform(post(apiEndpoint + "/admin/create")).andExpect(status().isUnauthorized());
        mockMvc.perform(put(apiEndpoint + "/admin/1")).andExpect(status().isUnauthorized());
        mockMvc.perform(delete(apiEndpoint + "/admin/1")).andExpect(status().isUnauthorized());
    }
}
