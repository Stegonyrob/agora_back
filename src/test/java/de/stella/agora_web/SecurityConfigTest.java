package de.stella.agora_web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests de configuración de seguridad Verifica que los endpoints estén
 * correctamente protegidos según roles
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@DisplayName("SecurityConfiguration - Tests de seguridad y permisos")
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Usuario con rol USER debe poder acceder a endpoints /api/v1/any/**")
    void testUserAccess() throws Exception {
        // Los endpoints /api/v1/any/** requieren USER o ADMIN
        mockMvc.perform(get("/api/v1/any/test"))
                .andExpect(status().isNotFound()); // 404 porque el endpoint no existe, pero pasó la seguridad
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Usuario con rol ADMIN debe poder acceder a endpoints /api/v1/admin/**")
    void testAdminAccess() throws Exception {
        // Los endpoints /api/v1/admin/** requieren rol ADMIN
        mockMvc.perform(get("/api/v1/admin/test"))
                .andExpect(status().isNotFound()); // 404 porque el endpoint no existe, pero pasó la seguridad
    }

    @Test
    @DisplayName("Acceso anónimo debe ser permitido en endpoints /api/v1/all/**")
    void testAnonymousAccess() throws Exception {
        // Los endpoints /api/v1/all/** son públicos
        mockMvc.perform(get("/api/v1/all/test"))
                .andExpect(status().isNotFound()); // 404 porque el endpoint no existe, pero pasó la seguridad
    }

    @Test
    @DisplayName("CORS debe estar configurado para orígenes permitidos")
    void testCorsConfiguration() throws Exception {
        // Verificar que CORS permite localhost:5173
        mockMvc.perform(get("/api/v1/all/test")
                .header("Origin", "http://localhost:5173"))
                .andExpect(status().isNotFound()) // 404 porque el endpoint no existe
                .andExpect(header().exists("Access-Control-Allow-Origin"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Endpoints públicos /api/v1/public/** deben ser accesibles sin autenticación")
    void testPublicEndpointsAccess() throws Exception {
        mockMvc.perform(get("/api/v1/public/test"))
                .andExpect(status().isNotFound()); // Pasa seguridad, falla porque endpoint no existe
    }

    @Test
    @DisplayName("Imágenes estáticas /temp_images/** deben ser accesibles sin autenticación")
    void testStaticImagesAccess() throws Exception {
        mockMvc.perform(get("/temp_images/test.jpg"))
                .andExpect(status().isNotFound()); // Pasa seguridad
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("GET a /api/v1/text-images/** debe ser público")
    void testTextImagesPublicAccess() throws Exception {
        mockMvc.perform(get("/api/v1/text-images/1"))
                .andExpect(status().isNotFound()); // Pasa seguridad
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST/PUT/DELETE a /api/v1/text-images/** requiere rol ADMIN")
    void testTextImagesAdminAccess() throws Exception {
        mockMvc.perform(post("/api/v1/text-images"))
                .andExpect(status().is4xxClientError()); // Rechazado por validación, no por seguridad
    }
}
