package de.stella.agora_web.profiles.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Builder
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    private String country;
    private String phone;

    public @NonNull Long getUserId() {
        return id;
    }

}
