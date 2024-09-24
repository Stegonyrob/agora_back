package de.stella.agora_web.comment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.stella.agora_web.comment.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

  @Override
  List<Comment> findAll();

  @Override
  Optional<Comment> findById(Long id);

  public List<Comment> findByTagsName(String tagName);

  public List<Comment> findByUserId(Long userId);

  public Object findByPostId(Long postId);

  List<Comment> findAllByOrderByCreationDateAsc();
}
