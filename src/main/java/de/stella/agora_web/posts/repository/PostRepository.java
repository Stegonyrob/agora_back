package de.stella.agora_web.posts.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.replies.model.Reply;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
  @Override
  List<Post> findAll();

  @Override
  Optional<Post> findById(Long id);

  List<Post> findByUser_Id(Long userId); // Cambia a "findByUser_Id"

  List<Post> findByTags_Name(String tagName);

  void save(Reply reply);
}
