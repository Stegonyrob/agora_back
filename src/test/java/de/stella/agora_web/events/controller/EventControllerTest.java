package de.stella.agora_web.events.controller;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.stella.agora_web.events.controller.dto.EventDTO;

@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateEvent_ShouldReturnCreated() throws Exception {
        // Arrange
        EventDTO eventDTO = new EventDTO();
        eventDTO.setTitle("Evento de Prueba");
        eventDTO.setMessage("Este es un evento de prueba.");
        eventDTO.setArchived(false);
        eventDTO.setCapacity(100);
        eventDTO.setEventDate(LocalDate.of(2025, 8, 30));
        eventDTO.setEventTime("19:20");

        String eventJson = objectMapper.writeValueAsString(eventDTO);

        // Act & Assert
        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(eventJson))
                .andExpect(status().isCreated());
    }
}
