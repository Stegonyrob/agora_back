package de.stella.agora_web.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.jwt.TokenDTO;
import de.stella.agora_web.jwt.TokenGenerator;

@RestController
@RequestMapping("${api-endpoint}")
public class AuthController {
    @Autowired
    TokenGenerator tokenGenerator;
    @Autowired
    DaoAuthenticationProvider daoAuthenticationProvider;
    @Autowired
    @Qualifier("jwtRefreshTokenAuthProvider")
    JwtAuthenticationProvider refreshTokenAuthProvider;

    @PostMapping("/all/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO loginDTO) {
        Authentication authentication = daoAuthenticationProvider.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(loginDTO.getUsername(), loginDTO.getPassword()));
        System.out.println(authentication);

        return ResponseEntity.ok(tokenGenerator.createToken(authentication));
    }

    @PostMapping("/all/token")
    public ResponseEntity<TokenDTO> token(@RequestBody RefreshTokenDTO tokenDTO) {
        System.out.println("Token recibido: " + tokenDTO.getRefreshToken());
        Authentication authentication = refreshTokenAuthProvider
                .authenticate(new BearerTokenAuthenticationToken(tokenDTO.getRefreshToken()));

        return ResponseEntity.ok(tokenGenerator.createToken(authentication));
    }
}

// import com.auth0.jwt.JWT;
// import com.auth0.jwt.algorithms.Algorithm;
// import com.auth0.jwt.interfaces.DecodedJWT;

// // ...

// public void handleRequest(HttpServletRequest request, HttpServletResponse
// response) {
// // Obtener el token desde el encabezado Authorization
// String token = request.getHeader("Authorization");

// // Eliminar la palabra "Bearer" del token si está presente
// if (token != null && token.startsWith("Bearer ")) {
// token = token.substring(7);
// }

// try {
// // Decodificar el token JWT
// DecodedJWT decodedToken = JWT.decode(token);

// // Obtener los datos del token
// String issuer = decodedToken.getIssuer();
// String subject = decodedToken.getSubject();
// String audience = decodedToken.getAudience().get(0);
// Date expiresAt = decodedToken.getExpiresAt();

// // Realizar las validaciones necesarias (por ejemplo, verificar la firma, el
// emisor, el asunto, etc.)

// // ...

// // Realizar la lógica de tu aplicación

// // ...

// } catch (Exception e) {
// // Manejar el error en caso de que el token sea inválido
// // ...
// }
// }