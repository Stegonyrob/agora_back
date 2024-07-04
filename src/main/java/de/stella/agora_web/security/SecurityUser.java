package de.stella.agora_web.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import de.stella.agora_web.roles.model.Role;
import de.stella.agora_web.user.model.User;

public class SecurityUser implements UserDetails {

    User user;

    public SecurityUser(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        System.out.println("User roles: " + user.getRoles());
        System.out.println("User roles size: " + user.getRoles().size());
        for (Role role : user.getRoles()) {
            System.out.println("User role: " + role.getName());
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getName());
            authorities.add(authority);
        }

        System.out.println("Authorities: " + authorities);
        System.out.println("Authorities size: " + authorities.size());

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

    public String getRole() {
        // If the user has no roles, it will throw an IndexOutOfBoundsException, so we
        // catch it here
        try {
            return user.getRoles().iterator().next().getName();
        } catch (NoSuchElementException e) {
            return ""; // Return an empty string if the user has no roles
        }
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
