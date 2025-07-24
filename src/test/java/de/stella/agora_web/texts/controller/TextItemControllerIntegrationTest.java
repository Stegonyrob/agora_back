package de.stella.agora_web.texts.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.stella.agora_web.texts.controller.dto.TextItemDTO;
import de.stella.agora_web.texts.model.TextItem;
import de.stella.agora_web.texts.repository.TextItemRepository;

@SpringBootTest
@AutoConfigureMockMvc
class TextItemControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TextItemRepository textItemRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        textItemRepository.deleteAll();
    }

    @Test
    void testCreateAndGetText() throws Exception {
        TextItemDTO dto = new TextItemDTO();
        dto.setTitle("Test Title");
        dto.setDescription("Test Desc");
        dto.setImage("img.png");
        dto.setNameImage("nameImg");

        // Create
        ResultActions createResult = mockMvc.perform(post("/api/v1/texts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));
        createResult.andExpect(status().isCreated());
        String response = createResult.andReturn().getResponse().getContentAsString();
        TextItemDTO created = objectMapper.readValue(response, TextItemDTO.class);

        // Get
        mockMvc.perform(get("/api/v1/texts/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.description").value("Test Desc"));
    }

    @Test
    void testUpdateText() throws Exception {
        TextItem item = new TextItem();
        item.setTitle("Old Title");
        item.setDescription("Old Desc");
        item.setImage("old.png");
        item.setNameImage("oldName");
        item = textItemRepository.save(item);

        TextItemDTO dto = new TextItemDTO();
        dto.setTitle("New Title");
        dto.setDescription("New Desc");
        dto.setImage("new.png");
        dto.setNameImage("newName");

        mockMvc.perform(put("/api/v1/texts/" + item.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Title"))
                .andExpect(jsonPath("$.description").value("New Desc"));
    }

    @Test
    void testDeleteText() throws Exception {
        TextItem item = new TextItem();
        item.setTitle("Title");
        item.setDescription("Desc");
        item.setImage("img.png");
        item.setNameImage("nameImg");
        item = textItemRepository.save(item);

        mockMvc.perform(delete("/api/v1/texts/" + item.getId()))
                .andExpect(status().isNoContent());
        assertFalse(textItemRepository.findById(item.getId()).isPresent());
    }
}
