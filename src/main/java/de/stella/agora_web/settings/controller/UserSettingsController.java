package de.stella.agora_web.settings.controller;

import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.settings.controller.dto.UserSettingsDTO;
import de.stella.agora_web.settings.service.PublicSettingsService;
import de.stella.agora_web.settings.service.impl.UserSettingsServiceImpl;

/**
 * 👤 CONTROLADOR DE SETTINGS DE USUARIO
 *
 * Maneja configuraciones permanentes para usuarios autenticados. Para usuarios
 * anónimos, usar PublicSettingsController.
 */
@RestController
@RequestMapping("${api-endpoint}/any/user/settings")
public class UserSettingsController {

    private final UserSettingsServiceImpl userSettingsService;
    private final PublicSettingsService publicSettingsService;

    public UserSettingsController(UserSettingsServiceImpl userSettingsService,
            PublicSettingsService publicSettingsService) {
        this.userSettingsService = userSettingsService;
        this.publicSettingsService = publicSettingsService;
    }

    /**
     * ✅ OBTENER SETTINGS - Público con lógica condicional
     *
     * @param userId ID del usuario
     * @param authentication Información de autenticación (puede ser null)
     * @return Settings del usuario o settings por defecto
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserSettingsDTO> getSettings(@PathVariable Long userId,
            Authentication authentication) {
        // Si no hay autenticación, devolver settings por defecto
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.ok(publicSettingsService.getDefaultSettings());
        }

        // Si está autenticado, verificar que sea el usuario correcto
        // (Esta validación se hace en el servicio)
        try {
            UserSettingsDTO settings = userSettingsService.getSettings(userId);
            return ResponseEntity.ok(settings);
        } catch (Exception e) {
            // Si no existen settings o hay error, devolver settings por defecto
            return ResponseEntity.ok(publicSettingsService.getDefaultSettings());
        }
    }

    /**
     * 🔐 CREAR SETTINGS - Solo usuario autenticado
     */
    @PreAuthorize("#userId == authentication.principal.id")
    @PostMapping("/{userId}")
    public ResponseEntity<UserSettingsDTO> createSettings(@PathVariable Long userId, @RequestBody UserSettingsDTO dto) {
        Objects.requireNonNull(userId, "userId cannot be null");
        Objects.requireNonNull(dto, "dto cannot be null");

        return ResponseEntity.ok(userSettingsService.saveSettings(userId, dto));
    }

    /**
     * 🔐 ACTUALIZAR SETTINGS - Solo usuario autenticado
     */
    @PreAuthorize("#userId == authentication.principal.id")
    @PutMapping("/{userId}")
    public ResponseEntity<UserSettingsDTO> updateSettings(@PathVariable Long userId, @RequestBody UserSettingsDTO dto) {
        return ResponseEntity.ok(userSettingsService.saveSettings(userId, dto));
    }

    /**
     * 🔐 ELIMINAR SETTINGS - Solo usuario autenticado
     */
    @PreAuthorize("#userId == authentication.principal.id")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteSettings(@PathVariable Long userId) {
        userSettingsService.deleteSettings(userId);
        return ResponseEntity.noContent().build();
    }
}
