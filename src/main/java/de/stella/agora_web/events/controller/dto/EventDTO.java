package de.stella.agora_web.events.controller.dto;

import java.util.List;

import de.stella.agora_web.attendee.controller.dto.AttendeeDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class EventDTO {

    private Long id;

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 100, message = "Title must be less than 100 characters")
    private String title;

    @NotBlank(message = "Message cannot be blank")
    @Size(max = 300, message = "Message must be less than 300 characters")
    private String message;

    private boolean archived;

    private List<AttendeeDTO> attendees;

    private int capacity;
    private int attendeesCount;

    private List<String> tags;

    public EventDTO() {

    }

    public EventDTO(Long id, String title, String message, boolean archived, List<AttendeeDTO> attendees, int capacity,
            int attendeesCount) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.archived = archived;
        this.attendees = attendees;
        this.capacity = capacity;
        this.attendeesCount = attendeesCount;
        this.tags = tags;
    }
}
