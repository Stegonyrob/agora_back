package de.stella.agora_web.profiles.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.stella.agora_web.profiles.controller.dto.ProfileDTO;
import de.stella.agora_web.profiles.model.Profile;
import de.stella.agora_web.profiles.service.impl.ProfileServiceImpl;

@RestController
@RequestMapping(path = "${api-endpoint}/any/")
public class ProfileController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    ProfileServiceImpl service;

    public ProfileController(ProfileServiceImpl service) {
        this.service = service;
    }

    @PostAuthorize("returnObject.body.user.id == principal.id")
    @PostMapping(path = "/user/profile/{id}")
    public ResponseEntity<Profile> getProfileById(@RequestBody ProfileDTO profileDTO) throws Exception {
        Profile profile = service.getById(profileDTO.getUserId());
        return ResponseEntity.ok(profile);
    }

    @PostAuthorize("returnObject.body.id == authentication.principal.id")
    @GetMapping(path = "/user/profile/{id}")
    public ResponseEntity<?> getById(@NonNull @PathVariable Long id) throws Exception {
        Profile profile = service.getById(id);
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(profile);
    }

    @PostAuthorize("returnObject.body.id == authentication.principal.id")
    @PutMapping(path = "/user/profile/{id}")
    public ResponseEntity<Profile> update(@PathVariable Long id, @RequestBody ProfileDTO profileDTO) throws Exception {
        try {
            // Log the complete JSON received
            String jsonReceived = objectMapper.writeValueAsString(profileDTO);
            logger.info("=== PROFILE UPDATE REQUEST ===");
            logger.info("Profile ID: {}", id);
            logger.info("JSON received: {}", jsonReceived);
            logger.info("ProfileDTO toString: {}", profileDTO.toString());
            logger.info("============================");
        } catch (Exception e) {
            logger.error("Error logging ProfileDTO: {}", e.getMessage());
        }

        Profile profile = service.update(profileDTO, id);
        return ResponseEntity.accepted().body(profile);
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
                .build();
    }

}
