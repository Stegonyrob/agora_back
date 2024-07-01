package de.stella.agora_web.tags.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.stella.agora_web.tags.module.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findAll();

    List<Tag> findByName(String name);

    Optional<Tag> findById(Long id);

}