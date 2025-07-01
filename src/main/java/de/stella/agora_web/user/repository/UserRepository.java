package de.stella.agora_web.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import de.stella.agora_web.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByUsername(String username);

    public Optional<User> findByUsernameAndPassword(String username, String password);

    Optional<User> findByEmail(String email);

    // Consulta optimizada para obtener User con Roles en una sola consulta
    @Query("SELECT u FROM User u "
            + "LEFT JOIN FETCH u.roles "
            + "WHERE u.username = :username")
    Optional<User> findByUsernameWithRoles(@Param("username") String username);

    // Consulta optimizada para obtener User con Roles por email
    @Query("SELECT u FROM User u "
            + "LEFT JOIN FETCH u.roles "
            + "WHERE u.email = :email")
    Optional<User> findByEmailWithRoles(@Param("email") String email);

    // Consulta optimizada para obtener User con Roles por ID
    @Query("SELECT u FROM User u "
            + "LEFT JOIN FETCH u.roles "
            + "WHERE u.id = :id")
    Optional<User> findByIdWithRoles(@Param("id") Long id);

    // Consulta optimizada para obtener todos los usuarios con todas las relaciones necesarias para el DTO
    @Query("SELECT DISTINCT u FROM User u "
            + "LEFT JOIN FETCH u.roles "
            + "LEFT JOIN FETCH u.profile p "
            + "LEFT JOIN FETCH p.avatar "
            + "LEFT JOIN FETCH u.banned")
    List<User> findAllWithProfileAndRoles();
}
