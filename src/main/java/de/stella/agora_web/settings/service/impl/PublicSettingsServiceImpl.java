package de.stella.agora_web.settings.service.impl;

import org.springframework.stereotype.Service;

import de.stella.agora_web.settings.controller.dto.UserSettingsDTO;
import de.stella.agora_web.settings.dto.SystemConfigDTO;
import de.stella.agora_web.settings.service.PublicSettingsService;

/**
 * 🌍 IMPLEMENTACIÓN DEL SERVICIO PÚBLICO DE SETTINGS
 *
 * Proporciona configuraciones por defecto para usuarios no autenticados. Estas
 * configuraciones son constantes y NO se persisten en base de datos.
 */
@Service
public class PublicSettingsServiceImpl implements PublicSettingsService {

    /**
     * ✅ Settings por defecto del sistema para usuarios no autenticados
     */
    @Override
    public UserSettingsDTO getDefaultSettings() {
        UserSettingsDTO defaultSettings = new UserSettingsDTO();

        // Configuraciones por defecto basadas en la estructura actual
        defaultSettings.setUserId(null);                    // Sin usuario
        defaultSettings.setFontSize(16);                    // Tamaño de fuente mediano
        defaultSettings.setHighContrast(false);            // Sin alto contraste
        defaultSettings.setAnimations(true);               // Animaciones activadas
        defaultSettings.setDaltonic(false);                // Sin daltonismo
        defaultSettings.setShowPersonalInfo(false);        // Sin info personal
        defaultSettings.setTwoFA(false);                   // Sin 2FA
        defaultSettings.setSocialLinks("");                // Sin enlaces sociales

        return defaultSettings;
    }

    /**
     * ✅ Configuraciones del sistema disponibles
     */
    @Override
    public SystemConfigDTO getSystemConfig() {
        return new SystemConfigDTO(
                new String[]{"light", "dark", "auto"}, // Temas disponibles
                new String[]{"es", "en", "ca"}, // Idiomas disponibles
                new String[]{"high-contrast", "large-text", "reduced-motion"}, // Opciones accesibilidad
                "light", // Tema por defecto
                "es" // Idioma por defecto
        );
    }
}
