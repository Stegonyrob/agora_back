package de.stella.agora_web.profiles.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.stella.agora_web.profiles.model.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long>{
    
    public Optional<Profile> findByEmail(String name);

    default List<Profile> findAll() {
        throw new UnsupportedOperationException("unsupported, please use findById or findByEmail instead");
    }

    public void save(org.springframework.context.annotation.Profile newProfile);
}