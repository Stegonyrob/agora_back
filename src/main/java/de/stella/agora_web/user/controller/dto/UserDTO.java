package de.stella.agora_web.user.controller.dto;

import de.stella.agora_web.user.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String nickname;
    private String relationship;
    private String email;
    private String password;
    private String confirmPassword;

    public UserDTO(String username, String firstName, String lastName, String nickname, String relationship, String email, String password, String confirmPassword) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickname = nickname;
        this.relationship = relationship;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
    public User toUser() {
        User user = new User();
        user.setUsername(this.username);
        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);
        user.setNickname(this.nickname);
        user.setRelationship(this.relationship);
        user.setEmail(this.email);
        user.setPassword(this.password);
        return user;
    }
    public static Object from(User orElseThrow) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'from'");
    }
}
