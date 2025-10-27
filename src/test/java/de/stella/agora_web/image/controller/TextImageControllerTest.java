package de.stella.agora_web.image.controller;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.stella.agora_web.image.controller.dto.TextImageDTO;
import de.stella.agora_web.image.service.ITextImageService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
public class TextImageControllerTest {

    @org.springframework.beans.factory.annotation.Value("${api-endpoint}")
    private String apiEndpoint;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ITextImageService textImageService;

    private TextImageDTO textImageDTO;

    @BeforeEach
    void setUp() {
        textImageDTO = new TextImageDTO();
        textImageDTO.setId(1L);
        textImageDTO.setTextId(1L);
        textImageDTO.setImageName("test-text-image.jpg");
    }

    // ========== TESTS PÚBLICOS DE LECTURA ==========
    @Test
    void testGetAllTextImages_Public_Success() throws Exception {
        List<TextImageDTO> images = Arrays.asList(textImageDTO);
        when(textImageService.getAllTextImages()).thenReturn(images);

        mockMvc.perform(get(apiEndpoint + "/text-images"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].textId").value(1))
                .andExpect(jsonPath("$[0].imageName").value("test-text-image.jpg"));
    }

    @Test
    void testGetTextImage_Public_Success() throws Exception {
        when(textImageService.getTextImageById(1L)).thenReturn(textImageDTO);

        mockMvc.perform(get(apiEndpoint + "/text-images/image/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.textId").value(1))
                .andExpect(jsonPath("$.imageName").value("test-text-image.jpg"));
    }

    @Test
    void testGetImagesByTextId_Public_Success() throws Exception {
        List<TextImageDTO> images = Arrays.asList(textImageDTO);
        when(textImageService.getImagesByTextId(1L)).thenReturn(images);

        mockMvc.perform(get(apiEndpoint + "/text-images/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].textId").value(1));
    }

    @Test
    void testGetImagesByTextId_EmptyList_Success() throws Exception {
        when(textImageService.getImagesByTextId(1L)).thenReturn(Arrays.asList());

        mockMvc.perform(get(apiEndpoint + "/text-images/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    // ========== TESTS ADMINISTRATIVOS DE CREACIÓN ==========
    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateTextImage_Success() throws Exception {
        when(textImageService.createTextImage(any(TextImageDTO.class))).thenReturn(textImageDTO);

        mockMvc.perform(post(apiEndpoint + "/text-images")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(textImageDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.textId").value(1))
                .andExpect(jsonPath("$.imageName").value("test-text-image.jpg"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testCreateTextImage_Forbidden() throws Exception {
        mockMvc.perform(post(apiEndpoint + "/text-images")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(textImageDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUploadMultipleTextImages_Success() throws Exception {
        List<TextImageDTO> savedImages = Arrays.asList(textImageDTO);
        when(textImageService.processAndSaveImages(any(), anyLong())).thenReturn(savedImages);

        MockMultipartFile file1 = new MockMultipartFile("files", "image1.jpg", "image/jpeg", "image content".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("files", "image2.jpg", "image/jpeg", "image content 2".getBytes());

        mockMvc.perform(multipart(apiEndpoint + "/text-images/upload")
                .file(file1)
                .file(file2)
                .param("textId", "1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testUploadMultipleTextImages_Forbidden() throws Exception {
        MockMultipartFile file = new MockMultipartFile("files", "image.jpg", "image/jpeg", "content".getBytes());

        mockMvc.perform(multipart(apiEndpoint + "/text-images/upload")
                .file(file)
                .param("textId", "1"))
                .andExpect(status().isForbidden());
    }

    // ========== TESTS ADMINISTRATIVOS DE ELIMINACIÓN ==========
    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteTextImage_Success() throws Exception {
        doNothing().when(textImageService).deleteTextImage(1L);

        mockMvc.perform(delete(apiEndpoint + "/text-images/image/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testDeleteTextImage_Forbidden() throws Exception {
        mockMvc.perform(delete(apiEndpoint + "/text-images/image/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteMultipleTextImages_Success() throws Exception {
        doNothing().when(textImageService).deleteMultipleTextImages(any());

        List<Long> imageIds = Arrays.asList(1L, 2L, 3L);

        mockMvc.perform(delete(apiEndpoint + "/text-images/delete-multiple")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(imageIds)))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testDeleteMultipleTextImages_Forbidden() throws Exception {
        List<Long> imageIds = Arrays.asList(1L, 2L);

        mockMvc.perform(delete(apiEndpoint + "/text-images/delete-multiple")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(imageIds)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteImagesByText_Success() throws Exception {
        doNothing().when(textImageService).deleteImagesByTextId(1L);

        mockMvc.perform(delete(apiEndpoint + "/text-images/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testDeleteImagesByText_Forbidden() throws Exception {
        mockMvc.perform(delete(apiEndpoint + "/text-images/1"))
                .andExpect(status().isForbidden());
    }

    // ========== TESTS DE CASOS EDGE ==========
    @Test
    void testUnauthorizedAccess_AdminEndpoints() throws Exception {
        mockMvc.perform(post(apiEndpoint + "/text-images"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(delete(apiEndpoint + "/text-images/image/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUploadMultipleTextImages_ServiceException() throws Exception {
        when(textImageService.processAndSaveImages(any(), anyLong()))
                .thenThrow(new RuntimeException("Processing error"));

        MockMultipartFile file = new MockMultipartFile("files", "image.jpg", "image/jpeg", "content".getBytes());

        mockMvc.perform(multipart(apiEndpoint + "/text-images/upload")
                .file(file)
                .param("textId", "1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteMultipleTextImages_ServiceException() throws Exception {
        doThrow(new RuntimeException("Delete error")).when(textImageService).deleteMultipleTextImages(any());

        List<Long> imageIds = Arrays.asList(1L);

        mockMvc.perform(delete(apiEndpoint + "/text-images/delete-multiple")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(imageIds)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testPublicEndpoints_NoAuthenticationRequired() throws Exception {
        List<TextImageDTO> images = Arrays.asList(textImageDTO);
        when(textImageService.getAllTextImages()).thenReturn(images);

        // Los endpoints públicos no requieren autenticación
        mockMvc.perform(get(apiEndpoint + "/text-images"))
                .andExpect(status().isOk());

        when(textImageService.getTextImageById(1L)).thenReturn(textImageDTO);
        mockMvc.perform(get(apiEndpoint + "/text-images/image/1"))
                .andExpect(status().isOk());

        when(textImageService.getImagesByTextId(1L)).thenReturn(images);
        mockMvc.perform(get(apiEndpoint + "/text-images/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllTextImages_EmptyList_Success() throws Exception {
        when(textImageService.getAllTextImages()).thenReturn(Arrays.asList());

        mockMvc.perform(get(apiEndpoint + "/text-images"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
}
