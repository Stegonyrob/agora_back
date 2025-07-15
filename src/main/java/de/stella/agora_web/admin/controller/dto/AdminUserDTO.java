package de.stella.agora_web.admin.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminUserDTO {

    private Long id;
    private String username;
    private String email;
    private boolean admin;
    private boolean active;

    public AdminUserDTO(Long id, String username, String email, boolean admin, boolean active) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.admin = admin;
        this.active = active;
    }

    // Método auxiliar para verificar si es administrador
    public boolean isAdmin() {
        return admin;
    }

    // Método auxiliar para obtener el nombre de usuario
    public String getDisplayName() {
        return username != null ? username : email;
    }
}
