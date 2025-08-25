package de.stella.agora_web.admin.service;

import java.util.List;

import de.stella.agora_web.admin.controller.dto.AdminUserDTO;
import de.stella.agora_web.admin.controller.dto.AdminCreateDTO;

/**
 * 🧑‍💼 Servicio de Gestión de Usuarios Administrativos
 * 
 * Responsabilidad única: Gestionar el ciclo de vida de los usuarios 
 * con privilegios administrativos.
 * 
 * Se enfoca exclusivamente en operaciones CRUD de usuarios admin.
 */
public interface IAdminUserService {
    
    /**
     * Busca todos los usuarios con rol de administrador
     * @return Lista de usuarios administradores
     */
    List<AdminUserDTO> findAllAdminUsers();
    
    /**
     * Busca un usuario administrador por su ID
     * @param userId ID del usuario
     * @return Usuario administrador o null si no existe
     */
    AdminUserDTO findAdminUserById(Long userId);
    
    /**
     * Busca un usuario administrador por su email
     * @param email Email del usuario
     * @return Usuario administrador o null si no existe
     */
    AdminUserDTO findAdminUserByEmail(String email);
    
    /**
     * Crea un nuevo usuario con permisos administrativos
     * @param adminData Datos del usuario a crear
     * @return Usuario creado
     */
    AdminUserDTO createAdminUser(AdminCreateDTO adminData);
    
    /**
     * Actualiza los datos básicos de un usuario administrador
     * @param userId ID del usuario
     * @param updateData Nuevos datos
     * @return Usuario actualizado
     */
    AdminUserDTO updateAdminUser(Long userId, AdminCreateDTO updateData);
    
    /**
     * Elimina un usuario administrador del sistema
     * @param userId ID del usuario a eliminar
     */
    void deleteAdminUser(Long userId);
    
    /**
     * Verifica si un usuario tiene privilegios administrativos
     * @param userId ID del usuario
     * @return true si es admin, false en caso contrario
     */
    boolean isUserAdmin(Long userId);
    
    /**
     * Verifica si un email ya está en uso por un administrador
     * @param email Email a verificar
     * @return true si ya existe, false en caso contrario
     */
    boolean existsAdminByEmail(String email);
}
