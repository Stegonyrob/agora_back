package de.stella.agora_web.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpDTO {

    private String username;

    private String password;

    public SignUpDTO() {

    }

    public SignUpDTO(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return getFirstName();
    }

    public String getLastName1() {
        return getLastName1();
    }

    public String getLastName2() {
        return getLastName2();
    }

    public String getRelationship() {
        return getRelationship();
    }

    public String getEmail() {
        return getEmail();
    }

    public String getCity() {
        return getCity();
    }

}
