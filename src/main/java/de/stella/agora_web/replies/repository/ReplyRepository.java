package de.stella.agora_web.replies.repository;

import de.stella.agora_web.replies.model.Reply;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
  @SuppressWarnings("null")
  List<Reply> findAll();

  @SuppressWarnings("null")
  Optional<Reply> findById(Long id);
}
