package de.stella.agora_web.jwt;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
                       AuthenticationException authException) throws IOException {
    response.setHeader("Content-Type", "application/json");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    Map<String, String> error = new HashMap<>();
    error.put("message", "Unauthorized");
    response.getWriter().write(new ObjectMapper().writeValueAsString(error));
  }
}