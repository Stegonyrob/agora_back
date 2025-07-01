package de.stella.agora_web.banned.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.banned.controller.dto.BannedUserDTO;
import de.stella.agora_web.banned.model.Banned;
import de.stella.agora_web.banned.repository.BannedRepository;
import de.stella.agora_web.banned.service.IBannedService;

@RestController
@RequestMapping("${api-endpoint}/admin/banned")
public class BannedController {

    @Autowired
    private BannedRepository bannedRepository;

    @Autowired
    private IBannedService bannedService;

    @GetMapping("/{userId}")
    public BannedUserDTO getBannedUser(@PathVariable Long userId) {
        Banned banned = bannedService.findByUserId(userId);
        BannedUserDTO dto = new BannedUserDTO();
        dto.setUserId(userId);
        dto.setIsBanned(banned != null);
        return dto;
    }

    // Métodos para gestionar los baneos de usuarios
    @PostMapping
    public ResponseEntity<Banned> createBanned(@RequestBody Banned banned) {
        Banned newBanned = bannedRepository.save(banned);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBanned);
    }

    @GetMapping
    public ResponseEntity<List<Banned>> getAllBanned() {
        List<Banned> bannedList = bannedRepository.findAll();
        return ResponseEntity.ok(bannedList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Banned> getBannedById(@PathVariable Long id) {
        Banned banned = bannedRepository.findById(id).orElse(null);
        return ResponseEntity.ok(banned);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Banned> updateBanned(@PathVariable Long id, @RequestBody Banned banned) {
        Banned existingBanned = bannedRepository.findById(id).orElse(null);
        if (existingBanned != null) {
            existingBanned.setBanReason(banned.getBanReason());
            existingBanned.setBanDate(banned.getBanDate());
            Banned updatedBanned = bannedRepository.save(existingBanned);
            return ResponseEntity.ok(updatedBanned);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBanned(@PathVariable Long id) {
        bannedRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ============= ENDPOINTS PARA BANEAR/DESBANEAR POR USER ID =============
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/user/{userId}")
    public ResponseEntity<String> banUser(@PathVariable Long userId, @RequestBody BanRequestDTO banRequest) {
        try {
            String reason = banRequest.getReason();
            if (reason == null || reason.trim().isEmpty()) {
                reason = "Baneado por administrador";
            }

            bannedService.banUser(userId, reason, true, true);
            return ResponseEntity.ok("Usuario con ID " + userId + " baneado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al banear usuario: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<String> unbanUser(@PathVariable Long userId) {
        try {
            bannedService.unbanUser(userId);
            return ResponseEntity.ok("Usuario con ID " + userId + " desbaneado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al desbanear usuario: " + e.getMessage());
        }
    }

    // DTO para la solicitud de baneo
    public static class BanRequestDTO {

        private String reason;

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }
}
