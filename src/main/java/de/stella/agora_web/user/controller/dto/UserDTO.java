package de.stella.agora_web.user.controller.dto;

import java.util.Set;

import de.stella.agora_web.roles.model.Role;
import de.stella.agora_web.user.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String username;
    private String firstName;
    private String relationship;
    private String email;
    private String password;
    private String confirmPassword;
    private String firstLastName;
    private String secondLastName;
    private Set<Role> roles;

    public static UserDTO from(User user) {
        return new UserDTO(user.getUsername(), user.getFirstName(), user.getRelationship(), user.getEmail(), user.getPassword(), null);
    }

    public User toUser() {
        User user = new User();
        user.setUsername(this.username);
        user.setFirstName(this.firstName);
        user.setFirstLastName(this.firstLastName);
        user.setSecondLastName(this.secondLastName);
        user.setRelationship(this.relationship);
        user.setEmail(this.email);
        user.setPassword(this.password);
        return user;
    }

    public UserDTO(String username, String firstName, String relationship, String email, String password, String confirmPassword) {
        this.username = username;
        this.firstName = firstName;
        this.relationship = relationship;
        this.email = email;
        this.password = password;
    }

    public static class UserDTOBuilder {
        private String username;
        private String firstName;
        private String relationship;
        private String email;
        private String password;
        private String confirmPassword;
        private String firstLastName;
        private String secondLastName;
        private Long id;
        private Set<Role> roles;

        public UserDTOBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserDTOBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserDTOBuilder relationship(String relationship) {
            this.relationship = relationship;
            return this;
        }

        public UserDTOBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserDTOBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserDTOBuilder confirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
            return this;
        }

        public UserDTOBuilder firstLastName(String firstLastName) {
            this.firstLastName = firstLastName;
            return this;
        }

        public UserDTOBuilder secondLastName(String secondLastName) {
            this.secondLastName = secondLastName;
            return this;
        }

        public UserDTO build() {
            return new UserDTO(username, firstName, relationship, email, password, confirmPassword);
        }

        public UserDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserDTOBuilder roles(Set<Role> roles) {
            this.roles = roles;
            return this;
        }
    }

    public static UserDTOBuilder builder() {
        return new UserDTOBuilder();
    }
}
