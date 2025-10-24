package de.stella.agora_web.events.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.stella.agora_web.events.controller.dto.EventDTO;
import de.stella.agora_web.events.controller.dto.EventResponseDTO;
import de.stella.agora_web.events.controller.dto.PublicEventDTO;
import de.stella.agora_web.events.model.Event;
import de.stella.agora_web.events.service.IEventService;
import de.stella.agora_web.events.service.IPublicEventService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
public class EventControllerMainTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IEventService eventService;

    @MockBean
    private IPublicEventService publicEventService;

    private PublicEventDTO publicEventDTO;
    private EventResponseDTO eventResponseDTO;
    private EventDTO eventDTO;
    private Event event;

    @BeforeEach
    void setUp() {
        publicEventDTO = new PublicEventDTO();
        publicEventDTO.setId(1L);
        publicEventDTO.setTitle("Test Public Event");
        publicEventDTO.setMessage("Test event message");

        eventResponseDTO = new EventResponseDTO();
        eventResponseDTO.setId(1L);
        eventResponseDTO.setTitle("Test Event Response");
        eventResponseDTO.setMessage("Test event response message");

        eventDTO = new EventDTO();
        eventDTO.setTitle("Test Event");
        eventDTO.setMessage("Test event message");
        eventDTO.setEventDate(LocalDate.now().plusDays(7));
        eventDTO.setEventTime(LocalTime.of(14, 30));
        eventDTO.setCapacity(100);
        eventDTO.setArchived(false);

        event = new Event();
        event.setId(1L);
        event.setTitle("Test Event");
        event.setMessage("Test event message");
        event.setEventDate(LocalDate.now().plusDays(7));
        event.setEventTime(LocalTime.of(14, 30).toString());
        event.setCapacity(100);
        event.setArchived(false);
    }

    // ========== TESTS PARA OBTENER TODOS LOS EVENTOS ==========
    @Test
    @WithMockUser(roles = "USER")
    void testGetAllEventsWithImages_User_Success() throws Exception {
        List<PublicEventDTO> events = Arrays.asList(publicEventDTO);
        when(publicEventService.getAllPublicEventsWithImages()).thenReturn(events);

        mockMvc.perform(get("/api/v1/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Public Event"))
                .andExpect(jsonPath("$[0].message").value("Test event message"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllEventsWithImages_Admin_Success() throws Exception {
        List<PublicEventDTO> events = Arrays.asList(publicEventDTO);
        when(publicEventService.getAllPublicEventsWithImages()).thenReturn(events);

        mockMvc.perform(get("/api/v1/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void testGetAllEventsWithImages_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/events"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetAllEventsWithImages_EmptyList_Success() throws Exception {
        when(publicEventService.getAllPublicEventsWithImages()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/v1/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetAllEventsWithImages_ServiceException() throws Exception {
        when(publicEventService.getAllPublicEventsWithImages())
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/api/v1/events"))
                .andExpect(status().isInternalServerError());
    }

    // ========== TESTS PARA EVENTOS PAGINADOS ==========
    @Test
    @WithMockUser(roles = "USER")
    void testGetPaginatedEventsWithImages_Success() throws Exception {
        List<PublicEventDTO> events = Arrays.asList(publicEventDTO);
        Page<PublicEventDTO> eventsPage = new PageImpl<>(events);
        when(publicEventService.getAllPublicEventsWithImagesPaginated(any(Pageable.class))).thenReturn(eventsPage);

        mockMvc.perform(get("/api/v1/events/paginated")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Test Public Event"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetPaginatedEventsWithImages_Admin_Success() throws Exception {
        List<PublicEventDTO> events = Arrays.asList(publicEventDTO);
        Page<PublicEventDTO> eventsPage = new PageImpl<>(events);
        when(publicEventService.getAllPublicEventsWithImagesPaginated(any(Pageable.class))).thenReturn(eventsPage);

        mockMvc.perform(get("/api/v1/events/paginated")
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void testGetPaginatedEventsWithImages_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/events/paginated"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetPaginatedEventsWithImages_ServiceException() throws Exception {
        when(publicEventService.getAllPublicEventsWithImagesPaginated(any(Pageable.class)))
                .thenThrow(new RuntimeException("Pagination error"));

        mockMvc.perform(get("/api/v1/events/paginated"))
                .andExpect(status().isInternalServerError());
    }

    // ========== TESTS PARA OBTENER EVENTO POR ID ==========
    @Test
    @WithMockUser(roles = "USER")
    void testGetEventById_Success() throws Exception {
        when(eventService.getEventResponseById(1L)).thenReturn(eventResponseDTO);

        mockMvc.perform(get("/api/v1/events/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Event Response"))
                .andExpect(jsonPath("$.message").value("Test event response message"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetEventById_Admin_Success() throws Exception {
        when(eventService.getEventResponseById(1L)).thenReturn(eventResponseDTO);

        mockMvc.perform(get("/api/v1/events/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetEventById_NotFound() throws Exception {
        when(eventService.getEventResponseById(1L)).thenThrow(new RuntimeException("Event not found"));

        mockMvc.perform(get("/api/v1/events/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetEventById_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/events/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetEventById_ServiceException() throws Exception {
        when(eventService.getEventResponseById(1L)).thenThrow(new Exception("Internal error"));

        mockMvc.perform(get("/api/v1/events/1"))
                .andExpect(status().isInternalServerError());
    }

    // ========== TESTS PARA CREAR EVENTO ==========
    @Test
    @WithMockUser(roles = "USER")
    void testCreateEvent_Success() throws Exception {
        when(eventService.save(any(Event.class))).thenReturn(event);

        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Event"))
                .andExpect(jsonPath("$.message").value("Test event message"))
                .andExpect(jsonPath("$.capacity").value(100))
                .andExpect(jsonPath("$.archived").value(false));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateEvent_Admin_Success() throws Exception {
        when(eventService.save(any(Event.class))).thenReturn(event);

        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testCreateEvent_NullEventDTO_BadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateEvent_Unauthorized() throws Exception {
        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testCreateEvent_ServiceException() throws Exception {
        when(eventService.save(any(Event.class))).thenThrow(new Exception("Save error"));

        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDTO)))
                .andExpect(status().isInternalServerError());
    }

    // ========== TESTS PARA ACTUALIZAR EVENTO ==========
    @Test
    @WithMockUser(roles = "USER")
    void testUpdateEvent_Success() throws Exception {
        EventDTO updatedEventDTO = new EventDTO();
        updatedEventDTO.setTitle("Updated Event");
        updatedEventDTO.setMessage("Updated message");

        when(eventService.updateEvent(anyLong(), any(EventDTO.class))).thenReturn(updatedEventDTO);

        mockMvc.perform(put("/api/v1/events/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Event"))
                .andExpect(jsonPath("$.message").value("Updated message"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateEvent_Admin_Success() throws Exception {
        when(eventService.updateEvent(anyLong(), any(EventDTO.class))).thenReturn(eventDTO);

        mockMvc.perform(put("/api/v1/events/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testUpdateEvent_NullEventDTO_BadRequest() throws Exception {
        mockMvc.perform(put("/api/v1/events/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testUpdateEvent_NotFound() throws Exception {
        when(eventService.updateEvent(anyLong(), any(EventDTO.class)))
                .thenThrow(new RuntimeException("Event not found"));

        mockMvc.perform(put("/api/v1/events/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateEvent_Unauthorized() throws Exception {
        mockMvc.perform(put("/api/v1/events/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testUpdateEvent_ServiceException() throws Exception {
        when(eventService.updateEvent(anyLong(), any(EventDTO.class)))
                .thenThrow(new RuntimeException("Update error"));

        mockMvc.perform(put("/api/v1/events/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDTO)))
                .andExpect(status().isInternalServerError());
    }

    // ========== TESTS DE CASOS EDGE ==========
    @Test
    @WithMockUser(roles = "USER")
    void testCreateEvent_WithCompleteData_Success() throws Exception {
        EventDTO completeEventDTO = new EventDTO();
        completeEventDTO.setTitle("Complete Event");
        completeEventDTO.setMessage("Complete event message with all fields");
        completeEventDTO.setEventDate(LocalDate.now().plusMonths(1));
        completeEventDTO.setEventTime(LocalTime.of(10, 0));
        completeEventDTO.setCapacity(500);
        completeEventDTO.setArchived(false);

        Event completeEvent = new Event();
        completeEvent.setId(2L);
        completeEvent.setTitle("Complete Event");
        completeEvent.setMessage("Complete event message with all fields");

        when(eventService.save(any(Event.class))).thenReturn(completeEvent);

        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(completeEventDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.title").value("Complete Event"));
    }
}
