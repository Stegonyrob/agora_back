package de.stella.agora_web.profiles.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import de.stella.agora_web.comment.exceptions.CommentNotFoundException;
import de.stella.agora_web.comment.model.Comment;
import de.stella.agora_web.comment.repository.CommentRepository;
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
    private final CommentRepository commentRepository;

    @PreAuthorize("hasRole('USER')")

    public Profile getById(@NonNull Long id) throws ProfileNotFoundException {
        return repository.findById(id).orElseThrow(() -> new ProfileNotFoundException("Profile not found"));
    }

    @PreAuthorize("hasRole('USER')")

    public Profile getByEmail(@NonNull String email) throws ProfileNotFoundException {
        return repository.findByEmail(email).orElseThrow(() -> new ProfileNotFoundException("Profile not found"));
    }

    @PreAuthorize("hasRole('USER')")

    public Profile updateProfile(ProfileDTO profileDTO, Long id) throws ProfileNotFoundException {
        Profile profile = repository.findById(id).orElseThrow(() -> new ProfileNotFoundException("Profile Not Found"));

        profile.setFirstName(profileDTO.getFirstName());
        profile.setLastName1(profileDTO.getLastName1());
        profile.setLastName2(profileDTO.getLastName2());
        profile.setUsername(profileDTO.getUsername());
        profile.setRelationship(profileDTO.getRelationship());
        profile.setCity(profileDTO.getCity());
        profile.setCountry(profileDTO.getCountry());
        profile.setEmail(profileDTO.getEmail());
        profile.setPhone(profileDTO.getPhone());

        return repository.save(profile);
    }

    @PreAuthorize("hasRole('USER')")

    public String updateFavorites(Long commentId) throws Exception {
        SecurityContext contextHolder = SecurityContextHolder.getContext();
        Authentication auth = contextHolder.getAuthentication();

        Profile updatingProfile = repository.findByEmail(auth.getName())
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found"));
        Comment newComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));

        Set<Comment> favoriteComments = updatingProfile.getFavorites();
        String message = "";

        if (favoriteComments.contains(newComment)) {
            favoriteComments.remove(newComment);
            message = "Comment is removed from favorites";
        } else {
            favoriteComments.add(newComment);
            message = "Comment is added to favorites";
        }

        updatingProfile.setFavorites(favoriteComments);
        repository.save(updatingProfile);

        return message;
    }

    @Override
    public List<Profile> findAllProfiles() {
        return repository.findAll();
    }

    @Override
    public Optional<Profile> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public String addComment(Long profileId, Long commentId) {
        Profile profile = findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found with ID: " + profileId));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with ID: " + commentId));

        Set<Comment> comments = profile.getComments();
        String message = "";

        if (comments.contains(comment)) {
            comments.remove(comment);
            message = "Comment is removed from profile";
        } else {
            comments.add(comment);
            message = "Comment is added to profile";
        }

        profile.setComments(comments);
        repository.save(profile);

        return message;
    }

    @Override
    public String deleteComment(Long profileId, Long commentId) {
        Profile profile = findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found with ID: " + profileId));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with ID: " + commentId));

        Set<Comment> comments = profile.getComments();
        if (comments.remove(comment)) {
            profile.setComments(comments);
            repository.save(profile);
            return "Comment is removed from profile";
        } else {
            return "Comment not found in profile";
        }
    }

    @Override
    public String addFavorite(Long profileId, Long commentId) {
        Profile profile = findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found with ID: " + profileId));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with ID: " + commentId));

        Set<Comment> favorites = profile.getFavoriteComments();
        if (favorites.add(comment)) {
            profile.setFavoriteComments(favorites);
            repository.save(profile);
            return "Comment is added to favorites";
        } else {
            return "Comment is already in favorites";
        }
    }

    @Override
    public String deleteFavorite(Long profileId, Long commentId) {
        Profile profile = findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found with ID: " + profileId));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with ID: " + commentId));

        Set<Comment> favorites = profile.getFavoriteComments();
        if (favorites.remove(comment)) {
            profile.setFavoriteComments(favorites);
            repository.save(profile);
            return "Comment is removed from favorites";
        } else {
            return "Comment not found in favorites";
        }
    }

    public Profile update(ProfileDTO profileDTO, Long id) throws ProfileNotFoundException {
        Profile profile = repository.findById(id).orElseThrow(() -> new ProfileNotFoundException("Profile Not Found"));

        profile.setFirstName(profileDTO.getFirstName());
        profile.setLastName1(profileDTO.getLastName1());
        profile.setLastName2(profileDTO.getLastName2());
        profile.setUsername(profileDTO.getUsername());
        profile.setRelationship(profileDTO.getRelationship());
        profile.setCity(profileDTO.getCity());
        profile.setCountry(profileDTO.getCountry());
        profile.setEmail(profileDTO.getEmail());
        profile.setPhone(profileDTO.getPhone());

        return repository.save(profile);
    }

    public Profile getFavorites(Long id) throws ProfileNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found with ID: " + id));
    }

    public String delete(Long id) {
        repository.deleteById(id);
        return "Profile deleted";
    }
}
