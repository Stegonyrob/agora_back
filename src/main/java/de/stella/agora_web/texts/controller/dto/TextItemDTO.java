package de.stella.agora_web.texts.controller.dto;

import de.stella.agora_web.texts.model.TextItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TextItemDTO {

    private Long id;

    @NotBlank(message = "Image URL cannot be blank")
    @Size(max = 255, message = "Image URL must be less than 255 characters")
    private String image;

    @NotBlank(message = "Name to image cannot be blank")
    @Size(max = 255, message = "Name to image must be less than 255 characters")
    private String nameImage;

    @Size(max = 255, message = "Name must be less than 255 characters")
    private String title;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;

    @Size(max = 2000, message = "Content must be less than 2000 characters")
    private String content;

    public String getNameImage;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static TextItemDTO fromEntity(TextItem entity) {
        TextItemDTO dto = new TextItemDTO();
        // Assuming fields: id, title, content, etc.
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        // Add other field mappings as needed
        return dto;
    }
}
