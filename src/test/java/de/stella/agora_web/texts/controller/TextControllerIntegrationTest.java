package de.stella.agora_web.texts.controller;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

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
class TextControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TextRepository textItemRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        textItemRepository.deleteAll();
    }

//     @Test
//     void testCreateAndGetText() throws Exception {
//         TextItemDTO createDto = new TextItemDTO();
//         createDto.setTitle("Test Title");
//         createDto.setDescription("Test Desc");
//         createDto.setImage("img.png");
//         createDto.setNameImage("nameImg");
//         // Create
//         ResultActions result = mockMvc.perform(
//                 post("/api/v1/texts")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(createDto))
//         );
//         result.andExpect(status().isCreated());
//         String responseBody = result.andReturn().getResponse().getContentAsString();
//         TextItemDTO createdDto = objectMapper.readValue(responseBody, TextItemDTO.class);
//         // Get
//         mockMvc.perform(get("/api/v1/texts/" + createdDto.getId()))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.title").value("Test Title"))
//                 .andExpect(jsonPath("$.description").value("Test Desc"));
//     }
//     // @Test
//     // void testUpdateText() throws Exception {
//     //     TextItem item = new TextItem();
//     //     item.setTitle("Old Title");
//     //     item.setDescription("Old Desc");
//     //     item.setImage("old.png");
//     //     item.setNameImage("oldName");
//     //     item = textItemRepository.save(item);
//     //     TextItemDTO dto = new TextItemDTO();
//     //     dto.setTitle("New Title");
//     //     dto.setDescription("New Desc");
//     //     dto.setImage("new.png");
//     //     dto.setNameImage("newName");
//     //     mockMvc.perform(put("/api/v1/texts/" + item.getId())
//     //             .contentType(MediaType.APPLICATION_JSON)
//     //             .content(objectMapper.writeValueAsString(dto)))
//     //             .andExpect(status().isOk())
//     //             .andExpect(jsonPath("$.title").value("New Title"))
//     //             .andExpect(jsonPath("$.description").value("New Desc"));
//     // }
//     // @Test
//     // void testDeleteText() throws Exception {
//     //     TextItem item = new TextItem();
//     //     item.setTitle("Title");
//     //     item.setDescription("Desc");
//     //     item.setImage("img.png");
//     //     item.setNameImage("nameImg");
//     //     item = textItemRepository.save(item);
//     //     mockMvc.perform(delete("/api/v1/texts/" + item.getId()))
//     //             .andExpect(status().isNoContent());
//     //     assertFalse(textItemRepository.findById(item.getId()).isPresent());
//     // }
}
