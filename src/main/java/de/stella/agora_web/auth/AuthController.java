package de.stella.agora_web.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.jwt.TokenDTO;
import de.stella.agora_web.jwt.TokenGenerator;
import jakarta.validation.Valid;

@RestController
@RequestMapping("${api-endpoint}/all")
public class AuthController {

    @Autowired
    TokenGenerator tokenGenerator;

    @Autowired
    DaoAuthenticationProvider daoAuthenticationProvider;

    @Autowired
    @Qualifier("jwtRefreshTokenAuthProvider")
    JwtAuthenticationProvider refreshTokenAuthProvider;

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        if (loginDTO == null) {
            throw new NullPointerException("LoginDTO cannot be null");
        }

        Authentication authentication;
        try {
            if (loginDTO.getEmail() != null && !loginDTO.getEmail().isBlank()) {
                // Login por email
                authentication = daoAuthenticationProvider.authenticate(
                        UserEmailPasswordAuthenticationToken.unauthenticated(loginDTO.getEmail(), loginDTO.getPassword()));
            } else {
                // Login por username
                authentication = daoAuthenticationProvider.authenticate(
                        UsernamePasswordAuthenticationToken.unauthenticated(loginDTO.getUsername(), loginDTO.getPassword()));
            }
        } catch (org.springframework.security.core.AuthenticationException | IllegalArgumentException e) {
            throw new RuntimeException("Failed to authenticate user", e);
        }

        if (authentication == null) {
            throw new NullPointerException("Authentication cannot be null");
        }

        return ResponseEntity.ok(tokenGenerator.createToken(authentication));
    }

    @PostMapping("/token")
    public ResponseEntity<TokenDTO> token(@RequestBody TokenDTO tokenDTO) {
        Authentication authentication = refreshTokenAuthProvider.authenticate(
                new BearerTokenAuthenticationToken(tokenDTO.getRefreshToken())
        );
        // Jwt jwt = (Jwt) authentication.getCredentials();
        // check if present in db and not revoked, etc

        return ResponseEntity.ok(tokenGenerator.createToken(authentication));
    }
}
