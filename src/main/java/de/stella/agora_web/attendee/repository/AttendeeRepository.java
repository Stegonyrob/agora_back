package de.stella.agora_web.attendee.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.stella.agora_web.attendee.model.Attendee;

public interface AttendeeRepository extends JpaRepository<Attendee, Long> {

    public List<Attendee> findByEventId(Long eventId);

    public void deleteByIdAndEventId(Long attendeeId, Long eventId);

    Optional<Attendee> findByIdAndEventId(Long attendeeId, Long eventId);
}