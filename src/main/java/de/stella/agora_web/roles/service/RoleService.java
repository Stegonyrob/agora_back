package de.stella.agora_web.roles.service;

import org.springframework.stereotype.Service;

import de.stella.agora_web.roles.exceptions.RoleNotFoundException;
import de.stella.agora_web.roles.model.Role;
import de.stella.agora_web.roles.repository.RoleRepository;

import lombok.NonNull;

@Service
public class RoleService {

    RoleRepository repository;

    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    public Role getById(@NonNull Long id) {
        Role role = repository.findById(id).orElseThrow( () -> new RoleNotFoundException("Role Not found") );
        return role;
    }
}

