package de.stella.agora_web.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.auth.PasswordRecoveryService;
import de.stella.agora_web.auth.dto.PasswordRecoveryRequestDTO;
import de.stella.agora_web.auth.dto.PasswordResetDTO;

@RestController
@RequestMapping("${api-endpoint}/admin/password")
public class AdminPasswordRecoveryController {

    private final PasswordRecoveryService passwordRecoveryService;

    public AdminPasswordRecoveryController(PasswordRecoveryService passwordRecoveryService) {
        this.passwordRecoveryService = passwordRecoveryService;
    }

    @PostMapping("/recovery-request")
    public ResponseEntity<Void> requestRecovery(@RequestBody PasswordRecoveryRequestDTO request) {
        passwordRecoveryService.sendRecoveryEmail(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> resetPassword(@RequestBody PasswordResetDTO request) {
        passwordRecoveryService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok().build();
    }
}
