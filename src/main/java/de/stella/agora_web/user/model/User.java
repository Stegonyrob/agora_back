package de.stella.agora_web.user.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.replys.model.Reply;
import de.stella.agora_web.roles.model.Role;
import de.stella.agora_web.user.controller.dto.UserDTO;
import de.stella.agora_web.user.controller.dto.UserDTO.UserDTOBuilder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id;

    private String username;
    private String firstName;
    private String nickname;
    private String relationship;
    private String email;
    private String password;
    private String firstLastName;
    private String secondLastName;
    private String address;
    private String city;
    private String province;
    private String postalCode;
    private String numberPhone;
    private Set<Reply> favorites;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Post> posts;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "roles_users", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonManagedReference
    private Set<Role> roles;

    public User() {
        this.favorites = new HashSet<>();
    }

    public User(Long id, String username, String password, String firstName, String lastName, String nickname, String relationship, String email, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.firstLastName = lastName;
        this.secondLastName = lastName;
        this.nickname = nickname;
        this.relationship = relationship;
        this.email = email;
        this.roles = roles;
        this.favorites = new HashSet<>();
    }

    public boolean hasRole(String role) {
        return this.roles.stream().anyMatch(r -> r.getName().equals(role));
    }

    public Set<Reply> getFavorites() {
        return this.favorites;
    }

    public void addFavorite(Reply reply) {
        this.favorites.add(reply);
    }

    public void removeFavorite(Reply reply) {
        this.favorites.remove(reply);
    }

    public UserDTO toBuilder() {
        return ((UserDTOBuilder) UserDTO.builder())
                .id(this.getId())
                .username(this.getUsername())
                .email(this.getEmail())
                .firstName(this.getFirstName())
             
                .firstLastName(this.getFirstLastName())
                .secondLastName(this.getSecondLastName())
                .address(this.getAddress())
                .city(this.getCity())
                .province(this.getProvince())
                .postalCode(this.getPostalCode())
                .numberPhone(this.getNumberPhone())
                .roles(this.getRoles())
                .build();
    }
}
