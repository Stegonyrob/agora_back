package de.stella.agora_web.user.persistence.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import de.stella.agora_web.user.controller.dto.UserDTO;
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
        return userRepository.findByUsername(username);
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
        Objects.requireNonNull(user, "User cannot be null");
        Objects.requireNonNull(updatedUser, "UpdatedUser cannot be null");

        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword());
        user.setRoles(updatedUser.getRoles());

        return userRepository.save(user);
    }

    @Override
    public List<User> findById(List<Long> ids) {
        return userRepository.findAllById(ids);
    }

    @Override
    public User getLoggedInUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return (User) authentication.getPrincipal();
        }
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

    @Override
    public User save(UserDTO userDTO) {
        if (userDTO == null) {
            throw new IllegalArgumentException("UserDTO cannot be null");
        }

        validateUserDTO(userDTO);
        return null;

    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private void validateUserDTO(UserDTO userDTO) {
        Objects.requireNonNull(userDTO, "UserDTO cannot be null");
        validateUsername(userDTO.getUsername());
        validatePassword(userDTO.getPassword());
    }

    private void validateUsername(String username) {
        Objects.requireNonNull(username, "Username cannot be null");
        if (username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
    }

    private void validatePassword(String password) {
        Objects.requireNonNull(password, "Password cannot be null");
        if (password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
    }

}
