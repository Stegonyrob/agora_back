package de.stella.agora_web.events.controller.dto;

import java.util.List;

import com.google.auto.value.AutoValue.Builder;

import de.stella.agora_web.attendee.controller.dto.AttendeeDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Builder
public class EventDTO {

    private Long id;

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 100, message = "Title must be less than 100 characters")
    private String title;

    @NotBlank(message = "Message cannot be blank")
    @Size(max = 250, message = "Message must be less than 250 characters")
    private String message;

    private boolean archived;
    private List<AttendeeDTO> attendees;

    public EventDTO() {
        // Default constructor
    }

    public EventDTO(Long id, String title, String message, boolean archived, List<AttendeeDTO> attendees) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.archived = archived;
        this.attendees = attendees;
    }
}
