package de.stella.agora_web.avatar.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.stella.agora_web.avatar.module.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
    public Optional<Image> findByImageName(String name);

    public void deleteByImageName(String imageName);
}
