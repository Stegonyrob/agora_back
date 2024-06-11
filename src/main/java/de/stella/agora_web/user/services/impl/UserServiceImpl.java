package de.stella.agora_web.user.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.stella.agora_web.user.controller.dto.UserDTO;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.persistence.IUserDAO;
import de.stella.agora_web.user.services.IUserService;
@Service
public class UserServiceImpl implements IUserService {

    private final IUserDAO userDAO;

    public UserServiceImpl(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public Optional<User> findByUsernameAndPassword(String username, String password) {
        return userDAO.findByUsernameAndPassword(username, password);
    }
    @Override
    public Optional<User> findUserById(Long userId) {
        return userDAO.findById(userId);
    }

    @Override
    public User registerUser(UserDTO userDTO) {
        User user = userDTO.toUser();
        user.setPassword(encryptPassword(user.getPassword()));
        return userDAO.save(user);
    }

    private String encryptPassword(String password) {
        // Replace with your own encryption algorithm
        return password;
    }

    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userDAO.findById(id);
    }

    @Override
    public User save(User user) {
        return userDAO.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userDAO.deleteById(id);
    }

    @Override
    public boolean checkUserRole(String username, String role) {
        return findByUsernameAndPassword(username, "")
                .map(user -> user.hasRole(role))
                .orElse(false);
    }
    @Override
    public User update(User user, User updatedUser) {
        if (user == null || updatedUser == null) {
            throw new IllegalArgumentException("User and updatedUser cannot be null");
        }
        User mergedUser = new User();
        return userDAO.save(mergedUser);
    }

    @Override
    public List<User> getAll() {
        return userDAO.findAll();
    }
@Override
public Optional<User> findByUsername(String username) {
    return userDAO.findByUsername(username);
}

    @Override
    public List<User> getById(List<Long> ids) {
        return userDAO.findAllById(ids);
    }
}



