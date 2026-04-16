package de.stella.agora_web.security.csrf.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 📋 DTO para información de token CSRF
 *
 * Encapsula toda la información necesaria sobre un token CSRF siguiendo el
 * principio de responsabilidad única.
 *
 * Principios aplicados: - SRP: Solo contiene datos del token CSRF - Immutable:
 * Datos del token no deben cambiar después de creación - Clean Code: Nombres
 * descriptivos y estructura clara
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CsrfTokenInfo {

    /**
     * El token CSRF actual
     */
    private String token;

    /**
     * Nombre del header donde debe enviarse el token (ej: "X-CSRF-TOKEN")
     */
    private String headerName;

    /**
     * Nombre del parámetro de formulario donde debe enviarse el token (ej:
     * "_csrf")
     */
    private String parameterName;

    /**
     * Timestamp de cuándo se generó el token (para debugging)
     */
    private long timestamp;

    /**
     * Duración en segundos hasta que expira el token
     */
    private long expirationSeconds;

    /**
     * Constructor de conveniencia para casos básicos
     */
    public CsrfTokenInfo(String token, String headerName, String parameterName) {
        this.token = token;
        this.headerName = headerName;
        this.parameterName = parameterName;
        this.timestamp = System.currentTimeMillis();
        this.expirationSeconds = 1800; // 30 minutos por defecto
    }

    /**
     * Verifica si el token ha expirado
     *
     * @return true si el token ha expirado
     */
    public boolean isExpired() {
        long currentTime = System.currentTimeMillis();
        return (currentTime - timestamp) > (expirationSeconds * 1000);
    }

    /**
     * Obtiene el tiempo restante en segundos antes de que expire
     *
     * @return segundos restantes, 0 si ya expiró
     */
    public long getSecondsUntilExpiration() {
        long currentTime = System.currentTimeMillis();
        long elapsedSeconds = (currentTime - timestamp) / 1000;
        long remaining = expirationSeconds - elapsedSeconds;
        return Math.max(0, remaining);
    }
}
