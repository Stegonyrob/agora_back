package de.stella.agora_web.admin.controller.dto;

import de.stella.agora_web.profiles.controller.dto.ProfileDTO;

public class AdminUserDTO {

    private Long id;
    private String username;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
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
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
