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
import org.springframework.beans.factory.annotation.Value;
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

import de.stella.agora_web.image.controller.dto.EventImageDTO;
import de.stella.agora_web.image.controller.dto.ImageIdListDTO;
import de.stella.agora_web.image.service.IEventImageService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
public class EventImageControllerTest {

    @Value("${api-endpoint}")
    private String apiEndpoint;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IEventImageService eventImageService;

    private EventImageDTO eventImageDTO;

    @BeforeEach
    void setUp() {
        eventImageDTO = new EventImageDTO();
        eventImageDTO.setId(1L);
        eventImageDTO.setEventId(1L);
        eventImageDTO.setImageName("test-event-image.jpg");
    }

    // ========== TESTS PÚBLICOS DE LECTURA ==========
    @Test
    void testGetEventImage_Public_Success() throws Exception {
        when(eventImageService.getEventImageById(1L)).thenReturn(eventImageDTO);

        mockMvc.perform(get(apiEndpoint + "/event-images/image/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.eventId").value(1))
                .andExpect(jsonPath("$.imageName").value("test-event-image.jpg"));
    }

    @Test
    void testGetImagesByEvent_Public_Success() throws Exception {
        List<EventImageDTO> images = Arrays.asList(eventImageDTO);
        when(eventImageService.getImagesByEventId(1L)).thenReturn(images);

        mockMvc.perform(get(apiEndpoint + "/event-images/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].eventId").value(1))
                .andExpect(jsonPath("$[0].imageName").value("test-event-image.jpg"));
    }

    @Test
    void testGetImagesByEvent_EmptyList_Success() throws Exception {
        when(eventImageService.getImagesByEventId(1L)).thenReturn(Arrays.asList());

        mockMvc.perform(get(apiEndpoint + "/event-images/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    // ========== TESTS ADMINISTRATIVOS DE CREACIÓN ==========
    @Test
    @WithMockUser(roles = "ADMIN")
    void testUploadEventImage_Success() throws Exception {
        when(eventImageService.saveEventImage(any(EventImageDTO.class))).thenReturn(eventImageDTO);

        mockMvc.perform(post(apiEndpoint + "/event-images")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventImageDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.eventId").value(1))
                .andExpect(jsonPath("$.imageName").value("test-event-image.jpg"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testUploadEventImage_Forbidden() throws Exception {
        mockMvc.perform(post(apiEndpoint + "/event-images")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventImageDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUploadMultipleEventImages_Success() throws Exception {
        List<EventImageDTO> savedImages = Arrays.asList(eventImageDTO);
        when(eventImageService.processAndSaveImages(any(), anyLong())).thenReturn(savedImages);

        MockMultipartFile file1 = new MockMultipartFile("files", "image1.jpg", "image/jpeg", "image content".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("files", "image2.jpg", "image/jpeg", "image content 2".getBytes());

        mockMvc.perform(multipart(apiEndpoint + "/event-images/upload")
                .file(file1)
                .file(file2)
                .param("eventId", "1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testUploadMultipleEventImages_Forbidden() throws Exception {
        MockMultipartFile file = new MockMultipartFile("files", "image.jpg", "image/jpeg", "content".getBytes());

        mockMvc.perform(multipart(apiEndpoint + "/event-images/upload")
                .file(file)
                .param("eventId", "1"))
                .andExpect(status().isForbidden());
    }

    // ========== TESTS ADMINISTRATIVOS DE ELIMINACIÓN ==========
    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteEventImage_Success() throws Exception {
        doNothing().when(eventImageService).deleteEventImage(1L);

        mockMvc.perform(delete(apiEndpoint + "/event-images/image/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testDeleteEventImage_Forbidden() throws Exception {
        mockMvc.perform(delete(apiEndpoint + "/event-images/image/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteMultipleEventImages_Success() throws Exception {
        doNothing().when(eventImageService).deleteMultipleEventImages(any(List.class));

        ImageIdListDTO dto = new ImageIdListDTO();
        dto.setImageIds(Arrays.asList(1L, 2L, 3L));

        mockMvc.perform(delete(apiEndpoint + "/event-images/delete-multiple")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testDeleteMultipleEventImages_Forbidden() throws Exception {
        ImageIdListDTO dto = new ImageIdListDTO();
        dto.setImageIds(Arrays.asList(1L, 2L));

        mockMvc.perform(delete(apiEndpoint + "/event-images/delete-multiple")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteImagesByEventId_Success() throws Exception {
        doNothing().when(eventImageService).deleteImagesByEventId(1L);

        mockMvc.perform(delete(apiEndpoint + "/event-images/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testDeleteImagesByEventId_Forbidden() throws Exception {
        mockMvc.perform(delete(apiEndpoint + "/event-images/1"))
                .andExpect(status().isForbidden());
    }

    // ========== TESTS DE CASOS EDGE ==========
    @Test
    void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(post(apiEndpoint + "/event-images"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUploadMultipleEventImages_ServiceException() throws Exception {
        when(eventImageService.processAndSaveImages(any(), anyLong()))
                .thenThrow(new RuntimeException("Processing error"));

        MockMultipartFile file = new MockMultipartFile("files", "image.jpg", "image/jpeg", "content".getBytes());

        mockMvc.perform(multipart(apiEndpoint + "/event-images/upload")
                .file(file)
                .param("eventId", "1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteMultipleEventImages_ServiceException() throws Exception {
        doThrow(new RuntimeException("Delete error")).when(eventImageService).deleteMultipleEventImages(any(List.class));

        ImageIdListDTO dto = new ImageIdListDTO();
        dto.setImageIds(Arrays.asList(1L));

        mockMvc.perform(delete(apiEndpoint + "/event-images/delete-multiple")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testPublicEndpoints_NoAuthenticationRequired() throws Exception {
        when(eventImageService.getEventImageById(1L)).thenReturn(eventImageDTO);

        // Los endpoints públicos no requieren autenticación
        mockMvc.perform(get(apiEndpoint + "/event-images/image/1"))
                .andExpect(status().isOk());

        List<EventImageDTO> images = Arrays.asList(eventImageDTO);
        when(eventImageService.getImagesByEventId(1L)).thenReturn(images);

        mockMvc.perform(get(apiEndpoint + "/event-images/1"))
                .andExpect(status().isOk());
    }
}
