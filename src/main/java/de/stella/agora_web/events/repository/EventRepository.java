package de.stella.agora_web.events.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
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

}
