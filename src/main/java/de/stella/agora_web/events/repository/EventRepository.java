package de.stella.agora_web.events.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.stella.agora_web.events.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Override
    List<Event> findAll();

    @Override
    Optional<Event> findById(Long id);

    List<Event> findByUser_Id(Long userId);

    List<Event> findByTags_Name(String tagName);

    List<Event> findByArchived(Boolean archived);

    List<Event> findByTagsName(String tagName);

    // ✅ CONSULTA PARA EVENTOS PÚBLICOS - SIN múltiples FETCH JOIN 
    @Query("SELECT DISTINCT e FROM Event e "
            + "LEFT JOIN FETCH e.images i "
            + "WHERE e.archived = false "
            + "ORDER BY e.creationDate DESC")
    List<Event> findAllPublicEventsWithImages();

    // ✅ NUEVA: CONSULTA PAGINADA PARA EVENTOS PÚBLICOS
    @Query("SELECT e FROM Event e "
            + "WHERE e.archived = false "
            + "ORDER BY e.creationDate DESC")
    Page<Event> findAllPublicEventsPaginated(Pageable pageable);

    // ✅ CONSULTA PARA UN EVENTO PÚBLICO ESPECÍFICO - SIN múltiples FETCH JOIN
    @Query("SELECT e FROM Event e "
            + "LEFT JOIN FETCH e.images i "
            + "WHERE e.id = :id AND e.archived = false")
    Optional<Event> findPublicEventWithImages(@Param("id") Long id);

    // ✅ CONSULTA SEPARADA PARA CARGAR TAGS DE UN EVENTO
    @Query("SELECT e FROM Event e "
            + "LEFT JOIN FETCH e.tags t "
            + "WHERE e.id = :id")
    Optional<Event> findEventWithTags(@Param("id") Long id);

    // ✅ CONSULTA SEPARADA PARA CARGAR ATTENDEES DE UN EVENTO  
    @Query("SELECT e FROM Event e "
            + "LEFT JOIN FETCH e.attendees a "
            + "WHERE e.id = :id")
    Optional<Event> findEventWithAttendees(@Param("id") Long id);

    // ✅ CONSULTA PARA CONTAR LIKES DE UN EVENTO
    @Query("SELECT COUNT(el) FROM EventLove el WHERE el.event.id = :eventId")
    Long countEventLoves(@Param("eventId") Long eventId);

}
