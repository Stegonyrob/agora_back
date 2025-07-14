package de.stella.agora_web.admin.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import de.stella.agora_web.admin.controller.dto.AdminUserDTO;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.service.impl.UserServiceImpl;

@Service
public class AdminServiceImpl {

    private final UserServiceImpl userService;

    public AdminServiceImpl(UserServiceImpl userService) {
        this.userService = userService;
    }

    public List<AdminUserDTO> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(this::toAdminUserDTO)
                .collect(Collectors.toList());
    }

    public AdminUserDTO createAdmin(AdminUserDTO dto) {
        // Implementar lógica de creación de admin
        // ...
        return dto;
    }

    public void deleteAdmin(Long id) {
        // Implementar lógica de borrado seguro de admin (mínimo 2 admins)
        // ...
    }

    private AdminUserDTO toAdminUserDTO(User user) {
        AdminUserDTO dto = new AdminUserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setAdmin(user.isAdmin());
        return dto;
    }
}
