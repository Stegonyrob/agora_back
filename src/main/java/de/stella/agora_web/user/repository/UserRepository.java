package de.stella.agora_web.user.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.stella.agora_web.user.model.User;

public interface UserRepository extends JpaRepository <User, Long>{
    public Optional<User> findByUsername(String username);

    public Optional<User> findByUsernameAndPassword(String username, String password);

    public Optional<User> findById(String subject);

    public User getReferenceById(String string);


    
}


    
// @Repository revisar esto
// public interface UserRepository extends JpaRepository<User, String>{ 
//     boolean existsByUsername(String username); 
//     Optional<User> findByUsername(String username); 
// } 

