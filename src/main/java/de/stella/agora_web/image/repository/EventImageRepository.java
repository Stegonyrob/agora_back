package de.stella.agora_web.image.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.stella.agora_web.image.module.EventImage;

public interface EventImageRepository extends JpaRepository<EventImage, Long> {
    List<EventImage> findByEventId(Long eventId);
}