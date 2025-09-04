package de.stella.agora_web.image.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.stella.agora_web.image.entity.TextImage;

@Repository
public interface TextImageRepository extends JpaRepository<TextImage, Long> {

    List<TextImage> findByTextId(Long textId);
}
