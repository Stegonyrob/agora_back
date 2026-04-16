package de.stella.agora_web.admin.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import de.stella.agora_web.admin.controller.dto.AdminCreateDTO;
import de.stella.agora_web.admin.controller.dto.AdminUserDTO;
import de.stella.agora_web.admin.service.IAdminService;
import de.stella.agora_web.avatar.module.Avatar;
import de.stella.agora_web.avatar.repository.AvatarRepository;
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

    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    private final IUserService userService;
    private final RoleRepository roleRepository;
    private final ProfileRepository profileRepository;
    private final ITotpService totpService;
    private final AvatarRepository avatarRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImpl(IUserService userService,
            RoleRepository roleRepository,
            ProfileRepository profileRepository,
            ITotpService totpService,
            AvatarRepository avatarRepository,
            PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.profileRepository = profileRepository;
        this.totpService = totpService;
        this.avatarRepository = avatarRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<AdminUserDTO> getAllUsers() { // Renombrado a getAllAdmins para claridad
        logger.debug("Obteniendo todos los usuarios administradores");
        return userService.getAll().stream()
                .filter(User::isAdmin)
                .map(this::toAdminUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AdminUserDTO createAdmin(AdminUserDTO adminUserDTO) {
        Long userId = adminUserDTO.getProfile() != null ? adminUserDTO.getProfile().getUserId() : null;
        if (userId == null) {
            logger.error("El AdminUserDTO debe contener un profile con userId");
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
        // Verificar que no sea el último admin
        List<User> admins = userService.getAll().stream()
                .filter(User::isAdmin)
                .collect(Collectors.toList());
        if (admins.size() <= 1) {
            logger.error("Intento de eliminar el último administrador");
            throw new IllegalStateException("Debe haber al menos un administrador.");
        }

        User user = userService.findById(id).orElseThrow();

        // ✅ CORREGIDO: Eliminar completamente el usuario y su perfil
        // Primero eliminar el perfil si existe
        Profile profile = user.getProfile();
        if (profile != null) {
            profileRepository.delete(profile);
        }

        // Luego eliminar el usuario (esto también eliminará las relaciones de roles)
        userService.deleteById(id);
    }

    /**
     * Método específico para degradar admin a usuario común (mantiene la
     * cuenta)
     */
    public void demoteAdminToUser(Long id) {
        // Verificar que no sea el último admin
        List<User> admins = userService.getAll().stream()
                .filter(User::isAdmin)
                .collect(Collectors.toList());
        if (admins.size() <= 1) {
            logger.error("Intento de degradar el último administrador");
            throw new IllegalStateException("Debe haber al menos un administrador.");
        }

        User user = userService.findById(id).orElseThrow();
        // Solo quitar el rol ADMIN (mantener usuario)
        user.getRoles().removeIf(role -> "ROLE_ADMIN".equals(role.getName()));
        userService.save(user);
    }

    // Métodos que ahora usan los servicios inyectados
    public AdminUserDTO createAndPromoteAdmin(AdminCreateDTO dto) {
        // Crear usuario completo con todos los campos necesarios
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        // ✅ CORREGIDO: Codificar la password con BCrypt
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setAcceptedRules(true); // Por defecto para admins

        // Usar save en lugar de createAndSaveNewUser
        user = userService.save(user);

        // Crear perfil asociado al usuario
        Profile profile = new Profile();
        // ✅ CRÍTICO: Asignar el mismo ID del usuario al perfil
        profile.setId(user.getId());
        profile.setFirstName(dto.getFirstName());
        profile.setLastName1(dto.getLastName1());
        profile.setLastName2(dto.getLastName2());
        profile.setEmail(dto.getEmail());
        profile.setPhone(dto.getPhone());
        profile.setCity(dto.getCity());
        profile.setCountry(dto.getCountry());
        profile.setRelationship(dto.getRelationship());
        profile.setUsername(dto.getUsername());
        // ✅ CORREGIDO: Codificar password en profile también
        profile.setPassword(passwordEncoder.encode(dto.getPassword()));
        profile.setConfirmPassword(passwordEncoder.encode(dto.getConfirmPassword()));

        if (dto.getAvatarId() != null) {
            Avatar avatar = avatarRepository.findById(dto.getAvatarId()).orElse(null);
            profile.setAvatar(avatar);
        }
        profile.setUser(user);
        profileRepository.save(profile);

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
            logger.error("El usuario con id {} no es admin", id);
            throw new IllegalArgumentException("El usuario no es admin");
        }
        // Se delega la lógica de seguridad al servicio TOTP
        return totpService.generateTotpSecret();
    }

    public boolean validateAdminTotp(Long id, String code) {
        User user = userService.findById(id).orElseThrow();
        if (!user.isAdmin()) {
            logger.error("El usuario con id {} no es admin", id);
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
            // Evitar recursión en serialización: ProfileDTO no debe contener referencias circulares
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
        logger.debug("Obteniendo todos los administradores");
        return userService.getAll().stream()
                .filter(User::isAdmin)
                .map(this::toAdminUserDTO)
                .collect(Collectors.toList());
    }

    public AdminUserDTO getAdminById(Long id) {
        User user = userService.findById(id).orElseThrow();
        if (!user.isAdmin()) {
            logger.error("El usuario con id {} no es administrador", id);
            throw new IllegalArgumentException("El usuario no es administrador");
        }
        return toAdminUserDTO(user);
    }
}
