package de.stella.agora_web.settings.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.settings.controller.dto.UserSettingsDTO;
import de.stella.agora_web.settings.dto.SystemConfigDTO;
import de.stella.agora_web.settings.service.PublicSettingsService;

/**
 * 🌍 CONTROLADOR PÚBLICO DE SETTINGS
 *
 * Maneja configuraciones temporales para usuarios no autenticados. Estas
 * configuraciones NO se persisten en el servidor, son solo para proporcionar
 * valores por defecto que el frontend puede usar con localStorage/cookies.
 */
@RestController
@RequestMapping("${api-endpoint}/public/settings")
public class PublicSettingsController {

    private final PublicSettingsService publicSettingsService;

    public PublicSettingsController(PublicSettingsService publicSettingsService) {
        this.publicSettingsService = publicSettingsService;
    }

    /**
     * ✅ PÚBLICO: Obtiene configuraciones por defecto para usuarios no
     * autenticados
     *
     * @return Settings por defecto (accesibilidad, fuente, etc.)
     */
    @GetMapping("/default")
    public ResponseEntity<UserSettingsDTO> getDefaultSettings() {
        UserSettingsDTO defaultSettings = publicSettingsService.getDefaultSettings();
        return ResponseEntity.ok(defaultSettings);
    }

    /**
     * ✅ PÚBLICO: Obtiene configuraciones del sistema (constantes)
     *
     * @return Configuraciones disponibles del sistema
     */
    @GetMapping("/system")
    public ResponseEntity<SystemConfigDTO> getSystemConfig() {
        SystemConfigDTO systemConfig = publicSettingsService.getSystemConfig();
        return ResponseEntity.ok(systemConfig);
    }

    /**
     * 📝 NOTA IMPORTANTE:
     *
     * Este controlador NO tiene endpoints POST/PUT/DELETE porque: - Los
     * settings temporales se manejan en frontend (localStorage/cookies) - El
     * backend solo proporciona valores por defecto - Para persistencia real, el
     * usuario debe autenticarse y usar UserSettingsController
     */
}
