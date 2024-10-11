package de.stella.agora_web.jwt;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import de.stella.agora_web.security.SecurityUser;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.repository.UserRepository;
import lombok.NonNull;

@Component
public class JWTtoUserConverter implements Converter<Jwt, UsernamePasswordAuthenticationToken> {
    private final UserRepository userRepository;

    public JWTtoUserConverter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UsernamePasswordAuthenticationToken convert(@NonNull Jwt source) {
        Long userId = Long.valueOf(source.getSubject());
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        SecurityUser securityUser = new SecurityUser(user);
        return new UsernamePasswordAuthenticationToken(securityUser, securityUser.getPassword(),
                securityUser.getAuthorities());
    }
}
