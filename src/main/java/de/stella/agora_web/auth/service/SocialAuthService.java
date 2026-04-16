package de.stella.agora_web.auth.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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

    private static final Logger log = LoggerFactory.getLogger(SocialAuthService.class);

    private static final String FACEBOOK_GRAPH_URL
            = "https://graph.facebook.com/me?fields=email,name&access_token=";

    @Value("${social.google.client-id:}")
    private String googleClientId;

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
        Optional<User> existingUser = findUserByEmail(email);

        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            user = createUserFromSocialLogin(email, name, provider);
        }

        if (user.getSanctionStatus() == SanctionStatus.EXPELLED) {
            throw new IllegalStateException("Usuario expulsado. Acceso denegado.");
        }

        SecurityUser securityUser = new SecurityUser(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                securityUser,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        return tokenGenerator.createToken(authentication);
    }

    private Optional<User> findUserByEmail(String email) {
        List<User> allUsers = userService.getAll();
        return allUsers.stream()
                .filter(user -> email.equals(user.getEmail()))
                .findFirst();
    }

    private User createUserFromSocialLogin(String email, String name, String provider) {
        try {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername(email);
            newUser.setPassword(passwordEncoder.encode("social_login_" + provider));

            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            newUser.setRoles(roles);

            User savedUser = userService.save(newUser);

            ProfileDTO profileDTO = ProfileDTO.builder()
                    .userId(savedUser.getId())
                    .email(email)
                    .username(email)
                    .build();

            String[] nameParts = name.split(" ", 2);
            profileDTO.setFirstName(nameParts[0]);
            if (nameParts.length > 1) {
                profileDTO.setLastName1(nameParts[1]);
            }

            profileService.updateProfile(profileDTO, savedUser.getId());

            return savedUser;
        } catch (Exception e) {
            throw new IllegalStateException("Error creando usuario desde login social", e);
        }
    }

    // ── Google ──────────────────────────────────────────────────────────────
    private String extractEmailFromGoogleToken(String idToken) {
        GoogleIdToken.Payload payload = verifyGoogleToken(idToken);
        return payload.getEmail();
    }

    private String extractNameFromGoogleToken(String idToken) {
        GoogleIdToken.Payload payload = verifyGoogleToken(idToken);
        Object name = payload.get("name");
        return name != null ? name.toString() : payload.getEmail();
    }

    private GoogleIdToken.Payload verifyGoogleToken(String idToken) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken token = verifier.verify(idToken);
            if (token == null) {
                throw new IllegalArgumentException("Token de Google inválido o expirado");
            }
            return token.getPayload();
        } catch (GeneralSecurityException | IOException e) {
            log.error("Error verificando token de Google: {}", e.getMessage());
            throw new IllegalArgumentException("Error verificando token de Google", e);
        }
    }

    // ── Facebook ─────────────────────────────────────────────────────────────
    private String extractEmailFromFacebookToken(String accessToken) {
        JsonObject profile = fetchFacebookProfile(accessToken);
        if (!profile.has("email")) {
            throw new IllegalArgumentException("La cuenta de Facebook no tiene email público");
        }
        return profile.get("email").getAsString();
    }

    private String extractNameFromFacebookToken(String accessToken) {
        JsonObject profile = fetchFacebookProfile(accessToken);
        return profile.has("name") ? profile.get("name").getAsString() : "Usuario Facebook";
    }

    private JsonObject fetchFacebookProfile(String accessToken) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(FACEBOOK_GRAPH_URL + accessToken))
                    .GET()
                    .build();

            HttpResponse<String> response;
            try (HttpClient httpClient = HttpClient.newHttpClient()) {
                response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            }

            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();

            if (json.has("error")) {
                String error = json.getAsJsonObject("error").get("message").getAsString();
                throw new IllegalArgumentException("Error de Facebook: " + error);
            }
            return json;
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Error llamando a la Graph API de Facebook: {}", e.getMessage());
            throw new IllegalArgumentException("Error verificando token de Facebook", e);
        }
    }
}
