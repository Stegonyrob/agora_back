package de.stella.agora_web.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.stella.agora_web.banned.model.Banned;
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
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

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

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
    name = "roles_users",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  private Set<Role> roles;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
  private Profile profile;

  @OneToMany(mappedBy = "user")
  private List<Banned> banned;

  public boolean hasRole(String role) {
    return roles.stream().anyMatch(r -> r.getName().equals(role));
  }

  public User(Long id, String username, String password) {
    this.id = id;
    this.username = username;
    this.password = password;
  }

  public User(String userName, String password) {
    this.username = userName;
    this.password = password;
  }

  public GrantedAuthority getAuthority() {
    return null;
  }
}
