package de.stella.agora_web.texts.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "texts")
public class TextItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @NotBlank(message = "Image URL cannot be blank")
    @Column(nullable = false, length = 255)
    private String image;

    @NotBlank(message = "Name to image cannot be blank")
    @Column(name = "name_image", nullable = false, length = 255)
    private String nameImage;

    @NotBlank(message = "Description cannot be blank")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    public String getContent() {
        return description;
    }

    public void setContent(String content) {
        this.description = content;
    }
}
