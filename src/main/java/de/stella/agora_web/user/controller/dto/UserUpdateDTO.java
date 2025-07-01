package de.stella.agora_web.user.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class UserUpdateDTO {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    @Email(message = "El email debe tener un formato válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    // Información del perfil
    private String firstName;
    private String lastName1;
    private String lastName2;

    // Roles (solo nombres)
    private String[] roles;

    // Password opcional (solo si se quiere cambiar)
    private String password;

    // Estado de reglas aceptadas
    private boolean acceptedRules;
}
