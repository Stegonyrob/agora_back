package de.stella.agora_web.posts.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.posts.model.PostLove;
import de.stella.agora_web.profiles.model.Profile;

@Repository
public interface PostLoveRepository extends JpaRepository<PostLove, Long> {

    boolean existsByPostIdAndProfileUserId(Long postId, Long userId);

    Optional<PostLove> findByPostIdAndProfileUserId(Long postId, Long userId);

    Optional<PostLove> findByProfileAndPost(Profile profile, Post post);

    boolean existsByProfileAndPost(Profile profile, Post post);

    int countByPostId(Long postId);
}
