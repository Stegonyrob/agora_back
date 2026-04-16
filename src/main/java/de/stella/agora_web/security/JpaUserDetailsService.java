package de.stella.agora_web.security;

import java.util.Objects;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.stella.agora_web.user.repository.UserRepository;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    UserRepository repository;

    public JpaUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Objects.requireNonNull(usernameOrEmail, "username or email cannot be null");
        if (usernameOrEmail.contains("@")) {
            return repository.findByEmailWithRoles(usernameOrEmail)
                    .map(SecurityUser::new)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found for email: " + usernameOrEmail));
        } else {
            return repository.findByUsernameWithRoles(usernameOrEmail)
                    .map(SecurityUser::new)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found for username: " + usernameOrEmail));
        }
    }
}
