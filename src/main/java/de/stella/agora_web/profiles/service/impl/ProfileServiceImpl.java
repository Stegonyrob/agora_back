package de.stella.agora_web.profiles.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.stella.agora_web.profiles.controller.dto.ProfileDTO;
import de.stella.agora_web.profiles.model.Profile;
import de.stella.agora_web.profiles.persistence.IProfileDAO;
import de.stella.agora_web.profiles.service.IProfileService;

@Service
public class ProfileServiceImpl implements IProfileService {

    private final IProfileDAO profileDAO;

    public ProfileServiceImpl(IProfileDAO profileDAO) {
        this.profileDAO = profileDAO;
    }


    @Override
    public Optional<Profile> findProfileById(Long profileId) {
        return profileDAO.findById(profileId);
    }

    @Override
    public List<Profile> findAllProfiles() {
        return profileDAO.findAll();
    }

    @Override
    public Profile saveProfile(Profile profile) {
        return profileDAO.save(profile);
    }

    @Override
    public void deleteProfileById(Long id) {
        profileDAO.deleteById(id);
    }

    @Override
    public Optional<Profile> findProfileByUsernameAndPassword(String username, String password) {
        return profileDAO.findByUsernameAndPassword(username, password);
    }

    @Override
    public boolean checkProfileUserRole(String username, String role) {
        return profileDAO.findByUsername(username)
                .map(profile -> profile.hasRole(role))
                .orElse(false);
    }

    @Override
    public Profile updateProfile(Profile profile, Profile updatedProfile) {
        return profileDAO.update(profile, updatedProfile);
    }

    @Override
    public List<Profile> getAllProfiles() {
        return findAllProfiles();
    }

    @Override
    public Profile registerProfile(ProfileDTO profileDTO) {
        Profile profile = new Profile(
                profileDTO.getFirstName(),
                profileDTO.getLastName1(),
                profileDTO.getLastName2(),
                profileDTO.getUsername(),
                profileDTO.getRelationship(),
                profileDTO.getEmail(),
                profileDTO.getPassword(),
                profileDTO.getConfirmPassword(),
                profileDTO.getCity()
        );
        return profileDAO.save(profile);
    }

    @Override
    public List<Profile> getProfilesById(List<Long> ids) {
        return profileDAO.findAllById(ids);
    }

    @Override
    public Optional<Profile> findProfileByUsername(String username) {
        return profileDAO.findByUsername(username);
    }

    public Profile getById(Long id) {
        return profileDAO.findById(id)
                .orElseThrow();
    }

    public Profile getByEmail(String email) {
        return profileDAO.findByEmail(email)
                .orElseThrow();
    }

    public Profile update(ProfileDTO profileDTO, Long id) {
        Profile existingProfile = profileDAO.findById(id)
                .orElseThrow();
        existingProfile.setFirstName(profileDTO.getFirstName());
        existingProfile.setLastName1(profileDTO.getLastName1());
        existingProfile.setLastName2(profileDTO.getLastName2());
        existingProfile.setUsername(profileDTO.getUsername());
        existingProfile.setRelationship(profileDTO.getRelationship());
        existingProfile.setEmail(profileDTO.getEmail());
        existingProfile.setPassword(profileDTO.getPassword());
        existingProfile.setConfirmPassword(profileDTO.getConfirmPassword());
        existingProfile.setCity(profileDTO.getCity());
        return profileDAO.save(existingProfile);
    }

    public String updateFavorites(Long id) {
        Profile profile = profileDAO.findById(id)
                .orElseThrow();
        profile.setFavorite(!profile.isFavorite());
        Profile updatedProfile = profileDAO.save(profile);
        return updatedProfile.isFavorite() ? "Added to favorites" : "Removed from favorites";
    }

}
