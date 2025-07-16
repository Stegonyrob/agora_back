package de.stella.agora_web.admin.controller.dto;

import de.stella.agora_web.profiles.controller.dto.ProfileDTO;

public class AdminUserDTO {

    private ProfileDTO profile;
    private boolean admin;
    private boolean active;

    public AdminUserDTO(ProfileDTO profile, boolean admin, boolean active) {
        this.profile = profile;
        this.admin = admin;
        this.active = active;
    }

    public AdminUserDTO() {
    }

    public ProfileDTO getProfile() {
        return profile;
    }

    public void setProfile(ProfileDTO profile) {
        this.profile = profile;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDisplayName() {
        return profile != null ? profile.getUsername() : null;
    }
}
