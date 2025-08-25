package de.stella.agora_web.user.service.impl;

import org.springframework.stereotype.Service;

import de.stella.agora_web.user.service.ITotpService;

/**
 * 🔐 Implementación temporal del servicio TOTP
 *
 * Esta implementación proporciona funcionalidad básica para permitir que la
 * aplicación compile y arranque correctamente.
 */
@Service
public class TotpServiceImpl implements ITotpService {

    @Override
    public String generateTotpSecret() {
        // Implementación temporal - genera un secreto básico
        return "TEMP_TOTP_SECRET_" + System.currentTimeMillis();
    }

    @Override
    public boolean validateTotpCode(String base64Secret, String code) {
        // Implementación temporal - siempre válido para testing
        // TODO: Implementar validación real de TOTP
        return code != null && code.length() == 6;
    }
}
