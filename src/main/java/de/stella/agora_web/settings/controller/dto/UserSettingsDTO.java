package de.stella.agora_web.settings.controller.dto;

import lombok.Data;

@Data
public class UserSettingsDTO {

    private Long userId;
    private Integer fontSize;
    private Boolean highContrast;
    private Boolean animations;
    private Boolean daltonic;
    private Boolean showPersonalInfo;
    private Boolean twoFA;
    private String socialLinks;
}
