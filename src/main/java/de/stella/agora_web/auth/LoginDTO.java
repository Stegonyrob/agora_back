package de.stella.agora_web.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginDTO {

  public LoginDTO(@NotBlank @Size(min = 3, max = 50) String username,
      @NotBlank @Size(min = 8, max = 100) String password) {
    this.username = username;
    this.password = password;
  }

  @NotBlank
  @Size(min = 3, max = 50)
  private String username;

  @NotBlank
  @Size(min = 8, max = 100)
  private String password;

  private String role;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }
}
