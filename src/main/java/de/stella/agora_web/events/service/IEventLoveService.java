package de.stella.agora_web.events.service;

public interface IEventLoveService {
    void unloveEventAnonymously(Long eventId);

    void loveEvent(Long eventId, Long profileId);

    void unloveEvent(Long eventId, Long profileId);

    boolean hasLove(Long eventId, Long profileId);

    long countLoves(Long eventId);

    boolean toggleLove(Long eventId, Long profileId);

    void loveEventAnonymously(Long eventId);
}
