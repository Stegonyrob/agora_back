package de.stella.agora_web.user.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserListDTO {

    private Long id;
    private String username;
    private String email;
    private boolean acceptedRules;

    // Información del perfil simplificada
    private Long profileId; // ✅ AGREGAR ESTO
    private String firstName;
    private String lastName1;
    private String lastName2;

    // Avatar simplificado
    private Long avatarId;
    private String avatarUrl;
    private String avatarDisplayName;

    // Roles simplificados (solo nombres)
    private String[] roles;

    // Estado de ban simplificado
    private boolean isBanned;
    private String banReason;

    // Método auxiliar para obtener el nombre completo
    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        if (firstName != null && !firstName.trim().isEmpty()) {
            fullName.append(firstName);
        }
        if (lastName1 != null && !lastName1.trim().isEmpty()) {
            if (fullName.length() > 0) {
                fullName.append(" ");
            }
            fullName.append(lastName1);
        }
        if (lastName2 != null && !lastName2.trim().isEmpty()) {
            if (fullName.length() > 0) {
                fullName.append(" ");
            }
            fullName.append(lastName2);
        }
        return fullName.toString().isEmpty() ? username : fullName.toString();
    }

    // Método auxiliar para verificar si es administrador
    public boolean isAdmin() {
        if (roles == null) {
            return false;
        }
        for (String role : roles) {
            if ("ROLE_ADMIN".equals(role)) {
                return true;
            }
        }
        return false;
    }
}
