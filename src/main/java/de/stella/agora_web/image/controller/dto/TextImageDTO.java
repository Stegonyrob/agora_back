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
public class TextImageDTO {

    private Long id;
    private String imageName;
    private String imagePath;
    private Long textId;

    public TextImageDTO(Long id, String imageName, Long textId) {
        this.id = id;
        this.imageName = imageName;
        this.textId = textId;
    }

    public TextImageDTO(Long id, String imageName, String imagePath) {
        this.id = id;
        this.imageName = imageName;
        this.imagePath = imagePath;
    }
}
