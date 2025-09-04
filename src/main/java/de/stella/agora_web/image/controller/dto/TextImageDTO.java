package de.stella.agora_web.image.controller.dto;

import lombok.Data;

@Data
public class TextImageDTO {

    private Long id;
    private Long textId;
    private String imageName;
    private String imageUrl;
    private String description;
}
