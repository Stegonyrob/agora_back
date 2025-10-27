package de.stella.agora_web.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Custom handler for access denied exceptions.
 * Logs access denied attempts and returns HTTP 403 Forbidden response.
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
        // Registrar información sobre el acceso denegado
        logger.warn("Acceso denegado a la URL: {} | Usuario: {} | Error: {}",
                request.getRequestURI(),
                request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "anonymous",
                accessDeniedException.getMessage());

        // Enviar respuesta 403
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
    }
}
