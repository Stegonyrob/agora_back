package de.stella.agora_web.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    TokenGenerator tokenGenerator;

    @Autowired
    DaoAuthenticationProvider daoAuthenticationProvider;

    @Autowired
    @Qualifier("jwtRefreshTokenAuthProvider")
    JwtAuthenticationProvider refreshTokenAuthProvider;

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            if (loginDTO == null) {
                logger.warn("Login fallido: loginDTO es null");
                throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Debes enviar username o email válido");
            }
            boolean hasUsername = loginDTO.getUsername() != null && !loginDTO.getUsername().isBlank();
            boolean hasEmail = loginDTO.isValidEmail();
            if (hasUsername == hasEmail) { // Ambos true o ambos false
                logger.warn("Login fallido: Debes enviar solo username o solo email válido, no ambos ni ninguno");
                throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Debes enviar solo username o solo email válido");
            }
            Authentication authentication;
            if (hasEmail) {
                logger.info("Intento de login por email: {}", loginDTO.getUseremail());
                authentication = daoAuthenticationProvider.authenticate(
                        UserEmailPasswordAuthenticationToken.unauthenticated(loginDTO.getUseremail(), loginDTO.getPassword()));
            } else {
                logger.info("Intento de login por username: {}", loginDTO.getUsername());
                authentication = daoAuthenticationProvider.authenticate(
                        UsernamePasswordAuthenticationToken.unauthenticated(loginDTO.getUsername(), loginDTO.getPassword()));
            }
            if (authentication == null) {
                logger.warn("Login fallido: autenticación nula");
                throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Credenciales incorrectas");
            }
            return ResponseEntity.ok(tokenGenerator.createToken(authentication));
        } catch (org.springframework.security.core.AuthenticationException | IllegalArgumentException e) {
            logger.warn("Login fallido: credenciales incorrectas o argumento ilegal: {}", e.getMessage());
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Credenciales incorrectas");
        } catch (org.springframework.web.server.ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error inesperado en login: {}", e.getMessage(), e);
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado en el servidor");
        }
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
