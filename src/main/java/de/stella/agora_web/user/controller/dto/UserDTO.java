package de.stella.agora_web.user.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String name;
    private String username;
    private String password;
    private String role;

    public UserDTO() {
    }

    public UserDTO(Long id, String name, String username, String password, String role) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    private String email;

    public String getEmail() {
        return email;
    }

}