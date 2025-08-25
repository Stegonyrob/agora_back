package de.stella.agora_web.admin.service.impl;

import org.springframework.stereotype.Service;

import de.stella.agora_web.admin.controller.dto.AdminCreateDTO;
import de.stella.agora_web.admin.controller.dto.AdminUserDTO;
import de.stella.agora_web.avatar.repository.AvatarRepository;
import de.stella.agora_web.profiles.controller.ProfileController;
import de.stella.agora_web.profiles.controller.dto.ProfileDTO;
import de.stella.agora_web.profiles.model.Profile;
import de.stella.agora_web.profiles.repository.ProfileRepository;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;

@Service

@RequiredArgsConstructor
public class AdminServiceProfileImpl {

    private final UserServiceImpl userService;
    private final ProfileRepository profileRepository;
    private final AvatarRepository avatarRepository;

    /**
     * Devuelve el perfil completo de un admin por ID como ProfileDTO.
     */
    public ProfileDTO getAdminProfileById(Long id) {
        User user = userService.findById(id).orElseThrow();
        if (!user.isAdmin()) {
            throw new IllegalArgumentException("El usuario no es administrador");
        }
        // Buscar el perfil por userId
        Profile profile = profileRepository.findByUserId(user.getId())
                .stream().findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Perfil no encontrado para el admin"));
        return ProfileController.toProfileDTO(profile);
    }

    /**
     * Actualiza los datos de un admin (username, email, phone).
     */
    public AdminUserDTO updateAdmin(Long id, AdminCreateDTO dto) {
        User user = userService.findById(id).orElseThrow();
        if (!user.isAdmin()) {
            throw new IllegalArgumentException("El usuario no es administrador");
        }
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

    private AdminUserDTO toAdminUserDTO(User user) {
        Profile profile = user.getProfile();
        ProfileDTO profileDTO = null;
        if (profile != null) {
            profileDTO = de.stella.agora_web.profiles.controller.ProfileController.toProfileDTO(profile);
        }
        boolean isAdmin = user.isAdmin();
        boolean isActive = true; // Puedes ajustar la lógica según tu modelo de usuario
        return new AdminUserDTO(profileDTO, isAdmin, isActive);
    }
}
