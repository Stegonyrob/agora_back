package de.stella.agora_web.user.register;

import de.stella.agora_web.user.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDto {
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
    public String getUsername() {
        return username;
    }
    public Object getUserName() {
        return username;
    }
      
   
}