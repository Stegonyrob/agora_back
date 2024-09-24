package de.stella.agora_web.replies.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.stella.agora_web.replies.model.Reply;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

  @Override
  List<Reply> findAll();

  @Override
  Optional<Reply> findById(Long id);

  List<Reply> findByPostId(Long postId);

  List<Reply> findByUserId(Long userId);

  List<Reply> findByTags_Name(String tagName);

  public List<Reply> findByCommentId(Long commentId);

  List<Reply> findAllByTagsName(String tagName);
}
