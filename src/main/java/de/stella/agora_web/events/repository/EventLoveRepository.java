package de.stella.agora_web.events.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.stella.agora_web.events.model.EventLove;

@Repository
public interface EventLoveRepository extends JpaRepository<EventLove, Long> {

    /**
     * Busca un like específico por evento y perfil.
     */
    @Query("SELECT el FROM EventLove el WHERE el.event.id = :eventId AND el.profile.id = :profileId")
    Optional<EventLove> findByEventIdAndProfileId(@Param("eventId") Long eventId, @Param("profileId") Long profileId);

    /**
     * Cuenta los likes de un evento específico.
     */
    @Query("SELECT COUNT(el) FROM EventLove el WHERE el.event.id = :eventId")
    Long countByEventId(@Param("eventId") Long eventId);

    /**
     * Verifica si ya existe un like para un evento y perfil específicos.
     */
    @Query("SELECT CASE WHEN COUNT(el) > 0 THEN true ELSE false END FROM EventLove el WHERE el.event.id = :eventId AND el.profile.id = :profileId")
    boolean existsByEventIdAndProfileId(@Param("eventId") Long eventId, @Param("profileId") Long profileId);

    /**
     * Elimina un like específico por evento y perfil.
     */
    @Query("DELETE FROM EventLove el WHERE el.event.id = :eventId AND el.profile.id = :profileId")
    void deleteByEventIdAndProfileId(@Param("eventId") Long eventId, @Param("profileId") Long profileId);
}
