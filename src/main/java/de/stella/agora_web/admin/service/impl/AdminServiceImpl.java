package de.stella.agora_web.admin.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import de.stella.agora_web.admin.controller.dto.AdminCreateDTO;
import de.stella.agora_web.admin.controller.dto.AdminUserDTO;
import de.stella.agora_web.roles.model.Role;
import de.stella.agora_web.roles.repository.RoleRepository;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl {

    private final UserServiceImpl userService;
    private final RoleRepository roleRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    /**
     * Devuelve todos los usuarios con rol ADMIN.
     */
    public List<AdminUserDTO> getAllAdmins() {
        return userService.getAllUsers().stream()
                .filter(User::isAdmin)
                .map(this::toAdminUserDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convierte un usuario existente en administrador (añade el rol ADMIN si no
     * lo tiene).
     */
    public AdminUserDTO createAdmin(AdminUserDTO dto) {
        User user = userService.findById(dto.getId()).orElseThrow();
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("Role not found: ROLE_ADMIN"));
        if (user.getRoles().stream().noneMatch(role -> "ROLE_ADMIN".equals(role.getName()))) {
            user.getRoles().add(adminRole);
        }
        userService.save(user);
        return toAdminUserDTO(user);
    }

    /**
     * Elimina el rol ADMIN de un usuario, asegurando que siempre haya al menos
     * dos administradores.
     */
    public void deleteAdmin(Long id) {
        List<User> admins = userService.getAllUsers().stream()
                .filter(User::isAdmin)
                .collect(Collectors.toList());
        if (admins.size() <= 2) {
            throw new IllegalStateException("Debe haber al menos dos administradores.");
        }
        User user = userService.findById(id).orElseThrow();
        user.getRoles().removeIf(role -> "ROLE_ADMIN".equals(role.getName()));
        userService.save(user);
    }

    /**
     * Convierte un User a AdminUserDTO.
     */
    private AdminUserDTO toAdminUserDTO(User user) {
        AdminUserDTO dto = new AdminUserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setAdmin(user.isAdmin());
        return dto;
    }

    /**
     * Devuelve un administrador por ID, o lanza excepción si no es admin.
     */
    public AdminUserDTO getAdminById(Long id) {
        User user = userService.findById(id).orElseThrow();
        if (!user.isAdmin()) {
            throw new IllegalArgumentException("El usuario no es administrador");
        }
        return toAdminUserDTO(user);
    }

    public AdminUserDTO createAndPromoteAdmin(AdminCreateDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setTotpSecret(generateTotpSecret());

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("Role not found: ROLE_ADMIN"));
        if (user.getRoles() == null) {
            user.setRoles(new java.util.HashSet<>());
        }
        user.getRoles().add(adminRole);

        userService.save(user);

        return toAdminUserDTO(user);
    }

    /**
     * Genera una clave secreta TOTP (base32) para 2FA.
     */
    String generateTotpSecret() {
        // Genera 20 bytes aleatorios y codifica en base32 (sin dependencias externas)
        java.security.SecureRandom random = new java.security.SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        return java.util.Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Devuelve el secreto TOTP de un admin (para mostrar QR en frontend).
     */
    public String getAdminTotpSecret(Long id) {
        User user = userService.findById(id).orElseThrow();
        if (!user.isAdmin()) {
            throw new IllegalArgumentException("El usuario no es admin");
        }
        return user.getTotpSecret();
    }

    /**
     * Valida un código TOTP para el admin dado.
     */
    public boolean validateAdminTotp(Long id, String code) {
        User user = userService.findById(id).orElseThrow();
        if (!user.isAdmin()) {
            throw new IllegalArgumentException("El usuario no es admin");
        }
        String secret = user.getTotpSecret();
        if (secret == null || secret.isEmpty()) {
            return false;
        }
        return validateTotpCode(secret, code);
    }

    /**
     * Lógica de validación TOTP (RFC 6238, 30s window, 6 dígitos). Usa solo
     * dependencias estándar de Java.
     */
    private boolean validateTotpCode(String base64Secret, String code) {
        try {
            byte[] key = java.util.Base64.getDecoder().decode(base64Secret);
            long timeIndex = System.currentTimeMillis() / 1000 / 30;
            for (int i = -1; i <= 1; i++) { // tolerancia de 1 ventana
                String candidate = generateTotp(key, timeIndex + i);
                if (candidate.equals(code)) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    // Implementación TOTP simple (SHA1, 6 dígitos)
    private String generateTotp(byte[] key, long timeIndex) throws Exception {
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA1");
        javax.crypto.spec.SecretKeySpec signKey = new javax.crypto.spec.SecretKeySpec(key, "HmacSHA1");
        mac.init(signKey);
        byte[] data = new byte[8];
        for (int i = 7; i >= 0; i--) {
            data[i] = (byte) (timeIndex & 0xFF);
            timeIndex >>= 8;
        }
        byte[] hash = mac.doFinal(data);
        int offset = hash[hash.length - 1] & 0xF;
        int binary = ((hash[offset] & 0x7F) << 24)
                | ((hash[offset + 1] & 0xFF) << 16)
                | ((hash[offset + 2] & 0xFF) << 8)
                | (hash[offset + 3] & 0xFF);
        int otp = binary % 1000000;
        return String.format("%06d", otp);
    }
}
