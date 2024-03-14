package de.stella.agora_web.replys.persistence;

import java.util.Optional;
import org.springframework.stereotype.Component;

import de.stella.agora_web.user.model.User;





@Component
public interface IReplyDAO {
    

    void deleteById(Long id);

    Optional<User> findByUsernameAndPassword(String username, String password);

   
    Object update(User user, User updatedUser);
}