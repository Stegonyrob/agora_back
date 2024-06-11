package de.stella.agora_web.jwt;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import de.stella.agora_web.security.SecurityUser;
  
@Component
public class TokenGenerator { 
      
    // Obtiene el encojedor de tokens de acceso
    @Autowired
    JwtEncoder accessTokenEncoder; 
  
    // Obtiene el encojedor de tokens de refresco
    @Autowired
    @Qualifier("jwtRefreshTokenEncoder") 
    JwtEncoder refreshTokenEncoder; 
      
    /**
     * Crea un token de acceso.
     * 
     * @param authentication La autenticación del usuario
     * @return El token de acceso
     */
    private String createAccessToken(Authentication authentication) { 
        // Obtiene el usuario autenticado
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        System.out.println(securityUser);
        // Obtiene la fecha y hora actual
        Instant now = Instant.now(); 
  
        // Crea los claims del token
         JwtClaimsSet claimsSet = JwtClaimsSet.builder() 
                .issuer("myApp") 
                .issuedAt(now) 
                .expiresAt(now.plus(30, ChronoUnit.DAYS)) 
                .subject(Long.toString(securityUser.getId())) 
                .build(); 
        // Codifica el token y lo devuelve
        return accessTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue(); 
    } 
      
    /**
     * Crea un token de refresco.
     * 
     * @param authentication La autenticación del usuario
     * @return El token de refresco
     */
    private String createRefreshToken(Authentication authentication) { 
        // Obtiene el usuario autenticado
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Instant now = Instant.now(); 
  
        // Crea los claims del token
        JwtClaimsSet claimsSet = JwtClaimsSet.builder() 
                .issuer("myApp") // Emisor del token
                .issuedAt(now) // Fecha de emisión
                .expiresAt(now.plus(30, ChronoUnit.DAYS)) // Fecha de expiración
                .subject(Long.toString(securityUser.getId())) // Identificador del usuario
                .build(); 
  
        // Codifica el token y lo devuelve
        return refreshTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue(); 
    } 
    /**
     * Crea los tokens de acceso y refresco.
     * 
     * @param authentication La autenticación del usuario
     * @return El objeto TokenDTO con los tokens
     */
    public TokenDTO createToken(Authentication authentication) { 
        // Obtiene el usuario autenticado
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        System.out.println(authentication.getPrincipal());
  
        // Crea el objeto TokenDTO y lo llena con el ID del usuario y el token de acceso
        TokenDTO tokenDTO = new TokenDTO(); 
        tokenDTO.setUserId(securityUser.getId()); 
        tokenDTO.setAccessToken(createAccessToken(authentication)); 
  
        // Obtiene el token de refresco
        String refreshToken; 
        if (authentication.getCredentials() instanceof Jwt jwt) { 
            // Obtiene la fecha y hora actual
            Instant now = Instant.now(); 
            // Obtiene la fecha de expiración del token de acceso
            Instant expiresAt = jwt.getExpiresAt(); 
            // Calcula la diferencia en días entre la fecha actual y la fecha de expiración
            Duration duration = Duration.between(now, expiresAt); 
            long daysUntilExpired = duration.toDays(); 
            // Si el token de acceso no expira en menos de 7 días, se crea uno nuevo de refresco
            if (daysUntilExpired < 7) { 
                refreshToken = createRefreshToken(authentication); 
            } else { 
                // Si el token de acceso ya expira en más de 7 días, se utiliza el token existente de refresco
                refreshToken = jwt.getTokenValue(); 
            } 
        } else { 
            // Si no se ha recibido un token de acceso, se crea uno nuevo de refresco
            refreshToken = createRefreshToken(authentication); 
        } 
        // Coloca el token de refresco en el objeto TokenDTO
        tokenDTO.setRefreshToken(refreshToken); 
  
        return tokenDTO; 
    }

    public String createAccessToken(String username) {
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("myApp")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(30, ChronoUnit.DAYS))
                .subject(username)
                .build();
        return accessTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }
 
    
  
} 
