package de.stella.agora_web.profiles.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    boolean existsByUsername(String username);
    Optional<Profile> findByUsername(String username);
    Optional<Profile> findByUsernameAndPassword(String username, String password);
    void deleteByUsername(String username);
    void deleteById(Long id);
    Optional<Profile> findByEmail(String email);
    List<Profile> findAllByIdIn(Collection<Long> ids);
}
