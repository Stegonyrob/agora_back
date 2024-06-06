package de.stella.agora_web.auth;


import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import de.stella.agora_web.security.SecurityUser;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.repository.UserRepository;

  
  
  
@Component
public class JWTtoUserConverter implements Converter<Jwt, UsernamePasswordAuthenticationToken> { 

    // Inyección de la repositorio de usuario
    UserRepository userRepository;
  
    public JWTtoUserConverter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Convierte el JWT en un objeto de autenticación de usuario.
     * 
     * @param source El objeto JWT
     * @return El objeto de autenticación de usuario
     */
    @Override
    public UsernamePasswordAuthenticationToken convert(Jwt source) { 

        // Busca el usuario utilizando el id del JWT
        User user = userRepository.findById(source.getSubject()).orElseThrow();

        // Crea el objeto de usuario de seguridad y lo completa con los datos del usuario
        SecurityUser securityUser = new SecurityUser(user); 
        user.setId(Long.parseLong(source.getSubject())); 
        
        // Crea el objeto de autenticación de usuario con los datos del usuario y sus privilegios
        return new UsernamePasswordAuthenticationToken(securityUser, securityUser.getPassword(), securityUser.getAuthorities()); 
    } 
      
  
} 
