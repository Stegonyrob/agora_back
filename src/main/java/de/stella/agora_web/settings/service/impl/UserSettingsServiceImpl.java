package de.stella.agora_web.settings.service.impl;

import org.springframework.stereotype.Service;

import de.stella.agora_web.settings.controller.dto.UserSettingsDTO;
import de.stella.agora_web.settings.model.UserSettings;
import de.stella.agora_web.settings.repository.UserSettingsRepository;
import de.stella.agora_web.settings.service.IUserSettingsService;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.repository.UserRepository;

@Service
public class UserSettingsServiceImpl implements IUserSettingsService {

    private final UserSettingsRepository settingsRepo;
    private final UserRepository userRepo;

    public UserSettingsServiceImpl(UserSettingsRepository settingsRepo, UserRepository userRepo) {
        this.settingsRepo = settingsRepo;
        this.userRepo = userRepo;
    }

    @Override
    public UserSettingsDTO getSettings(Long userId) {
        UserSettings settings = settingsRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Settings not found"));
        return toDTO(settings);
    }

    @Override
    public UserSettingsDTO saveSettings(Long userId, UserSettingsDTO dto) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserSettings settings = settingsRepo.findByUserId(userId).orElse(new UserSettings());
        settings.setUser(user);
        settings.setFontSize(dto.getFontSize());
        settings.setHighContrast(dto.getHighContrast());
        settings.setAnimations(dto.getAnimations());
        settings.setDaltonic(dto.getDaltonic());
        settings.setShowPersonalInfo(dto.getShowPersonalInfo());
        settings.setTwoFA(dto.getTwoFA());
        settings.setSocialLinks(dto.getSocialLinks());
        settingsRepo.save(settings);
        return toDTO(settings);
    }

    @Override
    public void deleteSettings(Long userId) {
        settingsRepo.deleteByUserId(userId);
    }

    private UserSettingsDTO toDTO(UserSettings settings) {
        UserSettingsDTO dto = new UserSettingsDTO();
        dto.setUserId(settings.getUser().getId());
        dto.setFontSize(settings.getFontSize());
        dto.setHighContrast(settings.getHighContrast());
        dto.setAnimations(settings.getAnimations());
        dto.setDaltonic(settings.getDaltonic());
        dto.setShowPersonalInfo(settings.getShowPersonalInfo());
        dto.setTwoFA(settings.getTwoFA());
        dto.setSocialLinks(settings.getSocialLinks());
        return dto;
    }
}
