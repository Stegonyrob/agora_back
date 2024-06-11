package de.stella.agora_web.user.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.replys.model.Reply;
import de.stella.agora_web.roles.model.Role;
import de.stella.agora_web.user.controller.dto.UserDTO;
import jakarta.persistence.CascadeType;
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
    @Column(name = "username", columnDefinition = "VARCHAR(255)")
    private String username;
    @Column(name = "first_name", columnDefinition = "VARCHAR(255)")
    private String firstName;
    @Column(name = "first_last_name", columnDefinition = "VARCHAR(255)")
    private String firstLastName;
    @Column(name = "second_last_name", columnDefinition = "VARCHAR(255)")
    private String secondLastName;
    @Column(name = "address", columnDefinition = "VARCHAR(255)")
    private String address;
    @Column(name = "city", columnDefinition = "VARCHAR(255)")
    private String city;
    @Column(name = "province", columnDefinition = "VARCHAR(255)")
    private String province;
    @Column(name = "postal_code", columnDefinition = "VARCHAR(255)")
    private String postalCode;
    @Column(name = "number_phone", columnDefinition = "VARCHAR(255)")
    private String numberPhone;
    @Column(name = "relationship", columnDefinition = "VARCHAR(255)")
    private String relationship;
    @Column(name = "email", columnDefinition = "VARCHAR(255)")
    private String email;
    @Column(name = "password", columnDefinition = "VARCHAR(255)")
    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Reply> reply;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "favorites", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "reply_id"))
    @JsonManagedReference
    private Set<Reply> favorites = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Reply> replys = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "roles_users", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonManagedReference
    private Set<Role> roles;

    public User() {
        this.favorites = new HashSet<>();
    }

    public User(Long id, String username, String password, String firstName, String firstLastName, String secondLastName, String nickname, String relationship, String email, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.firstLastName = firstLastName;
        this.secondLastName = secondLastName;
        this.relationship = relationship;
        this.email = email;
        this.roles = roles;
        this.favorites = new HashSet<>();
    }

    public boolean hasRole(String role) {
        return this.roles.stream().anyMatch(r-> r.getName().equals(role));
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
        return UserDTO.builder()
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