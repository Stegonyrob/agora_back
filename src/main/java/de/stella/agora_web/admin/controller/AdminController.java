package de.stella.agora_web.admin.controller;

import java.util.List;

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

import de.stella.agora_web.admin.controller.dto.AdminCreateDTO;
import de.stella.agora_web.admin.controller.dto.AdminUserDTO;
import de.stella.agora_web.admin.service.impl.AdminServiceImpl;
import jakarta.validation.Valid;

@RestController
@RequestMapping("${api-endpoint}/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminServiceImpl adminService;

    public AdminController(AdminServiceImpl adminService) {
        this.adminService = adminService;
    }

    // ============= GESTIÓN DE USUARIOS ADMIN =============
    @PostMapping("/create")
    public ResponseEntity<AdminUserDTO> createAdmin(@RequestBody @Valid AdminCreateDTO dto) {
        AdminUserDTO created = adminService.createAndPromoteAdmin(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<AdminUserDTO>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminUserDTO> getAdminById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getAdminById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminUserDTO> updateAdmin(@PathVariable Long id, @RequestBody @Valid AdminCreateDTO dto) {
        AdminUserDTO updated = adminService.updateAdmin(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/demote/{id}")
    public ResponseEntity<Void> demoteAdminToUser(@PathVariable Long id) {
        adminService.demoteAdminToUser(id);
        return ResponseEntity.ok().build();
    }

    // ============= AUTENTICACIÓN 2FA =============
    @GetMapping("/{id}/2fa-secret")
    public ResponseEntity<String> getAdminTotpSecret(@PathVariable Long id) {
        String secret = adminService.getAdminTotpSecret(id);
        return ResponseEntity.ok(secret);
    }

    @PostMapping("/{id}/2fa-validate")
    public ResponseEntity<Boolean> validateAdminTotp(@PathVariable Long id, @RequestBody String code) {
        boolean valid = adminService.validateAdminTotp(id, code);
        return ResponseEntity.ok(valid);
    }
}
