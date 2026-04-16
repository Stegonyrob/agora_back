package de.stella.agora_web.security.csrf.service.impl;

import java.util.Optional;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Service;

import de.stella.agora_web.security.csrf.dto.CsrfTokenInfo;
import de.stella.agora_web.security.csrf.service.ICsrfTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * 🛡️ Implementación del servicio de tokens CSRF
 *
 * Gestiona la extracción, validación y provisión de información sobre tokens
 * CSRF de manera centralizada y segura.
 *
 * Principios SOLID aplicados: - SRP: Una sola responsabilidad (gestión CSRF) -
 * OCP: Implementación específica, intercambiable - LSP: Implementa
 * correctamente la interfaz - ISP: Interfaz específica para CSRF - DIP: Depende
 * de abstracciones Spring Security
 *
 * Clean Code: - Nombres descriptivos - Métodos pequeños y enfocados - Logging
 * estructurado - Manejo de errores explícito
 */
@Slf4j
@Service
public class CsrfTokenServiceImpl implements ICsrfTokenService {

    private static final String CSRF_TOKEN_ATTRIBUTE = CsrfToken.class.getName();
    private static final long DEFAULT_EXPIRATION_SECONDS = 1800; // 30 minutos

    @Override
    public Optional<CsrfToken> extractCsrfToken(HttpServletRequest request) {
        try {
            CsrfToken csrfToken = (CsrfToken) request.getAttribute(CSRF_TOKEN_ATTRIBUTE);

            if (csrfToken == null) {
                log.debug("🚨 CSRF Token no encontrado en request para URI: {}", request.getRequestURI());
                return Optional.empty();
            }

            log.debug("✅ CSRF Token extraído exitosamente para URI: {}", request.getRequestURI());
            return Optional.of(csrfToken);

        } catch (Exception e) {
            log.error("💥 Error extrayendo CSRF Token del request: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public boolean isValidCsrfToken(HttpServletRequest request) {
        Optional<CsrfToken> tokenOpt = extractCsrfToken(request);

        if (tokenOpt.isEmpty()) {
            log.warn("🚨 Validación CSRF fallida: Token no presente en request para URI: {}",
                    request.getRequestURI());
            return false;
        }

        CsrfToken token = tokenOpt.get();
        if (token.getToken() == null || token.getToken().trim().isEmpty()) {
            log.warn("🚨 Validación CSRF fallida: Token vacío para URI: {}",
                    request.getRequestURI());
            return false;
        }

        log.debug("✅ CSRF Token válido para URI: {}", request.getRequestURI());
        return true;
    }

    @Override
    public Optional<CsrfTokenInfo> getCsrfTokenInfo(HttpServletRequest request) {
        try {
            Optional<CsrfToken> tokenOpt = extractCsrfToken(request);

            if (tokenOpt.isEmpty()) {
                log.debug("📭 No se puede crear CsrfTokenInfo: Token no disponible para URI: {}",
                        request.getRequestURI());
                return Optional.empty();
            }

            CsrfToken csrfToken = tokenOpt.get();
            CsrfTokenInfo tokenInfo = createCsrfTokenInfo(csrfToken);

            log.debug("📋 CsrfTokenInfo creado exitosamente para URI: {} - Token Length: {}",
                    request.getRequestURI(),
                    tokenInfo.getToken().length());

            return Optional.of(tokenInfo);

        } catch (Exception e) {
            log.error("💥 Error creando CsrfTokenInfo: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    /**
     * Crea un CsrfTokenInfo a partir de un CsrfToken
     *
     * @param csrfToken el token CSRF de Spring Security
     * @return CsrfTokenInfo con toda la información necesaria
     */
    private CsrfTokenInfo createCsrfTokenInfo(CsrfToken csrfToken) {
        return new CsrfTokenInfo(
                csrfToken.getToken(),
                csrfToken.getHeaderName(),
                csrfToken.getParameterName(),
                System.currentTimeMillis(),
                DEFAULT_EXPIRATION_SECONDS
        );
    }
}
