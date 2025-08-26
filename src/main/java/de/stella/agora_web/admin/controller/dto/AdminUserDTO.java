package de.stella.agora_web.admin.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    // Getters y Setters
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

    // Métodos de conveniencia que delegan al profile
    @JsonIgnore
    public Long getId() {
        return profile != null ? profile.getId() : null;
    }

    @JsonIgnore
    public String getUsername() {
        return profile != null ? profile.getUsername() : null;
    }

    @JsonIgnore
    public String getEmail() {
        return profile != null ? profile.getEmail() : null;
    }

    @JsonIgnore
    public String getPhone() {
        return profile != null ? profile.getPhone() : null;
    }

    @JsonIgnore
    public String getFirstName() {
        return profile != null ? profile.getFirstName() : null;
    }

    @JsonIgnore
    public String getLastName() {
        return profile != null ? profile.getLastName1() : null;
    }

    public String getDisplayName() {
        return profile != null ? profile.getUsername() : null;
    }
}
