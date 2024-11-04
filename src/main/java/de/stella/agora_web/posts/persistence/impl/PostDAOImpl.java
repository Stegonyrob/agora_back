package de.stella.agora_web.posts.persistence.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.stella.agora_web.posts.persistence.IPostDAO;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.repository.UserRepository;
import lombok.NonNull;

@Component
public class PostDAOImpl implements IPostDAO {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> findByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    @Override
    public void deleteById(@NonNull Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User update(User user, User updatedUser) {
        // Assuming that 'user' is the existing user and 'updatedUser' is the user with
        // new changes
        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword());
        user.setRoles(updatedUser.getRoles());
        return userRepository.save(user);
    }

}