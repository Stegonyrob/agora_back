package de.stella.agora_web.admin.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import de.stella.agora_web.admin.controller.dto.AdminProfileDTO;
import de.stella.agora_web.admin.service.IAdminProfileService;

/**
 * 👤 Implementación temporal del Servicio de Perfiles Administrativos
 */
@Service
public class AdminProfileServiceImpl implements IAdminProfileService {

    @Override
    public List<AdminProfileDTO> getAllAdminProfiles() {
        return new ArrayList<>();
    }

    @Override
    public AdminProfileDTO getAdminProfileById(Long profileId) {
        return null;
    }

    @Override
    public AdminProfileDTO getProfileByAdminUserId(Long userId) {
        return null;
    }

    @Override
    public AdminProfileDTO createAdminProfile(Long userId, String firstName, String lastName, Long avatarId) {
        AdminProfileDTO profile = new AdminProfileDTO();
        profile.setId(1L);
        profile.setUserId(userId);
        profile.setFirstName(firstName);
        profile.setLastName(lastName);
        profile.setAvatarId(avatarId);
        return profile;
    }

    @Override
    public AdminProfileDTO updateAdminProfile(Long profileId, String firstName, String lastName, Long avatarId) {
        AdminProfileDTO profile = new AdminProfileDTO();
        profile.setId(profileId);
        profile.setFirstName(firstName);
        profile.setLastName(lastName);
        profile.setAvatarId(avatarId);
        return profile;
    }

    @Override
    public AdminProfileDTO updateAdminAvatar(Long profileId, Long avatarId) {
        AdminProfileDTO profile = new AdminProfileDTO();
        profile.setId(profileId);
        profile.setAvatarId(avatarId);
        return profile;
    }

    @Override
    public void deleteAdminProfile(Long profileId) {
        // Implementación temporal - no hace nada
    }

    @Override
    public boolean existsProfileForUser(Long userId) {
        return false;
    }
}

