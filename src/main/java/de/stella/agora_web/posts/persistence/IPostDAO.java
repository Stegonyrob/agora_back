package de.stella.agora_web.posts.persistence;


import org.springframework.stereotype.Component;


@Component
public interface IPostDAO {
        void deleteById(Long id);
        // Optional<User> findByUsernameAndPassword(String username, String password);
        // Object update(User user, User updatedUser);
}