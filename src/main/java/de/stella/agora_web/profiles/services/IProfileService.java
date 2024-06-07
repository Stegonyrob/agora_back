package de.stella.agora_web.profiles.services;

import java.util.List;

import de.stella.agora_web.profiles.controller.dto.ProfileDTO;
import de.stella.agora_web.profiles.exceptions.ProfileNotFoundException;
import de.stella.agora_web.profiles.model.Profile;
import de.stella.agora_web.user.model.User;
import lombok.NonNull;

public interface IProfileService {
    
    List<Profile> getAllProfiles();

    Profile getProfileById(Long id);

    Profile createProfile( ProfileDTO profileDTO, User user);

    Profile update(ProfileDTO profileDTO, Long id) throws Exception;

    void deleteProfile(Long id);

    Profile save( ProfileDTO profileDTO);

    Profile fetchById(Long profileId) throws ProfileNotFoundException;
    Profile getByEmail(@NonNull String email) throws Exception;

Profile findByEmail(String email) throws ProfileNotFoundException;}
