package de.stella.agora_web.auth;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.security.csrf.dto.CsrfTokenInfo;
import de.stella.agora_web.security.csrf.service.ICsrfTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 🛡️ Controlador para gestión de tokens CSRF
 *
 * Proporciona endpoints para que el frontend obtenga y gestione tokens CSRF de
 * manera segura.
 *
 * Principios SOLID aplicados: - SRP: Una responsabilidad (endpoints CSRF) -
 * DIP: Depende de abstracciones (ICsrfTokenService)
 *
 * Clean Code: - Nombres descriptivos - Logging estructurado - Manejo de errores
 * claro
 */
@Slf4j
@RestController
@RequestMapping("${api-endpoint}/all")
@RequiredArgsConstructor
public class CSRFController {

    private final ICsrfTokenService csrfTokenService;

    /**
     * 🎯 Obtiene el token CSRF actual para el cliente
     *
     * @param request HttpServletRequest para extraer el token
     * @return ResponseEntity con información del token CSRF
     */
    @GetMapping("/csrf-token")
    public ResponseEntity<CsrfTokenInfo> getCsrfToken(HttpServletRequest request) {
        log.debug("🔍 Solicitando token CSRF para URI: {}", request.getRequestURI());

        Optional<CsrfTokenInfo> tokenInfo = csrfTokenService.getCsrfTokenInfo(request);

        if (tokenInfo.isEmpty()) {
            log.warn("🚨 No se pudo obtener token CSRF para URI: {}", request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        }

        log.debug("✅ Token CSRF proporcionado exitosamente");
        return ResponseEntity.ok(tokenInfo.get());
    }
}
