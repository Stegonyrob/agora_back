package de.stella.agora_web.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.auth.controller.dto.SocialLoginRequestDTO;
import de.stella.agora_web.auth.service.SocialAuthService;
import de.stella.agora_web.jwt.TokenDTO;

@RestController
@RequestMapping("${api-endpoint}/all/auth")
public class SocialAuthController {

    @Autowired
    private SocialAuthService socialAuthService;

    @PostMapping("/login/google")
    public ResponseEntity<TokenDTO> googleLogin(@RequestBody SocialLoginRequestDTO request) {
        try {
            TokenDTO tokenDTO = socialAuthService.authenticateGoogleUser(request.getIdToken());
            return ResponseEntity.ok(tokenDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login/facebook")
    public ResponseEntity<TokenDTO> facebookLogin(@RequestBody SocialLoginRequestDTO request) {
        try {
            TokenDTO tokenDTO = socialAuthService.authenticateFacebookUser(request.getAccessToken());
            return ResponseEntity.ok(tokenDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
