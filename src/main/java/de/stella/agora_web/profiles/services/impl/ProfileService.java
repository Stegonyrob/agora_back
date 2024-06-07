package de.stella.agora_web.profiles.services.impl;

import java.util.Set;

import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import de.stella.agora_web.generics.IGenericGetService;
import de.stella.agora_web.generics.IGenericUpdateService;
import de.stella.agora_web.profiles.controller.dto.ProfileDTO;
import de.stella.agora_web.profiles.exceptions.ProfileNotFoundException;
import de.stella.agora_web.profiles.model.Profile;
import de.stella.agora_web.profiles.repository.ProfileRepository;
import de.stella.agora_web.replys.exceptions.ReplyNotFoundException;
import de.stella.agora_web.replys.model.Reply;
import de.stella.agora_web.replys.repository.ReplyRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProfileService implements IGenericUpdateService<ProfileDTO, Profile>, IGenericGetService<Profile> {

    ProfileRepository repository;
    ReplyRepository replyRepository;

    @PreAuthorize("hasRole('USER')")
    public Profile getById(@NonNull Long id)throws Exception{
        Profile profile = repository.findById(id).orElseThrow(() -> new ProfileNotFoundException("Profile not found"));

        return profile;
    }

    @PreAuthorize("hasRole('USER')")
    public Profile getByEmail(@NonNull String email)throws Exception{
        Profile profile = repository.findByEmail(email).orElseThrow(() -> new ProfileNotFoundException("Profile not found"));
        return profile;
    }

    @PreAuthorize("hasRole('USER')")
    @Override
    public Profile update(ProfileDTO profileDTO, Long id) {
       Profile profile = repository.findById(id).orElseThrow(()-> new ProfileNotFoundException("Profile Not Found"));

       profile.setFirstName(profileDTO.getFirstName());
       profile.setFirstLastName(profileDTO.getFirstLastName());
       profile.setSecondLastName(profileDTO.getSecondLastName());
       profile.setEmail(profileDTO.getEmail());
       profile.setAddress(profileDTO.getAddress());
       profile.setNumberPhone(profileDTO.getNumberPhone());
       profile.setPostalCode(profileDTO.getPostalCode());
       profile.setCity(profileDTO.getCity());
       profile.setProvince(profileDTO.getProvince());

       return repository.save(profile);
    }


    @PreAuthorize("hasRole('USER')")
    public String updateFavorites(Long ReplyId) throws Exception {
        
        SecurityContext contextHolder = SecurityContextHolder.getContext();
        Authentication auth = contextHolder.getAuthentication();
        
        Profile updatingProfile = repository.findByEmail(auth.getName()).orElseThrow(() -> new ProfileNotFoundException("Profile not found"));

        Reply newReply = replyRepository.findById(ReplyId).orElseThrow(() -> new ReplyNotFoundException("Reply not found")); // Using the autowired instance

        Set<Reply> favoriteReplys = updatingProfile.getFavorites();

        String message = "";

        if (favoriteReplys.contains(newReply)) {
            favoriteReplys.remove(newReply);
            message = "Reply is removed from favorites";
        } else {
            favoriteReplys.add(newReply);
            message = "Reply is added to favorites";
        }

        updatingProfile.setFavorites(favoriteReplys);

        repository.save(updatingProfile);
        
        return message;
    }
    
}
