package de.stella.agora_web.events.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.stella.agora_web.events.model.Event;
import de.stella.agora_web.events.model.EventLove;
import de.stella.agora_web.events.repository.EventLoveRepository;
import de.stella.agora_web.events.repository.EventRepository;
import de.stella.agora_web.events.service.IEventLoveService;
import de.stella.agora_web.profiles.model.Profile;
import de.stella.agora_web.profiles.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para gestión de likes en eventos. Cumple con SRP:
 * Solo maneja operaciones relacionadas con likes.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EventLoveServiceImpl implements IEventLoveService {

    private final EventLoveRepository eventLoveRepository;
    private final EventRepository eventRepository;
    private final ProfileRepository profileRepository;

    @Override
    @Transactional
    public void addLove(Long eventId, Long profileId) {
        // Verificar si ya existe el like
        if (eventLoveRepository.existsByEventIdAndProfileId(eventId, profileId)) {
            log.warn("El perfil {} ya ha dado like al evento {}", profileId, eventId);
            return;
        }

        // Buscar evento y perfil
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con ID: " + eventId));

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado con ID: " + profileId));

        // Crear y guardar el like
        EventLove eventLove = new EventLove();
        eventLove.setEvent(event);
        eventLove.setProfile(profile);

        eventLoveRepository.save(eventLove);
        log.info("Like agregado: perfil {} dio like al evento {}", profileId, eventId);
    }

    @Override
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
    public Long countLoves(Long eventId) {
        return eventLoveRepository.countByEventId(eventId);
    }

    @Override
    @Transactional
    public boolean toggleLove(Long eventId, Long profileId) {
        if (hasLove(eventId, profileId)) {
            removeLove(eventId, profileId);
            return false; // Se quitó el like
        } else {
            addLove(eventId, profileId);
            return true; // Se agregó el like
        }
    }
}
