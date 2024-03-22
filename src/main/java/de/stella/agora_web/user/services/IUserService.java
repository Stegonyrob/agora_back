package de.stella.agora_web.user.services;

import java.util.List;
import java.util.Optional;

import de.stella.agora_web.user.model.User;

public interface IUserService {

  List<User> findAll();

  Optional<User> findById(Long id);

  User save(User user);

  void deleteById(Long id);

  Optional<User> findByUsernameAndPassword(String username, String password);

  boolean checkUserRole(String username, String role);

  Object update(User user, User updatedUser);

  List<User> getAll();

}
