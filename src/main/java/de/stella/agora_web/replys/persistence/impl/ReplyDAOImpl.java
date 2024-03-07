package de.stella.agora_web.replys.persistence.impl;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.stella.agora_web.replys.persistence.IReplyDAO;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.repository.UserRepository;


@Component
public class ReplyDAOImpl implements IReplyDAO {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> findByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    @SuppressWarnings("null")
    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Object update(User user, User updatedUser) {
        return userRepository.update(user, updatedUser);
    }
}
