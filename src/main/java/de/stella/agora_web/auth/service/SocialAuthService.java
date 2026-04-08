package de.stella.agora_web.auth.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import de.stella.agora_web.user.model.User.SanctionStatus;
import de.stella.agora_web.user.service.IUserService;

@Service
public class SocialAuthService {

    private final IUserService userService;
    private final IProfileService profileService;
    private final TokenGenerator tokenGenerator;
    private final PasswordEncoder passwordEncoder;

    public SocialAuthService(IUserService userService, IProfileService profileService,
            TokenGenerator tokenGenerator, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.profileService = profileService;
        this.tokenGenerator = tokenGenerator;
        this.passwordEncoder = passwordEncoder;
    }

    public TokenDTO authenticateGoogleUser(String idToken) {
        String email = extractEmailFromGoogleToken(idToken);
        String name = extractNameFromGoogleToken(idToken);

        return processUserAuthentication(email, name, "google");
    }

    public TokenDTO authenticateFacebookUser(String accessToken) {
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

        // Lógica de sanción: expulsados no pueden acceder, suspendidos pueden acceder pero no participar
        if (user.getSanctionStatus() == SanctionStatus.EXPELLED) {
            throw new IllegalStateException("Usuario expulsado. Acceso denegado.");
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
            throw new IllegalStateException("Error creando usuario desde login social", e);
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
