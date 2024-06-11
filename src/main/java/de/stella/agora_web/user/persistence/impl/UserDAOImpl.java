package de.stella.agora_web.user.persistence.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Component;

import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.persistence.IUserDAO;
import de.stella.agora_web.user.repository.UserRepository;

@Component
public class UserDAOImpl implements IUserDAO {
    private final UserRepository userRepository;

    public UserDAOImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        Objects.requireNonNull(id, "ID cannot be null");
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found")));
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
    public Optional<User> findByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    @Override
    public User update(User user, User updatedUser) {
        // Implement the update logic here
        return user;
    }

    @Override
    public List<User> findById(List<Long> ids) {
        return userRepository.findAllById(ids);
    }

    @Override
    public User getLoggedInUser() {
        // Implement the logic to get the logged-in user
        return null;
    }

    @Override
    public List<User> findAllById(List<Long> ids) {
        return userRepository.findAllById(ids);
    }

    @Override
    public Optional<User> findUserById(Long userId) {
        return userRepository.findById(userId);
    }
}
