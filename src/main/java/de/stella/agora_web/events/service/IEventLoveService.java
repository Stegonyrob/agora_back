package de.stella.agora_web.events.service;

/**
 * Servicio específico para la gestión de likes en eventos. Cumple con el
 * principio de Responsabilidad Única (SRP).
 */
public interface IEventLoveService {

    /**
     * Agrega un like a un evento por parte de un perfil.
     *
     * @param eventId ID del evento
     * @param profileId ID del perfil que da el like
     */
    void addLove(Long eventId, Long profileId);

    /**
     * Quita un like de un evento por parte de un perfil.
     *
     * @param eventId ID del evento
     * @param profileId ID del perfil que quita el like
     */
    void removeLove(Long eventId, Long profileId);

    /**
     * Verifica si un perfil ya le ha dado like a un evento.
     *
     * @param eventId ID del evento
     * @param profileId ID del perfil
     * @return true si ya existe el like, false en caso contrario
     */
    boolean hasLove(Long eventId, Long profileId);

    /**
     * Obtiene la cantidad total de likes de un evento.
     *
     * @param eventId ID del evento
     * @return Número total de likes
     */
    Long countLoves(Long eventId);

    /**
     * Alterna el estado del like (toggle). Si existe lo quita, si no existe lo
     * agrega.
     *
     * @param eventId ID del evento
     * @param profileId ID del perfil
     * @return true si se agregó el like, false si se quitó
     */
    boolean toggleLove(Long eventId, Long profileId);
}
