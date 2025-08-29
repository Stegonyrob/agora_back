package de.stella.agora_web.events.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.stella.agora_web.events.model.Event;
import de.stella.agora_web.events.model.EventLove;
import de.stella.agora_web.events.repository.EventLoveRepository;
import de.stella.agora_web.events.repository.EventRepository;
import de.stella.agora_web.events.service.IEventLoveService;
import de.stella.agora_web.profiles.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventLoveServiceImpl implements IEventLoveService {

    @Override
    @Transactional
    public void loveEventAnonymously(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con ID: " + eventId));
        event.incrementAnonymousLoves();
        eventRepository.save(event);
        log.info("Love anónimo agregado al evento {}", eventId);
    }

    private final EventLoveRepository eventLoveRepository;
    private final EventRepository eventRepository;
    private final ProfileRepository profileRepository;

    @Transactional
    public void addLove(Long eventId, Long profileId) {
        if (eventLoveRepository.existsByEventIdAndProfileId(eventId, profileId)) {
            log.warn("El perfil {} ya ha dado like al evento {}", profileId, eventId);
            return;
        }

        // Only fetch to validate existence, do not store unused variables
        eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con ID: " + eventId));

        profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado con ID: " + profileId));

        EventLove eventLove = new EventLove();
        eventLoveRepository.save(eventLove);
        log.info("Like agregado: perfil {} dio like al evento {}", profileId, eventId);
    }

    @Transactional
    public void removeLove(Long eventId, Long profileId) {
        EventLove eventLove = eventLoveRepository.findByEventIdAndProfileId(eventId, profileId)
                .orElseThrow(() -> new RuntimeException("Like no encontrado para evento " + eventId + " y perfil " + profileId));
        eventLoveRepository.delete(eventLove);
        log.info("Like eliminado: perfil {} quitó like del evento {}", profileId, eventId);
    }

    @Override
    public boolean hasLove(Long eventId, Long profileId) {
        return eventLoveRepository.existsByEventIdAndProfileId(eventId, profileId);
    }

    @Override
    public long countLoves(Long eventId) {
        return eventLoveRepository.countByEventId(eventId);
    }

    @Override
    @Transactional
    public boolean toggleLove(Long eventId, Long profileId) {
        boolean hasLove = hasLove(eventId, profileId);
        if (hasLove) {
            removeLove(eventId, profileId);
        } else {
            addLove(eventId, profileId);
        }
        return !hasLove;
    }

    @Override
    @Transactional
    public void loveEvent(Long eventId, Long profileId) {
        addLove(eventId, profileId);
    }

    @Override
    public void unloveEvent(Long eventId, Long profileId) {
        removeLove(eventId, profileId);
    }
}
