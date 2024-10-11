package de.stella.agora_web.tags.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.stella.agora_web.tags.model.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
  @Override
  List<Tag> findAll();

  List<Tag> findByName(String name);

  @Override
  Optional<Tag> findById(Long id);
}
