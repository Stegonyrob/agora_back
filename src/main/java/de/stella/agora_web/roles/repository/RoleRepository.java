package de.stella.agora_web.roles.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import de.stella.agora_web.roles.model.Role;


@Repository

public interface RoleRepository extends JpaRepository<Role,Long>{
    
}
