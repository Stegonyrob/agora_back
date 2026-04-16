package de.stella.agora_web.profiles.service;

import java.util.List;
import java.util.Optional;

import de.stella.agora_web.profiles.controller.dto.ProfileDTO;
import de.stella.agora_web.profiles.model.Profile;

public interface IProfileService {

    Profile getById(Long id) throws Exception;

    Profile getByEmail(String email) throws Exception;

    Profile updateProfile(ProfileDTO profileDTO, Long id) throws Exception;

    List<Profile> findAllProfiles();

    Optional<Profile> findById(Long id);

    // Favoritos de posts
    String addFavoritePost(Long profileId, Long postId);

    String deleteFavoritePost(Long profileId, Long postId);

    // Puedes dejar los métodos de comentarios si los necesitas
}
