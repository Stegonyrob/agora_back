package de.stella.agora_web.admin.service;

import java.util.List;

import de.stella.agora_web.admin.controller.dto.AdminUserDTO;

public interface IAdminService {

    List<AdminUserDTO> getAllUsers();

    AdminUserDTO createAdmin(AdminUserDTO adminUserDTO);

    void deleteAdmin(Long id);
}
