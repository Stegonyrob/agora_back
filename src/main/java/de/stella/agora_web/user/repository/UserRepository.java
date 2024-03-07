package de.stella.agora_web.user.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import de.stella.agora_web.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

   
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.username = :newUsername, u.email = :newEmail WHERE u.id = :userId")
    void updateUserDetails(Long userId, String newUsername, String newEmail);

    Optional<User> findByUsernameAndPassword(String username, String password);
    Optional<User> findByUsername(String username);

    
    @Query("UPDATE User u SET u.name = :#{#updatedUser.name}, u.email = :#{#updatedUser.email} WHERE u.id = :#{#user.id}")
    Object update(User user, User updatedUser);
}



  

