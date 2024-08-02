package de.stella.agora_web.posts.repository;

import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.replies.model.Reply;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
  @SuppressWarnings("null")
  List<Post> findAll();

  @SuppressWarnings("null")
  Optional<Post> findById(Long id);

  List<Post> findByUserId(Long userId);

  void save(Reply reply);
}
