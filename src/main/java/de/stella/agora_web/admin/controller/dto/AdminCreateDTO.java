package de.stella.agora_web.admin.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminCreateDTO {

    // User fields
    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    @NotBlank(message = "La confirmación de contraseña es obligatoria")
    private String confirmPassword;

    @NotBlank(message = "El email es obligatorio")
    private String email;

    @NotBlank(message = "El teléfono es obligatorio para administradores")
    private String phone;

    // Profile fields
    @NotBlank(message = "El primer nombre es obligatorio")
    private String firstName;

    @NotBlank(message = "El primer apellido es obligatorio")
    private String lastName1;

    private String lastName2;
    private String city;
    private String country;
    private String relationship;
    private Long avatarId;
}
