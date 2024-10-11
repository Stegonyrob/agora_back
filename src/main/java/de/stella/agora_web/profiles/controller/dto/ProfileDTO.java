package de.stella.agora_web.profiles.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileDTO {

    private Long id;
    private String firstName;
    private String lastName1;
    private String lastName2;
    private String username;
    private String relationship;
    private String email;
    private String password;
    private String confirmPassword;
    private String city;

    public ProfileDTO(Object firstName2, Object lastName12, Object lastName22, Object relationship2, Object email2,
            Object city2) {

    }

    public Long getUserId() {
        return id;
    }

}