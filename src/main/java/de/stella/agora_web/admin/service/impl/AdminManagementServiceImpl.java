package de.stella.agora_web.admin.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.stella.agora_web.admin.controller.dto.AdminCreateDTO;
import de.stella.agora_web.admin.controller.dto.AdminUserDTO;
import de.stella.agora_web.admin.service.IAdminUserService;

/**
 * 🎯 Implementación Stub del Servicio de Gestión de Administradores
 *
 * Implementación temporal simplificada para resolver dependencias. Solo utiliza
 * AdminUserService por ahora.
 */
@Service
@Transactional
public class AdminManagementServiceImpl {

    private final IAdminUserService adminUserService;

    public AdminManagementServiceImpl(IAdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    public List<AdminUserDTO> getAllAdmins() {
        try {
            return adminUserService.findAllAdminUsers();
        } catch (Exception e) {
            // Fallback temporal
            return new ArrayList<>();
        }
    }

    public AdminUserDTO getAdminById(Long id) {
        try {
            return adminUserService.findAdminUserById(id);
        } catch (Exception e) {
            // Fallback temporal
            return null;
        }
    }

    public AdminUserDTO createAdmin(AdminCreateDTO adminCreateDTO) {
        try {
            return adminUserService.createAdminUser(adminCreateDTO);
        } catch (Exception e) {
            // Fallback temporal
            throw new RuntimeException("Error creating admin: " + e.getMessage(), e);
        }
    }

    public AdminUserDTO updateAdmin(Long id, AdminCreateDTO adminCreateDTO) {
        try {
            return adminUserService.updateAdminUser(id, adminCreateDTO);
        } catch (Exception e) {
            // Fallback temporal
            throw new RuntimeException("Error updating admin: " + e.getMessage(), e);
        }
    }

    public void deleteAdmin(Long id) {
        try {
            adminUserService.deleteAdminUser(id);
        } catch (Exception e) {
            // Fallback temporal
            throw new RuntimeException("Error deleting admin: " + e.getMessage(), e);
        }
    }
}
