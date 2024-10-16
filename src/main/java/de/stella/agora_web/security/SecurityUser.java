package de.stella.agora_web.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import de.stella.agora_web.roles.model.Role;
import de.stella.agora_web.user.model.User;

public class SecurityUser implements UserDetails {

  User user;
  public Object getRoles;

  public SecurityUser(User user) {
    this.user = user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

    System.out.println("-------------------");
    System.out.println(user.getRoles());
    System.out.println("-------------------");

    for (Role role : user.getRoles()) {
      System.out.println("User role: " + role.getName());
      SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getName());
      authorities.add(authority);
    }

    return authorities;
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  public Long getId() {
    return user.getId();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public String getRoles() {
    List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
    return String.join(",", roles);
  }
}
