package de.stella.agora_web.profiles.model;

import java.util.Set;
import java.util.stream.Collectors;

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

    @NotBlank
    @Size(min = 8, max = 100)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank
    @Size(min = 8, max = 100)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String confirmPassword;

    private String city;
    private String country;
    private String phone;

    // Relación con ReplyLove (si tienes una tabla auxiliar para replies favoritos, usa una entidad intermedia)
    // Si no, elimina o ajusta esta relación según tu modelo real.
    // Ejemplo seguro:
    // @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY)
    // private Set<ReplyLove> replyLoves;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id_user")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;

    // Relación correcta con PostLove
    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostLove> postLoves;

    @OneToOne
    @JoinColumn(name = "avatar_id")
    private Avatar avatar;

    public Profile() {
    }

    public Profile(Long id, String firstName, String lastName1, String lastName2, String username, String relationship,
            String email, String password, String confirmPassword, String city, String country, String phone) {
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
    }

    public boolean hasRole(String role) {
        return this.user.getRoles().stream().anyMatch(r -> r.getName().equals(role));
    }

    // Métodos para obtener posts favoritos a través de la tabla auxiliar
    public Set<Post> getFavoritePosts() {
        return postLoves != null
                ? postLoves.stream().map(PostLove::getPost).collect(Collectors.toSet())
                : Set.of();
    }

    public int getLoveCount() {
        return postLoves != null ? postLoves.size() : 0;
    }
}
