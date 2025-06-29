package de.stella.agora_web.profiles.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.stella.agora_web.profiles.model.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    boolean existsByUsername(String username);

    Optional<Profile> findByUsername(String username);

    Optional<Profile> findByUsernameAndPassword(String username, String password);

    void deleteByUsername(String username);

    @Override
    void deleteById(Long id);

    Optional<Profile> findByEmail(String email);

    List<Profile> findAllByIdIn(Collection<Long> ids);

    // Consulta optimizada para obtener Profile con User y Roles en una sola consulta
    @Query("SELECT p FROM Profile p "
            + "JOIN FETCH p.user u "
            + "LEFT JOIN FETCH u.roles "
            + "WHERE p.id = :id")
    Optional<Profile> findByIdWithUserAndRoles(@Param("id") Long id);

    // Consulta optimizada para obtener Profile con User por userId
    @Query("SELECT p FROM Profile p "
            + "JOIN FETCH p.user u "
            + "LEFT JOIN FETCH u.roles "
            + "WHERE u.id = :userId")
    Optional<Profile> findByUserIdWithUserAndRoles(@Param("userId") Long userId);

    // Consulta optimizada para obtener Profile solo con User básico (sin roles)
    @Query("SELECT p FROM Profile p "
            + "JOIN FETCH p.user u "
            + "WHERE p.id = :id")
    Optional<Profile> findByIdWithUser(@Param("id") Long id);
}
