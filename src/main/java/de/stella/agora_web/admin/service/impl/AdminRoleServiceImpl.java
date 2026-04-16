package de.stella.agora_web.admin.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import de.stella.agora_web.admin.service.IAdminRoleService;




/**
 * 🛡️ Implementación temporal del Servicio de Roles Administrativos
 */
@Service
public class AdminRoleServiceImpl implements IAdminRoleService {

    @Override
    public boolean assignAdminRole(Long userId) {
        return true;
    }

    @Override
    public boolean removeAdminRole(Long userId) {
        return true;
    }

    @Override
    public Set<String> getAdminRoles(Long userId) {
        return new HashSet<>();
    }

    @Override
    public boolean hasAdminRole(Long userId) {
        return false;
    }

    @Override
    public boolean assignMultipleAdminRoles(Long userId, Set<String> roleNames) {
        return true;
    }

    @Override
    public boolean removeMultipleAdminRoles(Long userId, Set<String> roleNames) {
        return true;
    }

    @Override
    public boolean updateAdminRoles(Long userId, Set<String> newRoles) {
        return true;
    }

    @Override
    public Set<Long> getUsersWithRole(String roleName) {
        return new HashSet<>();
    }
}
