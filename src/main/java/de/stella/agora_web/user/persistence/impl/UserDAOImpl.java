package de.stella.agora_web.user.persistence.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.persistence.IUserDAO;
import de.stella.agora_web.user.repository.UserRepository;
import lombok.NonNull;

@Component
public class UserDAOImpl implements IUserDAO {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    @NonNull

    public Optional<User> findById(Long id) {
        Objects.requireNonNull(id, "ID cannot be null");
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return ((IUserDAO) userRepository).findByUsername(username);
    }

    @Override
    public User save(User user) {
        Objects.requireNonNull(user, "User cannot be null");
        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        Objects.requireNonNull(id, "ID cannot be null");
        userRepository.deleteById(id);
    }



    @Override
    public Object update(User user, User updatedUser) {
        return ((IUserDAO) userRepository).update(user, updatedUser);
    }
}
