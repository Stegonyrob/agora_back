package de.stella.agora_web.replies.persistence;

import de.stella.agora_web.user.model.User;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public interface IReplyDAO {
  void deleteById(Long id);

  Optional<User> findByUsernameAndPassword(String username, String password);

  Object update(User user, User updatedUser);
}
