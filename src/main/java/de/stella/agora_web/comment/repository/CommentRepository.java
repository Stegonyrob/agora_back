package de.stella.agora_web.comment.repository;

import de.stella.agora_web.comment.model.Comment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
  @SuppressWarnings("null")
  @Override
  List<Comment> findAll();

  @SuppressWarnings("null")
  @Override
  Optional<Comment> findById(Long id);
}
