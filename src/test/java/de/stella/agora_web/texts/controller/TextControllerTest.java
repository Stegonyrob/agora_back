package de.stella.agora_web.texts.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.stella.agora_web.config.TestConfig;
import de.stella.agora_web.texts.controller.dto.TextDTO;
import de.stella.agora_web.texts.model.Text;
import de.stella.agora_web.texts.repository.TextRepository;

/**
 * Tests para TextController - CRUD de textos con autorización
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
public class TextControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TextRepository textRepository;

    @Value("${api-endpoint}")
    private String apiEndpoint;

    private Text testText;

    @BeforeEach
    void setUp() {
        textRepository.deleteAll();

        // Crear texto de prueba
        testText = new Text();
        testText.setCategory("educacion");
        testText.setTitle("Texto de Prueba");
        testText.setMessage("Este es un mensaje de prueba para testing.");
        testText.setArchived(false);
        testText = textRepository.save(testText);
    }

    // ============ TESTS GET ALL TEXTS ============
    @Test
    void testGetAllTexts_ShouldReturn200WithTextsList() throws Exception {
        mockMvc.perform(get(apiEndpoint + "/all/texts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id", is(testText.getId().intValue())))
                .andExpect(jsonPath("$[0].title", is("Texto de Prueba")))
                .andExpect(jsonPath("$[0].category", is("educacion")));
    }

    @Test
    void testGetAllTexts_EmptyDatabase_ShouldReturnEmptyArray() throws Exception {
        textRepository.deleteAll();

        mockMvc.perform(get(apiEndpoint + "/all/texts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()", is(0)));
    }

    // ============ TESTS GET TEXT BY ID ============
    @Test
    void testGetTextById_ValidId_ShouldReturn200WithText() throws Exception {
        mockMvc.perform(get(apiEndpoint + "/all/texts/{id}", testText.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testText.getId().intValue())))
                .andExpect(jsonPath("$.title", is("Texto de Prueba")))
                .andExpect(jsonPath("$.message", is("Este es un mensaje de prueba para testing.")))
                .andExpect(jsonPath("$.category", is("educacion")))
                .andExpect(jsonPath("$.archived", is(false)));
    }

    @Test
    void testGetTextById_InvalidId_ShouldReturn404() throws Exception {
        mockMvc.perform(get(apiEndpoint + "/all/texts/{id}", 99999L))
                .andExpect(status().isNotFound());
    }

    // ============ TESTS CREATE TEXT (ADMIN ONLY) ============
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateText_WithAdminRole_ShouldReturn201() throws Exception {
        TextDTO newTextDTO = new TextDTO();
        newTextDTO.setCategory("tecnologia");
        newTextDTO.setTitle("Nuevo Texto");
        newTextDTO.setMessage("Este es un nuevo texto creado en test.");

        mockMvc.perform(post(apiEndpoint + "/all/texts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTextDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is("Nuevo Texto")))
                .andExpect(jsonPath("$.category", is("tecnologia")))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.createdAt", notNullValue()));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testCreateText_WithUserRole_ShouldReturn403() throws Exception {
        TextDTO newTextDTO = new TextDTO();
        newTextDTO.setCategory("tecnologia");
        newTextDTO.setTitle("Nuevo Texto");
        newTextDTO.setMessage("Este es un nuevo texto creado en test.");

        mockMvc.perform(post(apiEndpoint + "/all/texts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTextDTO)))
                .andExpect(status().isForbidden());
    }

    // ============ TESTS UPDATE TEXT (ADMIN ONLY) ============
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateText_WithAdminRole_ShouldReturn200() throws Exception {
        TextDTO updateDTO = new TextDTO();
        updateDTO.setCategory("educacion-actualizada");
        updateDTO.setTitle("Texto Actualizado");
        updateDTO.setMessage("Este es un mensaje actualizado.");

        mockMvc.perform(put(apiEndpoint + "/all/texts/{id}", testText.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Texto Actualizado")))
                .andExpect(jsonPath("$.category", is("educacion-actualizada")))
                .andExpect(jsonPath("$.message", is("Este es un mensaje actualizado.")));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testUpdateText_WithUserRole_ShouldReturn403() throws Exception {
        TextDTO updateDTO = new TextDTO();
        updateDTO.setTitle("Intento de actualización");
        updateDTO.setMessage("No debería funcionar.");

        mockMvc.perform(put(apiEndpoint + "/all/texts/{id}", testText.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isForbidden());
    }

    // ============ TESTS ARCHIVE TEXT (ADMIN ONLY) ============
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testArchiveText_ShouldReturn204() throws Exception {
        mockMvc.perform(patch(apiEndpoint + "/all/texts/{id}/archive", testText.getId())
                .param("archive", "true"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testArchiveText_WithUserRole_ShouldReturn403() throws Exception {
        mockMvc.perform(patch(apiEndpoint + "/all/texts/{id}/archive", testText.getId())
                .param("archive", "true"))
                .andExpect(status().isForbidden());
    }
}
