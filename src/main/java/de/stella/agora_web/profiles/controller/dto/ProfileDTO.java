package de.stella.agora_web.profiles.controller.dto;


import de.stella.agora_web.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDTO {
    
    private User user;
    private String email;
    private String firstName;
    private String firstLastName;
    private String secondLastName;
    private String address;
    private String city;
    private String province;
    private String postalCode;
    private String numberPhone;
    private String password;
    private String username;
    private String name;
    private String role;
}
