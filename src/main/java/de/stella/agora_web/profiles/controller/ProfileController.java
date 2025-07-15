package de.stella.agora_web.profiles.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.stella.agora_web.gdpr.service.GdprService;
import de.stella.agora_web.profiles.controller.dto.ProfileDTO;
import de.stella.agora_web.profiles.model.Profile;
import de.stella.agora_web.profiles.service.impl.ProfileServiceImpl;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.service.impl.UserServiceImpl;

@RestController
@RequestMapping(path = "${api-endpoint}/any/user/profile")
public class ProfileController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    ProfileServiceImpl service;
    UserServiceImpl userService;
    GdprService gdprService;

    public ProfileController(ProfileServiceImpl service, UserServiceImpl userService, GdprService gdprService) {
        this.service = service;
        this.userService = userService;
        this.gdprService = gdprService;
    }

    @PostMapping(path = "/{id}")
    public ResponseEntity<Profile> getProfileById(@RequestBody ProfileDTO profileDTO) throws Exception {
        Profile profile = service.getById(profileDTO.getUserId());
        return ResponseEntity.ok(profile);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ProfileDTO> getById(@NonNull @PathVariable Long id) throws Exception {
        Profile profile = service.getById(id);
        ProfileDTO profileDTO = toProfileDTO(profile);
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(profileDTO);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping(path = "/{id}")
    public ResponseEntity<ProfileDTO> update(@PathVariable Long id, @RequestBody ProfileDTO profileDTO) throws Exception {
        try {
            // Log the complete JSON received
            String jsonReceived = objectMapper.writeValueAsString(profileDTO);
            logger.info("=== PROFILE UPDATE REQUEST ===");
            logger.info("Profile ID: {}", id);
            logger.info("JSON received: {}", jsonReceived);
            logger.info("ProfileDTO toString: {}", profileDTO.toString());
            logger.info("Avatar ID in request: {}", profileDTO.getAvatarId());
            logger.info("============================");
        } catch (Exception e) {
            logger.error("Error logging ProfileDTO: {}", e.getMessage());
        }

        Profile updatedProfile = service.update(profileDTO, id);
        ProfileDTO responseDTO = toProfileDTO(updatedProfile);

        logger.info("Updated profile avatar ID: {}", responseDTO.getAvatarId());

        return ResponseEntity.accepted().body(responseDTO);
    }

    @PutMapping(path = "/user/profile/favorite/{id}")
    public ResponseEntity<String> addRemoveFavorite(@PathVariable Long id) throws Exception {

        String message = service.updateFavorites(id);

        return ResponseEntity.status(200).body(message);
    }

    @GetMapping(path = "/user/profile/favorite/{id}")
    public ResponseEntity<Profile> getFavorite(@PathVariable Long id) throws Exception {
        Profile profile = service.getFavorites(id);
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(profile);
    }

    @DeleteMapping(path = "/user/profile/favorite/{id}")
    public ResponseEntity<String> deleteFavorite(@PathVariable Long id) throws Exception {

        String message = service.updateFavorites(id);

        return ResponseEntity.status(200).body(message);
    }

    @PostMapping(path = "/user/profile/favorite/{id}")
    public ResponseEntity<Profile> Favorite(@PathVariable Long id) throws Exception {
        Profile profile = service.getFavorites(id);
        if (profile == null) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(profile);
    }

    @DeleteMapping(path = "/user/profile/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws Exception {
        String message = service.delete(id);
        return ResponseEntity.status(200).body(message);
    }

    // ============= ENDPOINTS ADMINISTRATIVOS =============
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/admin/user/profile/{id}")
    public ResponseEntity<ProfileDTO> updateProfileAsAdmin(@PathVariable Long id, @RequestBody ProfileDTO profileDTO) {
        try {
            // Log the complete JSON received
            String jsonReceived = objectMapper.writeValueAsString(profileDTO);
            logger.info("=== ADMIN PROFILE UPDATE REQUEST ===");
            logger.info("Profile ID: {}", id);
            logger.info("JSON received: {}", jsonReceived);
            logger.info("============================");

            Profile updatedProfile = service.update(profileDTO, id);
            ProfileDTO responseDTO = toProfileDTO(updatedProfile);

            logger.info("Updated profile avatar ID: {}", responseDTO.getAvatarId());

            return ResponseEntity.accepted().body(responseDTO);
        } catch (Exception e) {
            logger.error("Error updating profile as admin: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/admin/user/profile/{id}")
    public ResponseEntity<String> deleteProfileAsAdmin(@PathVariable Long id) {
        try {
            String message = service.delete(id);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            logger.error("Error deleting profile as admin: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error al eliminar perfil: " + e.getMessage());
        }
    }

    // ============= ENDPOINTS PARA AUTO-EDICIÓN/ELIMINACIÓN (GDPR) =============
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/user/profile/me")
    public ResponseEntity<ProfileDTO> updateSelfProfile(@RequestBody ProfileDTO profileDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();

            Optional<User> userOptional = userService.findByUsername(currentUsername);
            if (userOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            User user = userOptional.get();
            if (user.getProfile() == null) {
                return ResponseEntity.notFound().build();
            }

            // Actualizar solo los campos del perfil proporcionados
            Profile existingProfile = user.getProfile();

            if (profileDTO.getFirstName() != null) {
                existingProfile.setFirstName(profileDTO.getFirstName());
            }
            if (profileDTO.getLastName1() != null) {
                existingProfile.setLastName1(profileDTO.getLastName1());
            }
            if (profileDTO.getLastName2() != null) {
                existingProfile.setLastName2(profileDTO.getLastName2());
            }
            if (profileDTO.getUsername() != null) {
                existingProfile.setUsername(profileDTO.getUsername());
                user.setUsername(profileDTO.getUsername()); // Sincronizar con usuario
            }
            if (profileDTO.getEmail() != null) {
                existingProfile.setEmail(profileDTO.getEmail());
                user.setEmail(profileDTO.getEmail()); // Sincronizar con usuario
            }
            if (profileDTO.getRelationship() != null) {
                existingProfile.setRelationship(profileDTO.getRelationship());
            }
            if (profileDTO.getCity() != null) {
                existingProfile.setCity(profileDTO.getCity());
            }
            if (profileDTO.getCountry() != null) {
                existingProfile.setCountry(profileDTO.getCountry());
            }
            if (profileDTO.getPhone() != null) {
                existingProfile.setPhone(profileDTO.getPhone());
            }
            if (profileDTO.getAvatarId() != null) {
                // Aquí debería actualizar el avatar, pero depende de la implementación
                logger.info("Avatar ID update requested: {}", profileDTO.getAvatarId());
            }

            // Actualizar ambos (usuario y perfil)
            userService.save(user);

            // Crear ProfileDTO actualizado para pasarlo al servicio
            ProfileDTO updatedProfileDTO = ProfileDTO.builder()
                    .firstName(existingProfile.getFirstName())
                    .lastName1(existingProfile.getLastName1())
                    .lastName2(existingProfile.getLastName2())
                    .username(existingProfile.getUsername())
                    .relationship(existingProfile.getRelationship())
                    .email(existingProfile.getEmail())
                    .city(existingProfile.getCity())
                    .country(existingProfile.getCountry())
                    .phone(existingProfile.getPhone())
                    .avatarId(profileDTO.getAvatarId())
                    .build();

            Profile updatedProfile = service.update(updatedProfileDTO, existingProfile.getId());

            return ResponseEntity.ok(toProfileDTO(updatedProfile));
        } catch (Exception e) {
            logger.error("Error updating self profile: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/user/profile/me")
    public ResponseEntity<String> deleteSelfAccount() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();

            Optional<User> userOptional = userService.findByUsername(currentUsername);
            if (userOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            User user = userOptional.get();

            if (!gdprService.canDeleteUser(user.getId())) {
                return ResponseEntity.badRequest().body("No se puede eliminar el último administrador del sistema");
            }

            // Usar GdprService para eliminación completa en cascada
            gdprService.deleteAllUserData(user.getId());
            return ResponseEntity.ok("Tu cuenta y todos tus datos han sido eliminados permanentemente conforme al GDPR");
        } catch (Exception e) {
            logger.error("Error deleting self account: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error al eliminar tu cuenta: " + e.getMessage());
        }
    }

    // ============= MÉTODOS AUXILIARES =============
    public static ProfileDTO toProfileDTO(Profile profile) {
        return ProfileDTO.builder()
                .id(profile.getId())
                .userId(profile.getUser() != null ? profile.getUser().getId() : null)
                .firstName(profile.getFirstName())
                .lastName1(profile.getLastName1())
                .lastName2(profile.getLastName2())
                .username(profile.getUsername())
                .relationship(profile.getRelationship())
                .email(profile.getEmail())
                .password(profile.getPassword())
                .confirmPassword(profile.getConfirmPassword())
                .city(profile.getCity())
                .country(profile.getCountry())
                .phone(profile.getPhone())
                .avatarId(profile.getAvatar() != null ? profile.getAvatar().getId() : null)
                .build();
    }

}
