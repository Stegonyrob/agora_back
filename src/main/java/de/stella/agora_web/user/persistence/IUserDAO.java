package de.stella.agora_web.user.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

import de.stella.agora_web.user.model.User;





@Component
public interface IUserDAO {
    List<User> findAll();
    
    Optional<User> findById(Long id);

    User save(User user);

    void deleteById(Long id);

    Optional<User> findByUsernameAndPassword(String username, String password);

    Optional<User> findByUsername(String username);

   

    Object update(User user, User updatedUser);
}
