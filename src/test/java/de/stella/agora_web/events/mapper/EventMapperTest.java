package de.stella.agora_web.events.mapper;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.stella.agora_web.events.controller.dto.EventDTO;
import de.stella.agora_web.events.model.Event;

public class EventMapperTest {

    private EventMapper eventMapper;

    @BeforeEach
    public void setUp() {
        eventMapper = new EventMapper();
    }

    @Test
    public void testToEntity_ShouldMapEventTime() {
        // Arrange
        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(1L);
        eventDTO.setTitle("Test Event");
        eventDTO.setMessage("This is a test event.");
        eventDTO.setArchived(false);
        eventDTO.setCapacity(100);
        eventDTO.setEventDate(LocalDate.of(2025, 8, 30));
        eventDTO.setEventTime("19:20");

        // Act
        Event event = eventMapper.toEntity(eventDTO);

        // Assert
        assertNotNull(event);
        assertEquals(eventDTO.getEventTime(), event.getEventTime(), "Event time should be mapped correctly.");
        assertEquals(eventDTO.getEventDate(), event.getEventDate(), "Event date should be mapped correctly.");
    }
}
