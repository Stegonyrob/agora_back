package de.stella.agora_web.tags.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.stella.agora_web.tags.model.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    @Override
    List<Tag> findAll();

    List<Tag> findByName(String name);

    @Override
    Optional<Tag> findById(Long id);

    // Consulta directa para obtener tags de un post específico (evita serialización circular)
    @Query("SELECT t FROM Tag t JOIN t.posts p WHERE p.id = :postId")
    List<Tag> findByPostId(@Param("postId") Long postId);

    // Consulta directa para obtener tags de un evento específico (evita serialización circular)
    @Query("SELECT t FROM Tag t JOIN t.events e WHERE e.id = :eventId")
    List<Tag> findByEventId(@Param("eventId") Long eventId);
}
