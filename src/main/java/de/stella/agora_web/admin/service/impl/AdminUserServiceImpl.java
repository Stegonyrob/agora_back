package de.stella.agora_web.admin.service.impl;

import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import de.stella.agora_web.admin.controller.dto.AdminCreateDTO;
import de.stella.agora_web.admin.controller.dto.AdminUserDTO;
import de.stella.agora_web.admin.service.IAdminUserService;

/**
 * 🔧 Implementación stub del servicio de gestión de usuarios administradores
 *
 * Implementación temporal simplificada para resolver dependencias de Spring Boot.
 * Esta implementación permitirá que la aplicación arranque correctamente.
 */
@Service
public class AdminUserServiceImpl implements IAdminUserService {

    @Override
    public List<AdminUserDTO> findAllAdminUsers() {
        // Implementación temporal stub
        return new ArrayList<>();
    }

    @Override
    public AdminUserDTO findAdminUserById(Long userId) {
        // Implementación temporal stub
        return null;
    }

    @Override
    public AdminUserDTO findAdminUserByEmail(String email) {
        // Implementación temporal stub
        return null;
    }

    @Override
    public AdminUserDTO createAdminUser(AdminCreateDTO adminCreateDTO) {
        // Implementación temporal stub
        AdminUserDTO result = new AdminUserDTO();
        result.setId(1L);
        result.setUsername(adminCreateDTO.getUsername());
        result.setEmail(adminCreateDTO.getEmail());
        result.setPhone(adminCreateDTO.getPhone());
        result.setFirstName(adminCreateDTO.getFirstName());
        result.setLastName(adminCreateDTO.getLastName());
        result.setAdmin(true);
        result.setActive(true);
        return result;
    }

    @Override
    public AdminUserDTO updateAdminUser(Long userId, AdminCreateDTO adminCreateDTO) {
        // Implementación temporal stub
        AdminUserDTO result = new AdminUserDTO();
        result.setId(userId);
        result.setUsername(adminCreateDTO.getUsername());
        result.setEmail(adminCreateDTO.getEmail());
        result.setPhone(adminCreateDTO.getPhone());
        result.setFirstName(adminCreateDTO.getFirstName());
        result.setLastName(adminCreateDTO.getLastName());
        result.setAdmin(true);
        result.setActive(true);
        return result;
    }

    @Override
    public void deleteAdminUser(Long userId) {
        // Implementación temporal stub - no hace nada
    }

    @Override
    public boolean isUserAdmin(Long userId) {
        // Implementación temporal stub
        return false;
    }

    @Override
    public boolean existsAdminByEmail(String email) {
        // Implementación temporal stub
        return false;
    }
}