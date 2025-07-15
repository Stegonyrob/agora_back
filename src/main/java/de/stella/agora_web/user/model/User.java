package de.stella.agora_web.user.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.BatchSize;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.stella.agora_web.banned.model.Banned;
import de.stella.agora_web.comment.model.Comment;
import de.stella.agora_web.profiles.model.Profile;
import de.stella.agora_web.roles.model.Role;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id;

    private String username;

    private String password;

    private String email;

    // Teléfono (solo requerido para admins a nivel de lógica, no de base de datos)
    @Column(name = "phone", length = 32)
    private String phone;

    // Clave secreta TOTP para 2FA (base32, oculta por defecto)
    @Column(name = "totp_secret", length = 64)
    private String totpSecret;

    @Column(nullable = false)
    private boolean acceptedRules;

    @BatchSize(size = 10)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "roles_users", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Profile profile;

    @BatchSize(size = 20)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Banned> banned;

    @BatchSize(size = 50)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Comment> comments;

    // Avatar removed - should be in Profile entity only
    public boolean hasRole(String role) {
        return roles.stream().anyMatch(r -> r.getName().equals(role));
    }

    public User(String userName, String password) {
        this.username = userName;
        this.password = password;
        this.acceptedRules = true; // Default to true when creating a user
    }

    public GrantedAuthority getAuthority() {
        return null;
    }

    public Set<Comment> getComments() {
        return Collections.unmodifiableSet(comments);
    }

    public void setComments(Set<Comment> comments) {
        this.comments = new HashSet<>(comments);
    }

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
    }

    public boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }

    public Object getIdUser() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getIdUser'");
    }
}
