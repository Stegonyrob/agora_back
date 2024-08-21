package de.stella.agora_web.tags.repository;

import de.stella.agora_web.tags.model.Tag;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
  @SuppressWarnings("null")
  List<Tag> findAll();

  List<Tag> findByName(String name);

  @SuppressWarnings("null")
  Optional<Tag> findById(Long id);
}
