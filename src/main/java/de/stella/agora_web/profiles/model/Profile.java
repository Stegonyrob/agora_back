package de.stella.agora_web.profiles.model;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.stella.agora_web.avatar.module.Image;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.replys.model.Reply;
import de.stella.agora_web.user.model.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Profile {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_profile")
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
  private boolean favorite;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "replies_favorites", joinColumns = @JoinColumn(name = "user_id"))
  private Set<Reply> replys;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "posts_favorites", joinColumns = @JoinColumn(name = "user_id"))
  private Set<Post> posts;

  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id", referencedColumnName = "id_user")
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private User user;

  public boolean hasRole(String roleName) {
    return getUser().getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase(roleName));
  }

  @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY)
  private Set<Image> images;

  public Optional<Set<Image>> getImages() {
    return Optional.ofNullable(images);
  }

  public void addImage(Image image) {
    if (images == null) {
      images = new HashSet<>();
    }
    images.add(image);
  }
}
