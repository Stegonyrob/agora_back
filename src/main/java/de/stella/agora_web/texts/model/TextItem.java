package de.stella.agora_web.texts.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "texts")
public class TextItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Image URL cannot be blank")
    @Size(max = 255, message = "Image URL must be less than 255 characters")
    private String image;

    @NotBlank(message = "Name to image cannot be blank")
    @Size(max = 255, message = "Name to image must be less than 100 characters")
    private String nameImage;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 500000, message = "Description must be less than 1000 characters")
    private String description;

    public TextItem(String description, Long id, String image, String nameImage) {
        this.description = description;
        this.id = id;
        this.image = image;
        this.nameImage = nameImage;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNameImage(String nameImage) {
        if (nameImage == null || nameImage.isBlank()) {
            throw new IllegalArgumentException("Name image cannot be null or blank");
        }
        if (nameImage.length() > 255) {
            throw new IllegalArgumentException("Name image must be less than 256 characters");
        }
        this.nameImage = nameImage;
    }
}