package de.stella.agora_web.settings.service;

import de.stella.agora_web.settings.controller.dto.UserSettingsDTO;
import de.stella.agora_web.settings.dto.SystemConfigDTO;

/**
 * 🌍 SERVICIO PÚBLICO DE SETTINGS
 *
 * Proporciona configuraciones por defecto para usuarios no autenticados. NO
 * maneja persistencia - solo valores por defecto para el frontend.
 */
public interface PublicSettingsService {

    /**
     * ✅ Obtiene configuraciones por defecto del sistema
     *
     * @return Settings por defecto (accesibilidad básica)
     */
    UserSettingsDTO getDefaultSettings();

    /**
     * ✅ Obtiene configuración del sistema (opciones disponibles)
     *
     * @return Configuraciones disponibles del sistema
     */
    SystemConfigDTO getSystemConfig();
}
