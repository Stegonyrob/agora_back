package de.stella.agora_web.auth;

import de.stella.agora_web.jwt.TokenDTO;
import de.stella.agora_web.jwt.TokenGenerator;
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

@RestController
@RequestMapping("${api-endpoint}/all")
public class AuthController {

  @Autowired
  TokenGenerator tokenGenerator;

  @Autowired
  DaoAuthenticationProvider daoAuthenticationProvider;

  @Autowired
  @Qualifier("jwtRefreshTokenAuthProvider")
  JwtAuthenticationProvider refreshTokenAuthProvider;

  @PostMapping("/login")
  public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO loginDTO) {
    System.out.println("Received login data: " + loginDTO);
    Authentication authentication = daoAuthenticationProvider.authenticate(
      UsernamePasswordAuthenticationToken.unauthenticated(
        loginDTO.getUsername(),
        loginDTO.getPassword()
      )
    );
    System.out.println(authentication);

    return ResponseEntity.ok(tokenGenerator.createToken(authentication));
  }

  @PostMapping("/token")
  public ResponseEntity<TokenDTO> token(@RequestBody TokenDTO tokenDTO) {
    Authentication authentication = refreshTokenAuthProvider.authenticate(
      new BearerTokenAuthenticationToken(tokenDTO.getRefreshToken())
    );
    // Jwt jwt = (Jwt) authentication.getCredentials();
    // check if present in db and not revoked, etc

    return ResponseEntity.ok(tokenGenerator.createToken(authentication));
  }
}
