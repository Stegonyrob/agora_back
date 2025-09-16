package de.stella.agora_web.texts.controller.dto;

import java.time.LocalDateTime;
import java.util.List;

import de.stella.agora_web.image.controller.dto.TextImageDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TextItemDTO {

    private Long id;

    @NotBlank(message = "Category cannot be blank")
    @Size(max = 64, message = "Category must be less than 64 characters")
    private String category;

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    @NotBlank(message = "Message cannot be blank")
    @Size(max = 2000, message = "Message must be less than 2000 characters")
    private String message;

    /**
     * Lista de imágenes asociadas al texto (OPCIONAL - se obtiene por separado para eficiencia).
     * Patrón consistente con Posts y Events: imágenes separadas del JSON principal.
     */
    private List<TextImageDTO> images;

    // Fecha de creación solo informativa, la asigna el backend
    private LocalDateTime createdAt;
}
