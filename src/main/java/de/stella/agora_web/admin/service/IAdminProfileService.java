package de.stella.agora_web.admin.service;

import java.util.List;

import de.stella.agora_web.admin.controller.dto.AdminProfileDTO;

/**
 * 👤 Servicio de Gestión de Perfiles de Administradores
 * 
 * Responsabilidad única: Gestionar los perfiles asociados a usuarios administrativos.
 * 
 * Se encarga exclusivamente de la gestión de perfiles, avatares y datos 
 * personales de los administradores.
 */
public interface IAdminProfileService {
    
    /**
     * Obtiene todos los perfiles de administradores
     * @return Lista de perfiles administrativos
     */
    List<AdminProfileDTO> getAllAdminProfiles();
    
    /**
     * Obtiene un perfil de administrador por ID
     * @param profileId ID del perfil
     * @return Perfil encontrado
     */
    AdminProfileDTO getAdminProfileById(Long profileId);
    
    /**
     * Obtiene el perfil asociado a un usuario administrador
     * @param userId ID del usuario
     * @return Perfil del administrador
     */
    AdminProfileDTO getProfileByAdminUserId(Long userId);
    
    /**
     * Crea un nuevo perfil para un administrador
     * @param userId ID del usuario administrador
     * @param firstName Nombre
     * @param lastName Apellido
     * @param avatarId ID del avatar (opcional)
     * @return Perfil creado
     */
    AdminProfileDTO createAdminProfile(Long userId, String firstName, String lastName, Long avatarId);
    
    /**
     * Actualiza los datos de un perfil administrativo
     * @param profileId ID del perfil
     * @param firstName Nuevo nombre
     * @param lastName Nuevo apellido
     * @param avatarId Nuevo avatar ID (opcional)
     * @return Perfil actualizado
     */
    AdminProfileDTO updateAdminProfile(Long profileId, String firstName, String lastName, Long avatarId);
    
    /**
     * Actualiza solo el avatar de un perfil administrativo
     * @param profileId ID del perfil
     * @param avatarId Nuevo avatar ID
     * @return Perfil actualizado
     */
    AdminProfileDTO updateAdminAvatar(Long profileId, Long avatarId);
    
    /**
     * Elimina un perfil administrativo
     * @param profileId ID del perfil a eliminar
     */
    void deleteAdminProfile(Long profileId);
    
    /**
     * Verifica si existe un perfil para un usuario específico
     * @param userId ID del usuario
     * @return true si existe el perfil, false en caso contrario
     */
    boolean existsProfileForUser(Long userId);
}
