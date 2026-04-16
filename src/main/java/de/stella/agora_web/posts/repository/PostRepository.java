package de.stella.agora_web.posts.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.stella.agora_web.posts.controller.dto.PostSummaryDTO;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.replies.model.Reply;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Override
    List<Post> findAll();

    @Override
    Optional<Post> findById(Long id);

    List<Post> findByUser_Id(Long userId);

    List<Post> findByTags_Name(String tagName);

    void save(Reply reply);

    @Query("SELECT p FROM Post p JOIN p.tags t WHERE t.name = :tagName")
    List<Post> findByTagsName(@Param("tagName") String tagName);

    // ✅ Consulta optimizada SEGURA para evitar N+1 sin afectar persistencia
    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.tags WHERE p.id IN :postIds")
    List<Post> findByIdsWithTags(@Param("postIds") List<Long> postIds);

    @Query("""
        SELECT p.id AS id, p.title AS title, p.message AS message, 
               p.creationDate AS creationDate,
               p.archived AS archived,
               (SELECT COUNT(c) FROM Comment c WHERE c.post.id = p.id) AS commentsCount,
               (SELECT COUNT(pl) FROM PostLove pl WHERE pl.post.id = p.id) AS favoritesCount,
               p.user.id AS userId
        FROM Post p
    """)
    Page<PostSummaryDTO> findAllWithCounts(Pageable pageable);
}
