package de.stella.agora_web.settings.controller;

import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.settings.controller.dto.UserSettingsDTO;
import de.stella.agora_web.settings.service.impl.UserSettingsServiceImpl;

@RestController

@RequestMapping("${api-endpoint}/any/user/settings")
public class UserSettingsController {

    private final UserSettingsServiceImpl service;

    public UserSettingsController(UserSettingsServiceImpl service) {
        this.service = service;
    }

    @PreAuthorize("#userId == authentication.principal.id")
    @GetMapping("/{userId}")
    public ResponseEntity<UserSettingsDTO> getSettings(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getSettings(userId));
    }

    @PreAuthorize("#userId == authentication.principal.id")
    @PostMapping("/{userId}")
    public ResponseEntity<UserSettingsDTO> createSettings(@PathVariable Long userId, @RequestBody UserSettingsDTO dto) {
        Objects.requireNonNull(userId, "userId cannot be null");
        Objects.requireNonNull(dto, "dto cannot be null");

        return ResponseEntity.ok(service.saveSettings(userId, dto));
    }

    @PreAuthorize("#userId == authentication.principal.id")
    @PutMapping("/{userId}")
    public ResponseEntity<UserSettingsDTO> updateSettings(@PathVariable Long userId, @RequestBody UserSettingsDTO dto) {
        return ResponseEntity.ok(service.saveSettings(userId, dto));
    }

    @PreAuthorize("#userId == authentication.principal.id")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteSettings(@PathVariable Long userId) {
        service.deleteSettings(userId);
        return ResponseEntity.noContent().build();
    }
}
