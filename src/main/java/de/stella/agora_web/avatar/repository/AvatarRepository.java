package de.stella.agora_web.avatar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.stella.agora_web.avatar.module.Avatar;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {
    Optional<Avatar> findByImageName(String imageName);

    List<Avatar> findByPreloadedTrue();
}