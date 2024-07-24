package de.stella.agora_web.security;

import jakarta.servlet.ServletException;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  public void handle(
    HttpServletRequest request,
    HttpServletResponse response,
    AccessDeniedException accessDeniedException
  ) throws IOException {
    // Registrar información sobre el acceso denegado
    System.out.println("Acceso denegado a la URL: " + request.getRequestURI());
    System.out.println("Usuario: " + request.getUserPrincipal());
    System.out.println("Error: " + accessDeniedException.getMessage());

    // Enviar respuesta 403
    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
  }

  @Override
  public void handle(
    jakarta.servlet.http.HttpServletRequest request,
    jakarta.servlet.http.HttpServletResponse response,
    AccessDeniedException accessDeniedException
  ) throws IOException, ServletException {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
