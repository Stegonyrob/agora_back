package de.stella.agora_web.admin.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 👤 DTO para Perfil de Administrador
 *
 * Encapsula los datos del perfil de un usuario administrativo.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminProfileDTO {

    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String fullName;
    private Long avatarId;
    private String avatarUrl;

    /**
     * Constructor para crear perfil con datos básicos
     */
    public AdminProfileDTO(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
    }

    /**
     * Genera el nombre completo automáticamente
     */
    public String getFullName() {
        if (fullName == null) {
            fullName = (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
        }
        return fullName.trim();
    }
}
