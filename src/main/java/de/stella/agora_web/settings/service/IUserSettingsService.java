package de.stella.agora_web.settings.service;

import de.stella.agora_web.settings.controller.dto.UserSettingsDTO;

public interface IUserSettingsService {

    UserSettingsDTO getSettings(Long userId);

    UserSettingsDTO saveSettings(Long userId, UserSettingsDTO dto);

    void deleteSettings(Long userId);
}
