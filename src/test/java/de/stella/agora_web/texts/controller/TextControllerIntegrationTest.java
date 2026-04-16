package de.stella.agora_web.texts.controller;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

import de.stella.agora_web.config.TestConfig;
import de.stella.agora_web.texts.repository.TextRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
@TestPropertySource(properties = {
    "spring.h2.console.enabled=false",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Import(TestConfig.class)
@Transactional
class TextControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TextRepository textItemRepository;

    @BeforeEach
    public void setUp() {
        textItemRepository.deleteAll();
    }

    @Test
    void testGetAllTexts_ShouldReturnEmptyListWhenNoTexts() throws Exception {
        mockMvc.perform(get("/api/all/texts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetTextById_ShouldReturnNotFoundWhenMissing() throws Exception {
        mockMvc.perform(get("/api/all/texts/{id}", 99999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testDeleteText_ShouldReturnNotFoundWhenMissing() throws Exception {
        mockMvc.perform(delete("/api/all/texts/{id}", 99999L))
                .andExpect(status().isNotFound());
    }
}
