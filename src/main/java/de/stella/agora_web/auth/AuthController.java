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
  
    @PostMapping("/login") 
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO loginDTO) { 
        Authentication authentication = daoAuthenticationProvider.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(loginDTO.getUsername(), loginDTO.getPassword())); 
        System.out.println(authentication);
  
        return ResponseEntity.ok(tokenGenerator.createToken(authentication)); 
    } 
  
    @PostMapping("/token") 
    public ResponseEntity<TokenDTO> token(@RequestBody TokenDTO tokenDTO) { 
        Authentication authentication = refreshTokenAuthProvider.authenticate(new BearerTokenAuthenticationToken(tokenDTO.getRefreshToken())); 
        // Jwt jwt = (Jwt) authentication.getCredentials(); 
        // check if present in db and not revoked, etc 
  
        return ResponseEntity.ok(tokenGenerator.createToken(authentication)); 
    } 
} 

// @GetMapping(path = "/login")
// public ResponseEntity<Map<String, String>> login() {
//     SecurityContext context = SecurityContextHolder.getContext();
    
//     Authentication auth = context.getAuthentication();
    
//     String username = auth.getName();
//     String role = "";
//     if (auth.getAuthorities() != null && !auth.getAuthorities().isEmpty()) {
//         role = auth.getAuthorities().iterator().next().getAuthority();
//     }
    
//     // Assuming you can retrieve the user object from the username
//     Optional<User> optionalUser = userService.findByUsername(username);
//     if (optionalUser.isEmpty()) {
//         throw new UsernameNotFoundException("User not found");
//     }
//     User user = optionalUser.get();
    
//     Map<String, String> json = new HashMap<>();
//     json.put("message", "Logged");
//     json.put("username", username);
//     json.put("role", role);
//     // Assuming the UserDetails object has a method to get the user ID
//     json.put("userId", user.getId().toString());
    
//     return ResponseEntity.status(HttpStatus.ACCEPTED).body(json);
// }