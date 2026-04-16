package de.stella.agora_web.avatar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO optimizado para el selector de avatares. Solo incluye la información
 * necesaria para mostrar en el frontend.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvatarSelectorDTO {

    private Long id;
    private String imageName;
    private String displayName;

    /**
     * Para avatares precargados, la URL será construida por el frontend como:
     * /avatars/static/{imageName} Para avatares personalizados, la URL será:
     * /api/avatars/{id}/image
     */
    public String getImageUrl() {
        return "/avatars/static/" + imageName; // Para precargados
    }
}
