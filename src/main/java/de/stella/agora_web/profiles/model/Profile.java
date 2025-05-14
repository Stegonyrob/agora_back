package de.stella.agora_web.profiles.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.stella.agora_web.avatar.module.Avatar;
import de.stella.agora_web.comment.model.Comment;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.replies.model.Reply;
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
  private boolean favorite;
  private String country;
  private String phone;

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

  public Set<Comment> getFavorites() {
    return this.getFavoriteComments();
  }

  public void setFavorites(Set<Comment> favorites) {
    this.setFavoriteComments(favorites);
  }

  public Set<Comment> getComments() {
    return this.user.getComments();
  }

  public void setComments(Set<Comment> comments) {
    this.user.setComments(comments);
  }

  public Set<Comment> getFavoriteComments() {
    return this.user.getComments();
  }

  public void setFavoriteComments(Set<Comment> favoriteComments) {
    this.user.setComments(favoriteComments);
  }
}
