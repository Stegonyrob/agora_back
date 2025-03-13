package de.stella.agora_web.roles.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import de.stella.agora_web.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_role")
  private Long id;

  @NotBlank
  @Size(min = 1, max = 50)
  @Column(name = "name")
  private String name;

  @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
  @JsonBackReference
  private Set<User> users;

  public String getRole() {
    return this.name;
  }

  public Role(long id, String name) {
    this.id = id;
    this.name = name;
    this.users = new HashSet<>();
  }
}
