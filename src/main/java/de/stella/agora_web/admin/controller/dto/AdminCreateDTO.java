package de.stella.agora_web.admin.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminCreateDTO {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    @NotBlank(message = "El email es obligatorio")
    private String email;

    @NotBlank(message = "El teléfono es obligatorio para administradores")
    private String phone;
}
