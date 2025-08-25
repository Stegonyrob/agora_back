package de.stella.agora_web.admin.service;

import java.util.List;

import de.stella.agora_web.admin.controller.dto.AdminCreateDTO;
import de.stella.agora_web.admin.controller.dto.AdminUserDTO;

/**
 * 🔧 Servicio de Gestión de Administradores
 *
 * Responsabilidad única: Coordinar las operaciones de alto nivel relacionadas
 * con la gestión de administradores del sistema.
 *
 * Este servicio orquesta otros servicios especializados pero no implementa
 * lógica de negocio específica.
 */
public interface IAdminManagementService {

    /**
     * Obtiene todos los administradores del sistema
     *
     * @return Lista de administradores
     */
    List<AdminUserDTO> getAllAdmins();

    /**
     * Obtiene un administrador específico por ID
     *
     * @param id ID del administrador
     * @return Administrador encontrado
     */
    AdminUserDTO getAdminById(Long id);

    /**
     * Crea un nuevo administrador completo (usuario + perfil + rol)
     *
     * @param adminCreateDTO Datos del administrador a crear
     * @return Administrador creado
     */
    AdminUserDTO createCompleteAdmin(AdminCreateDTO adminCreateDTO);

    /**
     * Promociona un usuario existente a administrador
     *
     * @param userId ID del usuario a promocionar
     * @return Usuario promocionado como administrador
     */
    AdminUserDTO promoteUserToAdmin(Long userId);

    /**
     * Actualiza los datos de un administrador existente
     *
     * @param id ID del administrador
     * @param adminCreateDTO Nuevos datos
     * @return Administrador actualizado
     */
    AdminUserDTO updateAdmin(Long id, AdminCreateDTO adminCreateDTO);

    /**
     * Remueve privilegios de administrador de un usuario
     *
     * @param id ID del administrador a "degradar"
     */
    void removeAdminPrivileges(Long id);
}
