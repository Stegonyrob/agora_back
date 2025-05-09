package de.stella.agora_web.events.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventDTO {

    private Long id;

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 100, message = "Title must be less than 100 characters")
    private String title;

    @NotBlank(message = "Message cannot be blank")
    @Size(max = 250, message = "Message must be less than 250 characters")
    private String message;

    private boolean archived;
}