package de.stella.agora_web.auth;

import jakarta.validation.constraints.Size;

public class LoginDTO {

    @Size(min = 3, max = 50, message = "El username debe tener entre 3 y 50 caracteres")
    private String username;

    @Size(max = 50, message = "El email debe tener máximo 50 caracteres")
    private String useremail;

    @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
    private String password;

    private String role;

    public LoginDTO(String username, String useremail, String password) {
        this.username = username;
        this.useremail = useremail;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isValidLogin() {
        boolean hasUsername = username != null && !username.isBlank();
        boolean hasEmail = isValidEmail();
        return hasUsername || hasEmail;
    }

    public boolean isValidEmail() {
        return useremail != null && !useremail.isBlank() && useremail.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    }
}
