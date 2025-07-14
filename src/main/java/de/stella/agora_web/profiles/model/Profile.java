package de.stella.agora_web.profiles.model;

import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.annotations.BatchSize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.stella.agora_web.avatar.module.Avatar;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.posts.model.PostLove;
import de.stella.agora_web.user.model.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "profiles")
@Getter
@Setter
public class Profile {

    private static final Logger log = LoggerFactory.getLogger(Profile.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_profile")
    private Long id;

    @NotBlank
    @Size(min = 1, max = 50)
    private String firstName;

    @NotBlank
    @Size(min = 1, max = 50)
    private String lastName1;

    @Size(max = 50)
    private String lastName2;

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    private String relationship;

    @NotBlank
    @Email
    private String email;

    // Campos para cambio de contraseña (solo se usan en el frontend, no se persisten)
    @Size(max = 100)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Size(max = 100)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String confirmPassword;

    private String city;
    private String country;
    private String phone;

    // Relación con User, se usa para roles y autenticación
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id_user", unique = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;

    // Relación correcta con PostLove
    @BatchSize(size = 20)
    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostLove> postLoves;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avatar_id")
    private Avatar avatar;

    public Profile() {
        log.debug("Profile() constructor called");
    }

    public Profile(Long id, String firstName, String lastName1, String lastName2, String username, String relationship,
            String email, String password, String confirmPassword, String city, String country, String phone, Avatar avatar) {
        this.id = id;
        this.firstName = firstName;
        this.lastName1 = lastName1;
        this.lastName2 = lastName2;
        this.username = username;
        this.relationship = relationship;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.city = city;
        this.country = country;
        this.phone = phone;
        this.avatar = avatar; // Inicializar avatar recibido
        log.debug("Profile(Long, ...) constructor called with id={}, username={}, avatar={}", id, username, avatar != null ? avatar.getImageName() : "null");
    }

    public boolean hasRole(String role) {
        boolean result = this.user != null && this.user.getRoles().stream().anyMatch(r -> r.getName().equals(role));
        log.debug("hasRole('{}') called for profile id={}, result={}", role, this.id, result);
        return result;
    }

    // Métodos para obtener posts favoritos a través de la tabla auxiliar
    public Set<Post> getFavoritePosts() {
        Set<Post> favorites = postLoves != null ? postLoves.stream().map(PostLove::getPost).collect(Collectors.toSet()) : Set.of();
        log.debug("getFavoritePosts() called for profile id={}, count={}", this.id, favorites.size());
        return favorites;
    }

    public int getLoveCount() {
        int count = postLoves != null ? postLoves.size() : 0;
        log.debug("getLoveCount() called for profile id={}, count={}", this.id, count);
        return count;
    }

    public void setAvatar(Avatar avatar) {
        log.debug("setAvatar() called for profile id={}, avatar={}", this.id, avatar != null ? avatar.getImageName() : "null");
        this.avatar = avatar;
    }
}
