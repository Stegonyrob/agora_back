package de.stella.agora_web.tags.controller;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.stella.agora_web.tags.dto.PostSummaryDTO;
import de.stella.agora_web.tags.dto.TagListDTO;
import de.stella.agora_web.tags.dto.TagSummaryDTO;
import de.stella.agora_web.tags.model.Tag;
import de.stella.agora_web.tags.service.ITagService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
@TestPropertySource(properties = {
    "spring.h2.console.enabled=false"
})
public class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ITagService tagService;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${api-endpoint}")
    private String apiEndpoint;

    private TagSummaryDTO tagSummaryDTO;
    private PostSummaryDTO postSummaryDTO;
    private Tag tag;

    @BeforeEach
    void setUp() {
        tagSummaryDTO = new TagSummaryDTO();
        tagSummaryDTO.setId(1L);
        tagSummaryDTO.setName("test-tag");
        tagSummaryDTO.setArchived(false);

        postSummaryDTO = new PostSummaryDTO();
        postSummaryDTO.setId(1L);
        postSummaryDTO.setTitle("Test Post");

        tag = new Tag();
        tag.setId(1L);
        tag.setName("created-tag");
        tag.setArchived(false);
    }

    @Test
    @WithMockUser(roles = "USER")
    void testAddTagsToPost_Success() throws Exception {
        doNothing().when(tagService).addTagToPost(anyLong(), anyString());

        List<String> tagNames = Arrays.asList("tag1", "tag2", "tag3");

        mockMvc.perform(post(apiEndpoint + "/any/tags/posts/1/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagNames)))
                .andExpect(status().isOk())
                .andExpect(content().string("Tags asociados correctamente al post 1"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testAddTagsToPost_WithTagListDTO_Success() throws Exception {
        doNothing().when(tagService).addTagToPost(anyLong(), anyString());

        TagListDTO tagListDTO = new TagListDTO();
        tagListDTO.setTags(Arrays.asList(tagSummaryDTO));

        mockMvc.perform(post(apiEndpoint + "/any/tags/posts/1/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagListDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Tags asociados correctamente al post 1"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testAddTagsToPost_EmptyList_BadRequest() throws Exception {
        List<String> emptyTags = Arrays.asList();

        mockMvc.perform(post(apiEndpoint + "/any/tags/posts/1/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyTags)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No se recibieron tags para asociar"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testAddTagsToEvent_Success() throws Exception {
        doNothing().when(tagService).addTagToEvent(anyLong(), anyString());

        TagListDTO tagListDTO = new TagListDTO();
        tagListDTO.setTags(Arrays.asList(tagSummaryDTO));

        mockMvc.perform(post(apiEndpoint + "/any/tags/events/1/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagListDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Tags asociados correctamente al evento 1"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testRemoveAllTagsFromPost_Success() throws Exception {
        doNothing().when(tagService).removeAllTagsFromPost(anyLong());

        mockMvc.perform(delete(apiEndpoint + "/any/tags/posts/1/tags"))
                .andExpect(status().isOk())
                .andExpect(content().string("Todas las tags eliminadas del post 1"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testRemoveAllTagsFromEvent_Success() throws Exception {
        doNothing().when(tagService).removeAllTagsFromEvent(anyLong());

        mockMvc.perform(delete(apiEndpoint + "/any/tags/events/1/tags"))
                .andExpect(status().isOk())
                .andExpect(content().string("Todas las tags eliminadas del evento 1"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetPostsByTagName_Success() throws Exception {
        when(tagService.getPostsSummaryByTagName(anyString())).thenReturn(Arrays.asList(postSummaryDTO));

        mockMvc.perform(get(apiEndpoint + "/any/tags/posts/test-tag"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Test Post"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetTagsByPostId_Success() throws Exception {
        when(tagService.getTagsByPostId(anyLong())).thenReturn(Arrays.asList(tagSummaryDTO));

        mockMvc.perform(get(apiEndpoint + "/any/tags/posts/1/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("test-tag"))
                .andExpect(jsonPath("$[0].archived").value(false));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetTagsByEvent_Success() throws Exception {
        when(tagService.getTagsByEventId(anyLong())).thenReturn(Arrays.asList(tagSummaryDTO));

        mockMvc.perform(get(apiEndpoint + "/any/tags/events/1/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("test-tag"))
                .andExpect(jsonPath("$[0].archived").value(false));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateTag_Success() throws Exception {
        when(tagService.createTag(anyString())).thenReturn(tag);

        Tag newTag = new Tag();
        newTag.setName("new-tag");

        mockMvc.perform(post(apiEndpoint + "/any/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTag)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("created-tag"))
                .andExpect(jsonPath("$.archived").value(false));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testAddSpecificTagToPost_Success() throws Exception {
        doNothing().when(tagService).addTagToPost(anyLong(), anyString());

        mockMvc.perform(post(apiEndpoint + "/any/tags/posts/1/tags/test-tag"))
                .andExpect(status().isOk())
                .andExpect(content().string("Tag 'test-tag' agregado al post 1"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testAddSpecificTagToEvent_Success() throws Exception {
        doNothing().when(tagService).addTagToEvent(anyLong(), anyString());

        mockMvc.perform(post(apiEndpoint + "/any/tags/events/1/tags/test-tag"))
                .andExpect(status().isOk())
                .andExpect(content().string("Tag 'test-tag' agregado al evento 1"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testRemoveSpecificTagFromPost_Success() throws Exception {
        doNothing().when(tagService).removeTagFromPost(anyLong(), anyString());

        mockMvc.perform(delete(apiEndpoint + "/any/tags/posts/1/tags/test-tag"))
                .andExpect(status().isOk())
                .andExpect(content().string("Tag 'test-tag' eliminado del post 1"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testRemoveSpecificTagFromEvent_Success() throws Exception {
        doNothing().when(tagService).removeTagFromEvent(anyLong(), anyString());

        mockMvc.perform(delete(apiEndpoint + "/any/tags/events/1/tags/test-tag"))
                .andExpect(status().isOk())
                .andExpect(content().string("Tag 'test-tag' eliminado del evento 1"));
    }
}
