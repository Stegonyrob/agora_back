package de.stella.agora_web.admin.service;

import java.util.List;

import de.stella.agora_web.admin.controller.dto.AdminUserDTO;

public interface IAdminService {

    List<AdminUserDTO> getAllUsers(); // Podríamos llamarlo getAllAdmins() para mayor claridad

    AdminUserDTO createAdmin(AdminUserDTO adminUserDTO); // Promueve a un usuario existente

    void deleteAdmin(Long id);
}
