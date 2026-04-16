// UserFavoriteEventRepository.java
package de.stella.agora_web.events.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.stella.agora_web.events.model.UserFavoriteEvent;

public interface UserFavoriteEventRepository extends JpaRepository<UserFavoriteEvent, Long> {

    boolean existsByUserIdAndEventId(Long userId, Long eventId);

    void deleteByUserIdAndEventId(Long userId, Long eventId);

    List<UserFavoriteEvent> findByUserId(Long userId);
}
