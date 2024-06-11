package de.stella.agora_web.user.model;

import java.util.HashSet;
import java.util.Set;

import de.stella.agora_web.profiles.model.Profile;
import de.stella.agora_web.replys.model.Reply;
import de.stella.agora_web.roles.model.Role;
import de.stella.agora_web.user.controller.dto.UserDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @ManyToMany
    @JoinTable(name = "user_role",
               joinColumns = @JoinColumn(name = "user_id"),
               inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @ManyToMany
    @JoinTable(name = "user_reply",
               joinColumns = @JoinColumn(name = "user_id"),
               inverseJoinColumns = @JoinColumn(name = "reply_id"))
    private Set<Reply> favorites = new HashSet<>();

    private UserDTO toDTO() {
        return UserDTO.builder()
                      .id(this.getId())
                      .username(this.getUsername())
                      .email(this.getEmail())
                      .roles(this.getRoles())
                      .build();
    }

    public boolean hasRole(String roleName) {
        return this.roles.stream().anyMatch(role -> role.getName().equals(roleName));
    }

    public void addFavorite(Reply reply) {
        this.favorites.add(reply);
    }

    public void removeFavorite(Reply reply) {
        this.favorites.remove(reply);
    }

    public void setFirstName(String firstName) {
        this.profile.setFirstName(firstName);
    }

    public void setFirstLastName(String firstLastName) {
        this.profile.setFirstLastName(firstLastName);
    }

    public void setSecondLastName(String secondLastName) {
        this.profile.setSecondLastName(secondLastName);
    }

    public void setRelationship(String relationship) {
        this.profile.setRelationship(relationship);
    }

    public String getFirstName() {
        return this.profile.getFirstName();
    }

    public String getFirstLastName() {
        return this.profile.getFirstLastName();
    }

    public String getSecondLastName() {
        return this.profile.getSecondLastName();
    }

    public String getRelationship() {
        return this.profile.getRelationship();
    }
}