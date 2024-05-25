package de.stella.agora_web.jwt;




import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class JwtAuthenticationFilter extends GenericFilterBean {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        // Autenticar al usuario y generar token JWT
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
        if (userDetails != null && userDetails.getPassword().equals(password)) {
            String token = jwtTokenUtil.generateToken(username);
            // Agregar token JWT a la respuesta
            HttpServletResponse response = (HttpServletResponse) res;
            response.addHeader("Authorization", "Bearer " + token);
        }
        chain.doFilter(req, res);
    }
}