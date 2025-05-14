package de.stella.agora_web.image.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventImageDTO {
    private Long id;
    private String imageName;
    private byte[] imageData;
    private Long eventId;
}