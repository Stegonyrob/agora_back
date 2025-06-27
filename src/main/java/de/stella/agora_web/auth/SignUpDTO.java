package de.stella.agora_web.auth;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpDTO {

    @NotBlank(message = "El username es obligatorio")
    @Size(min = 3, max = 50, message = "El username debe tener entre 3 y 50 caracteres")
    private String username;

    @Email(message = "El email debe tener un formato válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
    private String password;

    @AssertTrue(message = "Debes aceptar las normas del blog para registrarte")
    private boolean rulesAccepted;

    // Campos opcionales para el perfil (se pueden llenar después)
    private String firstName;
    private String lastName1;
    private String lastName2;
    private String city;

    public SignUpDTO() {
    }

    public SignUpDTO(String username, String email, String password, boolean rulesAccepted) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.rulesAccepted = rulesAccepted;
    }

    public String getRelationship() {
        return "defaultRelationship";
    }
}
