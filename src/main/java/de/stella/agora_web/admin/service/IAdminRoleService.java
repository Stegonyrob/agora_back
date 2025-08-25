package de.stella.agora_web.admin.service;

import java.util.Set;

/**
 * 🛡️ Servicio de Gestión de Roles Administrativos
 * 
 * Responsabilidad única: Gestionar la asignación y revocación de roles 
 * específicamente para usuarios administrativos.
 * 
 * Se encarga exclusivamente de la lógica de roles y permisos 
 * para el contexto administrativo.
 */
public interface IAdminRoleService {
    
    /**
     * Asigna el rol de administrador a un usuario
     * @param userId ID del usuario
     * @return true si se asignó correctamente, false en caso contrario
     */
    boolean assignAdminRole(Long userId);
    
    /**
     * Remueve el rol de administrador de un usuario
     * @param userId ID del usuario
     * @return true si se removió correctamente, false en caso contrario
     */
    boolean removeAdminRole(Long userId);
    
    /**
     * Obtiene todos los roles asignados a un administrador
     * @param userId ID del usuario administrador
     * @return Conjunto de nombres de roles
     */
    Set<String> getAdminRoles(Long userId);
    
    /**
     * Verifica si un usuario tiene el rol de administrador
     * @param userId ID del usuario
     * @return true si tiene rol admin, false en caso contrario
     */
    boolean hasAdminRole(Long userId);
    
    /**
     * Asigna múltiples roles administrativos a un usuario
     * @param userId ID del usuario
     * @param roleNames Nombres de los roles a asignar
     * @return true si se asignaron correctamente, false en caso contrario
     */
    boolean assignMultipleAdminRoles(Long userId, Set<String> roleNames);
    
    /**
     * Remueve múltiples roles administrativos de un usuario
     * @param userId ID del usuario
     * @param roleNames Nombres de los roles a remover
     * @return true si se removieron correctamente, false en caso contrario
     */
    boolean removeMultipleAdminRoles(Long userId, Set<String> roleNames);
    
    /**
     * Actualiza completamente los roles de un administrador
     * @param userId ID del usuario
     * @param newRoles Nuevos roles a asignar
     * @return true si se actualizaron correctamente, false en caso contrario
     */
    boolean updateAdminRoles(Long userId, Set<String> newRoles);
    
    /**
     * Obtiene todos los usuarios que tienen un rol específico
     * @param roleName Nombre del rol
     * @return Conjunto de IDs de usuarios con ese rol
     */
    Set<Long> getUsersWithRole(String roleName);
}
