package de.stella.agora_web.security.csrf.service;

import java.util.Optional;

import org.springframework.security.web.csrf.CsrfToken;

import de.stella.agora_web.security.csrf.dto.CsrfTokenInfo;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 🛡️ Servicio para gestión centralizada de tokens CSRF
 *
 * Responsabilidades: - Extraer tokens CSRF de requests - Validar tokens CSRF -
 * Proporcionar información del token
 *
 * Principios SOLID aplicados: - SRP: Una sola responsabilidad (gestión CSRF) -
 * OCP: Abierto para extensión (diferentes implementaciones) - DIP: Depende de
 * abstracciones, no concreciones
 */
public interface ICsrfTokenService {

    /**
     * Extrae el token CSRF del request actual
     *
     * @param request HttpServletRequest
     * @return Optional con el token CSRF o vacío si no existe
     */
    Optional<CsrfToken> extractCsrfToken(HttpServletRequest request);

    /**
     * Valida que el token CSRF esté presente en el request
     *
     * @param request HttpServletRequest
     * @return true si el token es válido, false en caso contrario
     */
    boolean isValidCsrfToken(HttpServletRequest request);

    /**
     * Obtiene información completa del token CSRF para respuesta
     *
     * @param request HttpServletRequest
     * @return CsrfTokenInfo con toda la información necesaria
     */
    Optional<CsrfTokenInfo> getCsrfTokenInfo(HttpServletRequest request);
}
