package de.stella.agora_web.profiles.model;

import de.stella.agora_web.user.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "profiles")
@Getter
@Setter
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToOne
    private User user;


    public Profile() {
    }

    public Profile(String firstName, String lastName1, String lastName2, String username, String relationship,
            String email, String password, String confirmPassword, String city) {
        this.firstName = firstName;
        this.lastName1 = lastName1;
        this.lastName2 = lastName2;
        this.username = username;
        this.relationship = relationship;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.city = city;
    }

    public boolean hasRole(String role) {
        return getUser().hasRole(role);
    }

    public boolean isFavorite() {
        return user.isFavorite();
    }

    public void setFavorite(boolean favorite) {
        user.setFavorite(favorite);
    }

}