package de.stella.agora_web.replies.persistence.impl;

import de.stella.agora_web.replies.persistence.IReplyDAO;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.repository.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReplyDAOImpl implements IReplyDAO {

  @Autowired
  private UserRepository userRepository;

  @Override
  public Optional<User> findByUsernameAndPassword(
    String username,
    String password
  ) {
    return ((IReplyDAO) userRepository).findByUsernameAndPassword(
        username,
        password
      );
  }

  @SuppressWarnings("")
  @Override
  public void deleteById(Long id) {
    userRepository.deleteById(id);
  }

  @Override
  public Object update(User user, User updatedUser) {
    return ((IReplyDAO) userRepository).update(user, updatedUser);
  }
}
