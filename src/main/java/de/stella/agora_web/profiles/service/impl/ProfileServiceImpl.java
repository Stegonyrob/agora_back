package de.stella.agora_web.profiles.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import de.stella.agora_web.avatar.repository.AvatarRepository;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.posts.model.PostLove;
import de.stella.agora_web.posts.repository.PostLoveRepository;
import de.stella.agora_web.posts.repository.PostRepository;
import de.stella.agora_web.profiles.controller.dto.ProfileDTO;
import de.stella.agora_web.profiles.exceptions.ProfileNotFoundException;
import de.stella.agora_web.profiles.model.Profile;
import de.stella.agora_web.profiles.repository.ProfileRepository;
import de.stella.agora_web.profiles.service.IProfileService;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@AllArgsConstructor
public class ProfileServiceImpl implements IProfileService {

    private final ProfileRepository repository;
    private final PostRepository postRepository;
    private final PostLoveRepository postLoveRepository;
    private final AvatarRepository avatarRepository;

    @PreAuthorize("hasRole('USER')")
    @Override
    public Profile getById(@NonNull Long id) throws ProfileNotFoundException {
        // Usar consulta optimizada que incluye User y Roles en una sola consulta
        return repository.findByIdWithUserAndRoles(id)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found"));
    }

    @PreAuthorize("hasRole('USER')")
    @Override
    public Profile getByEmail(@NonNull String email) throws ProfileNotFoundException {
        return repository.findByEmail(email).orElseThrow(() -> new ProfileNotFoundException("Profile not found"));
    }

    @PreAuthorize("hasRole('USER')")
    @Override
    public Profile updateProfile(ProfileDTO profileDTO, Long id) throws ProfileNotFoundException {
        // Usar consulta optimizada solo con datos de User básicos (sin roles innecesarios para update)
        Profile profile = repository.findByIdWithUser(id)
                .orElseThrow(() -> new ProfileNotFoundException("Profile Not Found"));

        profile.setFirstName(profileDTO.getFirstName());
        profile.setLastName1(profileDTO.getLastName1());
        profile.setLastName2(profileDTO.getLastName2());
        profile.setUsername(profileDTO.getUsername());
        profile.setRelationship(profileDTO.getRelationship());
        profile.setCity(profileDTO.getCity());
        profile.setCountry(profileDTO.getCountry());
        profile.setEmail(profileDTO.getEmail());
        profile.setPhone(profileDTO.getPhone());

        // Manejar actualización del avatar
        if (profileDTO.getAvatarId() != null) {
            avatarRepository.findById(profileDTO.getAvatarId())
                    .ifPresentOrElse(
                            avatar -> {
                                profile.setAvatar(avatar);
                                System.out.println("Avatar actualizado: ID=" + avatar.getId() + ", imageName=" + avatar.getImageName());
                            },
                            () -> System.out.println("Avatar no encontrado con ID: " + profileDTO.getAvatarId())
                    );
        } else {
            // Si no se proporciona avatarId, mantener el avatar actual o usar el default
            if (profile.getAvatar() == null) {
                avatarRepository.findDefaultAvatar()
                        .ifPresent(defaultAvatar -> {
                            profile.setAvatar(defaultAvatar);
                            System.out.println("Asignado avatar por defecto: " + defaultAvatar.getImageName());
                        });
            }
        }

        Profile savedProfile = repository.save(profile);
        System.out.println("Perfil guardado con avatar ID: "
                + (savedProfile.getAvatar() != null ? savedProfile.getAvatar().getId() : "null"));

        return savedProfile;
    }

    @Override
    public List<Profile> findAllProfiles() {
        return repository.findAll();
    }

    @Override
    public Optional<Profile> findById(Long id) {
        return repository.findById(id);
    }

    // Favoritos de posts
    @Override
    public String addFavoritePost(Long profileId, Long postId) {
        Profile profile = repository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found with ID: " + profileId));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        boolean exists = postLoveRepository.existsByProfileAndPost(profile, post);
        if (exists) {
            return "Post is already in favorites";
        }
        PostLove postLove = new PostLove();
        postLove.setProfile(profile);
        postLove.setPost(post);
        postLoveRepository.save(postLove);
        return "Post is added to favorites";
    }

    @Override
    public String deleteFavoritePost(Long profileId, Long postId) {
        Profile profile = repository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found with ID: " + profileId));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        Optional<PostLove> postLoveOpt = postLoveRepository.findByProfileAndPost(profile, post);
        if (postLoveOpt.isPresent()) {
            postLoveRepository.delete(postLoveOpt.get());
            return "Post is removed from favorites";
        } else {
            return "Post not found in favorites";
        }
    }
    // Actualiza el perfil

    public Profile update(ProfileDTO profileDTO, Long id) throws ProfileNotFoundException {
        return updateProfile(profileDTO, id);
    }

    // Añade o elimina un post de favoritos (toggle)
    public String updateFavorites(Long postId) {
        // Obtén el usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // Usar findByEmail normal ya que no necesitamos User/Roles para esta operación
        Profile profile = repository.findByEmail(auth.getName())
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        Optional<PostLove> postLoveOpt = postLoveRepository.findByProfileAndPost(profile, post);
        if (postLoveOpt.isPresent()) {
            postLoveRepository.delete(postLoveOpt.get());
            return "Post is removed from favorites";
        } else {
            PostLove postLove = new PostLove();
            postLove.setProfile(profile);
            postLove.setPost(post);
            postLoveRepository.save(postLove);
            return "Post is added to favorites";
        }
    }

    // Devuelve el perfil con sus posts favoritos
    public Profile getFavorites(Long id) throws ProfileNotFoundException {
        Profile profile = repository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found with ID: " + id));
        // Los favoritos se acceden con profile.getPostLoves() o un método utilitario
        return profile;
    }

    // Elimina el perfil (derecho al olvido)
    public String delete(Long id) {
        if (id == null) {
            throw new NullPointerException("Profile ID cannot be null");
        }
        try {
            repository.deleteById(id);
            return "Profile deleted";
        } catch (Exception e) {
            throw new RuntimeException("Error deleting profile with ID: " + id, e);
        }
    }
}
