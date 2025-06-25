package de.stella.agora_web.settings.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.stella.agora_web.settings.model.UserSettings;
import de.stella.agora_web.user.model.User;

public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {

    Optional<UserSettings> findByUser(User user);

    Optional<UserSettings> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
