package de.stella.agora_web.auth.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import de.stella.agora_web.jwt.TokenDTO;
import de.stella.agora_web.jwt.TokenGenerator;
import de.stella.agora_web.profiles.controller.dto.ProfileDTO;
import de.stella.agora_web.profiles.service.IProfileService;
import de.stella.agora_web.roles.model.Role;
import de.stella.agora_web.security.SecurityUser;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.service.IUserService;

@Service
public class SocialAuthService {

    @Autowired
    private IUserService userService;

    @Autowired
    private IProfileService profileService;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public TokenDTO authenticateGoogleUser(String idToken) {
        // TODO: Implementar validación del token de Google
        // Por ahora, simularemos la extracción de datos del token

        // Aquí debería validar el token con Google y extraer la información del usuario
        // GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
        //     .setAudience(Arrays.asList(clientId))
        //     .build();
        // Por ahora, simulamos datos extraídos del token
        String email = extractEmailFromGoogleToken(idToken);
        String name = extractNameFromGoogleToken(idToken);

        return processUserAuthentication(email, name, "google");
    }

    public TokenDTO authenticateFacebookUser(String accessToken) {
        // TODO: Implementar validación del token de Facebook
        // Por ahora, simularemos la extracción de datos del token

        // Aquí debería validar el token con Facebook Graph API
        // Facebook facebook = new FacebookFactory().getInstance();
        // facebook.setOAuthAccessToken(new AccessToken(accessToken));
        // Por ahora, simulamos datos extraídos del token
        String email = extractEmailFromFacebookToken(accessToken);
        String name = extractNameFromFacebookToken(accessToken);

        return processUserAuthentication(email, name, "facebook");
    }

    private TokenDTO processUserAuthentication(String email, String name, String provider) {
        // Buscar usuario existente por email
        Optional<User> existingUser = findUserByEmail(email);

        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            // Crear nuevo usuario
            user = createUserFromSocialLogin(email, name, provider);
        }

        // Crear autenticación
        SecurityUser securityUser = new SecurityUser(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                securityUser,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        // Generar token
        return tokenGenerator.createToken(authentication);
    }

    private Optional<User> findUserByEmail(String email) {
        // Buscar entre todos los usuarios
        List<User> allUsers = userService.getAll();
        return allUsers.stream()
                .filter(user -> email.equals(user.getEmail()))
                .findFirst();
    }

    private User createUserFromSocialLogin(String email, String name, String provider) {
        try {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername(email); // Usar email como username por defecto
            newUser.setPassword(passwordEncoder.encode("social_login_" + provider));

            // Crear rol USER
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            newUser.setRoles(roles);

            User savedUser = userService.save(newUser);

            // Crear perfil asociado usando ProfileDTO
            ProfileDTO profileDTO = ProfileDTO.builder()
                    .userId(savedUser.getId())
                    .email(email)
                    .username(email)
                    .build();

            // Separar nombre en firstName y lastName si es posible
            String[] nameParts = name.split(" ", 2);
            profileDTO.setFirstName(nameParts[0]);
            if (nameParts.length > 1) {
                profileDTO.setLastName1(nameParts[1]);
            }

            // Crear perfil usando el método updateProfile (crear uno nuevo con userId)
            profileService.updateProfile(profileDTO, savedUser.getId());

            return savedUser;
        } catch (Exception e) {
            throw new RuntimeException("Error creando usuario desde login social", e);
        }
    }

    // Métodos temporales para simular extracción de datos
    // TODO: Reemplazar con implementación real
    @SuppressWarnings("unused")
    private String extractEmailFromGoogleToken(String idToken) {
        // Simulación - en producción debería validar y extraer del token real
        return "user@gmail.com";
    }

    @SuppressWarnings("unused")
    private String extractNameFromGoogleToken(String idToken) {
        // Simulación - en producción debería validar y extraer del token real
        return "Usuario Google";
    }

    @SuppressWarnings("unused")
    private String extractEmailFromFacebookToken(String accessToken) {
        // Simulación - en producción debería validar y extraer del token real
        return "user@facebook.com";
    }

    @SuppressWarnings("unused")
    private String extractNameFromFacebookToken(String accessToken) {
        // Simulación - en producción debería validar y extraer del token real
        return "Usuario Facebook";
    }
}
