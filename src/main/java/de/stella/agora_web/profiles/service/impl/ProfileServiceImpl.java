package de.stella.agora_web.profiles.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public Optional<Profile> findProfileById(Long profileId) {
        return profileDAO.findById(profileId);
    }

    @Override
    @Transactional
    public List<Profile> findAllProfiles() {
        return profileDAO.findAll();
    }

    @Override
    @Transactional
    public Profile saveProfile(Profile profile) {
        return profileDAO.save(profile);
    }

    @Override
    @Transactional
    public void deleteProfileById(Long id) {
        profileDAO.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<Profile> findProfileByUsernameAndPassword(String username, String password) {
        return profileDAO.findByUsernameAndPassword(username, password);
    }

    @Override
    @Transactional
    public boolean checkProfileUserRole(String username, String role) {
        return profileDAO.findByUsername(username).map(profile -> profile.hasRole(role)).orElse(false);
    }

    @Override
    @Transactional
    public Profile updateProfile(Profile profile, Profile updatedProfile) {
        return profileDAO.update(profile, updatedProfile);
    }

    @Override
    @Transactional
    public List<Profile> getProfilesById(List<Long> ids) {
        return profileDAO.findAllById(ids);
    }

    @Override
    @Transactional
    public Optional<Profile> findProfileByUsername(String username) {
        return profileDAO.findByUsername(username);
    }

    @Transactional
    public Profile getById(Long id) {
        return profileDAO.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    @Transactional
    public Profile getByEmail(String email) {
        return profileDAO.findByEmail(email).orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    @Transactional
    public Profile update(ProfileDTO profileDTO, Long id) {
        Profile existingProfile = getById(id);
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

    @Transactional
    public String updateFavorites(Long id) {
        Profile profile = getById(id);
        profile.setFavorite(!profile.isFavorite());
        Profile updatedProfile = profileDAO.save(profile);
        return updatedProfile.isFavorite() ? "Added to favorites" : "Removed from favorites";
    }

    @Override
    @Transactional
    public Profile registerProfile(ProfileDTO profileDTO) {
        return profileDAO.save(new Profile());
    }

    @SuppressWarnings("unused")
    private String hashPassword(String password) {
        // Implement password hashing logic here
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public List<Profile> getAllProfiles() {
        return profileDAO.findAll();
    }
}

// package dev.mark.jewelsstorebackend.profiles;
// import java.util.Set;
// import org.springframework.lang.NonNull;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContext;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.stereotype.Service;
// import dev.mark.jewelsstorebackend.interfaces.IGenericGetService;
// import dev.mark.jewelsstorebackend.interfaces.IGenericUpdateService;
// import dev.mark.jewelsstorebackend.products.Product;
// import dev.mark.jewelsstorebackend.products.ProductNotFoundException;
// import dev.mark.jewelsstorebackend.products.ProductRepository;
// import lombok.AllArgsConstructor;
// @Service
// @AllArgsConstructor
// public class ProfileService implements IGenericUpdateService<ProfileDTO,
// Profile>, IGenericGetService<Profile> {
// ProfileRepository repository;
// ProductRepository productRepository;
// @PreAuthorize("hasRole('USER')")
// public Profile getById(@NonNull Long id)throws Exception{
// Profile profile = repository.findById(id).orElseThrow(() -> new
// ProfileNotFoundException("Profile not found"));
// return profile;
// }
// @PreAuthorize("hasRole('USER')")
// public Profile getByEmail(@NonNull String email)throws Exception{
// Profile profile = repository.findByEmail(email).orElseThrow(() -> new
// ProfileNotFoundException("Profile not found"));
// return profile;
// }
// @PreAuthorize("hasRole('USER')")
// @Override
// public Profile update(ProfileDTO profileDTO, Long id) {
// Profile profile = repository.findById(id).orElseThrow(()-> new
// ProfileNotFoundException("Profile Not Found"));
// profile.setFirstName(profileDTO.getFirstName());
// profile.setLastName(profileDTO.getLastName());
// profile.setAddress(profileDTO.getAddress());
// profile.setNumberPhone(profileDTO.getNumberPhone());
// profile.setPostalCode(profileDTO.getPostalCode());
// profile.setCity(profileDTO.getCity());
// profile.setProvince(profileDTO.getProvince());
// return repository.save(profile);
// }

// @PreAuthorize("hasRole('USER')")
// public String updateFavorites(Long productId) throws Exception {

// SecurityContext contextHolder = SecurityContextHolder.getContext();
// Authentication auth = contextHolder.getAuthentication();

// Profile updatingProfile =
// repository.findByEmail(auth.getName()).orElseThrow(() -> new
// ProfileNotFoundException("Profile not found"));
// Product newProduct = productRepository.findById(productId).orElseThrow(() ->
// new ProductNotFoundException("Product not found"));
// Set<Product> favoriteProducts = updatingProfile.getFavorites();
// String message = "";
// if (favoriteProducts.contains(newProduct)) {
// favoriteProducts.remove(newProduct);
// message = "Product is removed from favorites";
// } else {
// favoriteProducts.add(newProduct);
// message = "Product is added to favorites";
// }
// updatingProfile.setFavorites(favoriteProducts);
// repository.save(updatingProfile);

// return message;
// }

// }