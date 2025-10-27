package de.stella.agora_web.tags.controller;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.stella.agora_web.tags.dto.EventSummaryDTO;
import de.stella.agora_web.tags.dto.TagSummaryDTO;
import de.stella.agora_web.tags.service.ITagService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
public class PublicTagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ITagService tagService;

    @org.springframework.beans.factory.annotation.Value("${api-endpoint}")
    private String apiEndpoint;

    private TagSummaryDTO tagSummaryDTO;
    private EventSummaryDTO eventSummaryDTO;

    void setUp() {
        tagSummaryDTO = new TagSummaryDTO();
        tagSummaryDTO.setId(1L);
        tagSummaryDTO.setName("public-tag");

        eventSummaryDTO = new EventSummaryDTO();
        eventSummaryDTO.setId(1L);
        eventSummaryDTO.setTitle("Test Event");
    }

    // ========== TESTS PÚBLICOS DE LECTURA ==========
    @Test
    void testGetAllTags_Public_Success() throws Exception {
        List<TagSummaryDTO> tags = Arrays.asList(tagSummaryDTO);
        when(tagService.getAllTagsSummary()).thenReturn(tags);

        mockMvc.perform(get(apiEndpoint + "/all/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("public-tag"));
    }

    @Test
    void testGetAllTags_EmptyList_Success() throws Exception {
        when(tagService.getAllTagsSummary()).thenReturn(Arrays.asList());

        mockMvc.perform(get(apiEndpoint + "/all/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testGetEventsByTagName_Public_Success() throws Exception {
        List<EventSummaryDTO> events = Arrays.asList(eventSummaryDTO);
        when(tagService.getEventsSummaryByTagName("public-tag")).thenReturn(events);

        mockMvc.perform(get(apiEndpoint + "/all/tags/events/public-tag"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Event"));
    }

    @Test
    void testGetEventsByTagName_EmptyList_Success() throws Exception {
        when(tagService.getEventsSummaryByTagName("nonexistent-tag")).thenReturn(Arrays.asList());

        mockMvc.perform(get(apiEndpoint + "/all/tags/events/nonexistent-tag"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testGetEventsByTagName_SpecialCharacters_Success() throws Exception {
        List<EventSummaryDTO> events = Arrays.asList(eventSummaryDTO);
        when(tagService.getEventsSummaryByTagName("tag-with-special-chars")).thenReturn(events);

        mockMvc.perform(get(apiEndpoint + "/all/tags/events/tag-with-special-chars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    // ========== TESTS DE CASOS EDGE ==========
    @Test
    void testPublicEndpoints_NoAuthentication_Success() throws Exception {
        // Los endpoints públicos no requieren autenticación
        List<TagSummaryDTO> tags = Arrays.asList(tagSummaryDTO);
        when(tagService.getAllTagsSummary()).thenReturn(tags);

        mockMvc.perform(get(apiEndpoint + "/all/tags"))
                .andExpect(status().isOk());
    }

    @Test
    void testServiceException_InternalServerError() throws Exception {
        when(tagService.getAllTagsSummary())
                .thenThrow(new RuntimeException("Database connection error"));

        mockMvc.perform(get(apiEndpoint + "/all/tags"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testTagNameWithSpaces_Success() throws Exception {
        List<EventSummaryDTO> events = Arrays.asList(eventSummaryDTO);
        when(tagService.getEventsSummaryByTagName("tag with spaces")).thenReturn(events);

        // URL encoding para espacios (%20)
        mockMvc.perform(get(apiEndpoint + "/all/tags/events/tag%20with%20spaces"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testMultipleTags_Success() throws Exception {
        TagSummaryDTO tag1 = new TagSummaryDTO();
        tag1.setId(1L);
        tag1.setName("tag1");

        TagSummaryDTO tag2 = new TagSummaryDTO();
        tag2.setId(2L);
        tag2.setName("tag2");

        List<TagSummaryDTO> tags = Arrays.asList(tag1, tag2);
        when(tagService.getAllTagsSummary()).thenReturn(tags);

        mockMvc.perform(get(apiEndpoint + "/all/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("tag1"))
                .andExpect(jsonPath("$[1].name").value("tag2"));
    }

    @Test
    void testMultipleEvents_Success() throws Exception {
        EventSummaryDTO event1 = new EventSummaryDTO();
        event1.setId(1L);
        event1.setTitle("Event 1");

        EventSummaryDTO event2 = new EventSummaryDTO();
        event2.setId(2L);
        event2.setTitle("Event 2");

        List<EventSummaryDTO> events = Arrays.asList(event1, event2);
        when(tagService.getEventsSummaryByTagName("multi-event-tag")).thenReturn(events);

        mockMvc.perform(get(apiEndpoint + "/all/tags/events/multi-event-tag"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Event 1"))
                .andExpect(jsonPath("$[1].title").value("Event 2"));
    }
}
