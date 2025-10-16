package de.stella.agora_web.events.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.stella.agora_web.events.controller.dto.EventDTO;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.h2.console.enabled=false"
})
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${api-endpoint}")
    private String apiEndpoint;

    @Test
    @WithMockUser(username = "testuser", roles = {"ADMIN"})
    public void testCreateEvent_ShouldRequireAuthentication() throws Exception {
        // Arrange
        EventDTO eventDTO = new EventDTO();
        eventDTO.setTitle("Evento de Prueba");
        eventDTO.setMessage("Este es un evento de prueba.");
        eventDTO.setArchived(false);
        eventDTO.setCapacity(100);
        eventDTO.setEventDate(LocalDate.of(2025, 8, 30));
        eventDTO.setEventTime(java.time.LocalTime.of(19, 20));

        String eventJson = objectMapper.writeValueAsString(eventDTO);

        // Act & Assert - El endpoint requiere autenticación JWT
        // Sin autenticación real, esperamos 401/403
        mockMvc.perform(post(apiEndpoint + "/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(eventJson))
                .andExpect(status().isUnauthorized());
    }
}
