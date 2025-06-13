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

    private final User user;

    public SecurityUser(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : user.getRoles()) {
            // Asegúrate de que getName() retorna String
            String roleName = (role != null && role.getName() != null) ? role.getName().toString() : "";
            if (!roleName.isEmpty()) {
                authorities.add(new SimpleGrantedAuthority(roleName));
            }
        }
        return authorities;
    }

    public String getRoles() {
        if (user == null || user.getRoles() == null) {
            return "";
        }
        List<String> roles = user.getRoles().stream()
                .map(role -> (role != null && role.getName() != null) ? role.getName().toString() : "")
                .filter(name -> !name.isEmpty())
                .collect(Collectors.toList());
        return String.join(",", roles);
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
}
