package de.stella.agora_web.admin.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "admins")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private boolean active;
    private String password;

    public Admin(String username, String email, boolean active, String password) {
        this.username = username;
        this.email = email;
        this.active = active;
        this.password = password;
    }

    // Método auxiliar para verificar si es administrador
    public boolean isAdmin() {
        return true; // Siempre es admin en este contexto
    }
}
