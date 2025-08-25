package de.stella.agora_web.admin.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import de.stella.agora_web.admin.controller.dto.AdminCreateDTO;
import de.stella.agora_web.admin.controller.dto.AdminUserDTO;
import de.stella.agora_web.admin.service.IAdminService;
import de.stella.agora_web.profiles.controller.ProfileController;
import de.stella.agora_web.profiles.controller.dto.ProfileDTO;
import de.stella.agora_web.profiles.model.Profile;
import de.stella.agora_web.profiles.repository.ProfileRepository;
import de.stella.agora_web.roles.model.Role;
import de.stella.agora_web.roles.repository.RoleRepository;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.service.ITotpService;
import de.stella.agora_web.user.service.IUserService;

@Service
public class AdminServiceImpl implements IAdminService {

    private final IUserService userService;
    private final RoleRepository roleRepository;
    private final ProfileRepository profileRepository;
    private final ITotpService totpService;

    public AdminServiceImpl(IUserService userService, RoleRepository roleRepository, ProfileRepository profileRepository, ITotpService totpService) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.profileRepository = profileRepository;
        this.totpService = totpService;
    }

    @Override
    public List<AdminUserDTO> getAllUsers() { // Renombrado a getAllAdmins para claridad
        return userService.getAll().stream()
                .filter(User::isAdmin)
                .map(this::toAdminUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AdminUserDTO createAdmin(AdminUserDTO adminUserDTO) {
        Long userId = adminUserDTO.getProfile() != null ? adminUserDTO.getProfile().getUserId() : null;
        if (userId == null) {
            throw new IllegalArgumentException("El AdminUserDTO debe contener un profile con userId");
        }
        User user = userService.findById(userId).orElseThrow();
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("Role not found: ROLE_ADMIN"));
        if (user.getRoles().stream().noneMatch(role -> "ROLE_ADMIN".equals(role.getName()))) {
            user.getRoles().add(adminRole);
        }
        userService.save(user);
        return toAdminUserDTO(user);
    }

    @Override
    public void deleteAdmin(Long id) {
        List<User> admins = userService.getAll().stream()
                .filter(User::isAdmin)
                .collect(Collectors.toList());
        if (admins.size() <= 1) {
            throw new IllegalStateException("Debe haber al menos un administrador.");
        }
        User user = userService.findById(id).orElseThrow();
        user.getRoles().removeIf(role -> "ROLE_ADMIN".equals(role.getName()));
        userService.save(user);
    }

    // Métodos que ahora usan los servicios inyectados
    public AdminUserDTO createAndPromoteAdmin(AdminCreateDTO dto) {
        // Crear usuario manualmente ya que createAndSaveNewUser no existe
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        // Usar save en lugar de createAndSaveNewUser
        user = userService.save(user);

        // El único trabajo aquí es la lógica de "promoción"
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("Role not found: ROLE_ADMIN"));
        user.getRoles().add(adminRole);
        userService.save(user);

        return toAdminUserDTO(user);
    }

    public String getAdminTotpSecret(Long id) {
        User user = userService.findById(id).orElseThrow();
        if (!user.isAdmin()) {
            throw new IllegalArgumentException("El usuario no es admin");
        }
        // Se delega la lógica de seguridad al servicio TOTP
        return totpService.generateTotpSecret();
    }

    public boolean validateAdminTotp(Long id, String code) {
        User user = userService.findById(id).orElseThrow();
        if (!user.isAdmin()) {
            throw new IllegalArgumentException("El usuario no es admin");
        }
        String secret = user.getTotpSecret();
        if (secret == null || secret.isEmpty()) {
            return false;
        }
        // La validación se delega al servicio TOTP
        return totpService.validateTotpCode(secret, code);
    }

    // El resto de métodos públicos de administración que tenías
    // ...
    private AdminUserDTO toAdminUserDTO(User user) {
        Profile profile = user.getProfile();
        ProfileDTO profileDTO = null;
        if (profile != null) {
            profileDTO = ProfileController.toProfileDTO(profile);
        }
        boolean isAdmin = user.isAdmin();
        boolean isActive = true;
        return new AdminUserDTO(profileDTO, isAdmin, isActive);
    }

    public AdminUserDTO updateAdmin(Long id, AdminCreateDTO dto) {
        User user = userService.findById(id).orElseThrow();
        if (dto.getUsername() != null && !dto.getUsername().isBlank()) {
            user.setUsername(dto.getUsername());
        }
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getPhone() != null && !dto.getPhone().isBlank()) {
            user.setPhone(dto.getPhone());
        }
        userService.save(user);
        return toAdminUserDTO(user);
    }

    public List<AdminUserDTO> getAllAdmins() {
        return userService.getAll().stream()
                .filter(User::isAdmin)
                .map(this::toAdminUserDTO)
                .collect(Collectors.toList());
    }

    public AdminUserDTO getAdminById(Long id) {
        User user = userService.findById(id).orElseThrow();
        if (!user.isAdmin()) {
            throw new IllegalArgumentException("El usuario no es administrador");
        }
        return toAdminUserDTO(user);
    }
}
