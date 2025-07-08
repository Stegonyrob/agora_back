package de.stella.agora_web.events.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import de.stella.agora_web.events.controller.dto.EventDTO;
import de.stella.agora_web.events.controller.dto.EventResponseDTO;
import de.stella.agora_web.events.mapper.EventMapper;
import de.stella.agora_web.events.model.Event;
import de.stella.agora_web.events.model.UserFavoriteEvent;
import de.stella.agora_web.events.repository.EventRepository;
import de.stella.agora_web.events.repository.UserFavoriteEventRepository;
import de.stella.agora_web.events.service.IEventService;
import de.stella.agora_web.image.service.IEventImageService;
import de.stella.agora_web.security.SecurityUser;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.repository.UserRepository;

@Service
public class EventServiceImpl implements IEventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private IEventImageService imageService;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private UserFavoriteEventRepository userFavoriteEventRepository;

    @Autowired
    private UserRepository userRepository;

    private EventDTO toDto(Event event) {
        EventDTO dto = new EventDTO();
        if (event != null) {
            dto.setId(event.getId());
            dto.setTitle(event.getTitle());
            dto.setMessage(event.getMessage());
            dto.setArchived(event.getArchived());
        }
        return dto;
    }

    public List<EventDTO> toDtoList(List<Event> events) {
        return events.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventDTO getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + id));
        return eventMapper.toDto(event);
    }

    @Override
    public EventDTO createEvent(EventDTO eventDTO) {
        Event savedEvent = save(eventDTO);  // Use the save method that assigns the user
        return eventMapper.toDto(savedEvent);
    }

    @Override
    public EventDTO updateEvent(Long id, EventDTO eventDTO) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + id));

        event.setTitle(eventDTO.getTitle());
        event.setMessage(eventDTO.getMessage());
        event.setArchived(eventDTO.isArchived());

        Event savedEvent = eventRepository.save(event);
        return eventMapper.toDto(savedEvent);
    }

    @Override
    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new RuntimeException("Event not found with ID: " + id);
        }

        // Eliminar imágenes asociadas al evento
        imageService.deleteImagesByEventId(id);

        // Eliminar el evento
        eventRepository.deleteById(id);
    }

    @Override
    public EventDTO updateEventImage(Long eventId, String imagePath) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + eventId));

        // Actualizar el path de las imágenes asociadas al evento
        event.setImagePath(imagePath);

        Event updatedEvent = eventRepository.save(event);
        return eventMapper.toDto(updatedEvent);
    }

    @Override
    public void unArchiveEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + id));

        event.setArchived(false);
        eventRepository.save(event);
    }

    @Override
    public void archiveEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + id));

        event.setArchived(true);
        eventRepository.save(event);
    }

    @Override
    public Event getById(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found with ID: " + id));
    }

    @Override
    public Event save(EventDTO eventDTO) {
        Event event = eventMapper.toEntity(eventDTO);        // Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SecurityUser) {
            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
            Long userId = securityUser.getUser().getId();

            // Buscar el usuario en la base de datos
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + userId));

            // Asignar el usuario al evento
            event.setUser(user);
        } else {
            // Fallback: si no hay usuario autenticado, usar admin (ID=1)
            User adminUser = userRepository.findById(1L)
                    .orElseThrow(() -> new RuntimeException("Usuario admin no encontrado"));
            event.setUser(adminUser);
        }

        return eventRepository.save(event);
    }

    @Override
    public Event save(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public List<EventDTO> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return eventMapper.toDtoList(events);
    }

    @Override
    public void addFavorite(Long eventId, Long userId) {
        if (!userFavoriteEventRepository.existsByUserIdAndEventId(userId, eventId)) {
            UserFavoriteEvent favorite = new UserFavoriteEvent(userId, eventId);
            userFavoriteEventRepository.save(favorite);
        }
    }

    @Override
    public void removeFavorite(Long eventId, Long userId) {
        userFavoriteEventRepository.deleteByUserIdAndEventId(userId, eventId);
    }

    @Override
    public List<EventDTO> getFavoriteEventsByUser(Long userId) {
        List<UserFavoriteEvent> favorites = userFavoriteEventRepository.findByUserId(userId);
        List<Long> eventIds = favorites.stream()
                .map(UserFavoriteEvent::getEventId)
                .collect(Collectors.toList());
        List<Event> events = eventRepository.findAllById(eventIds);
        return eventMapper.toDtoList(events);
    }

    @Override
    public EventResponseDTO getEventResponseById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));

        return new EventResponseDTO(
                event.getId(),
                event.getTitle(),
                event.getMessage(),
                null, // location field doesn't exist in Event model
                event.getCreationDate(),
                null, // eventDate field doesn't exist in Event model
                event.getCapacity(),
                event.isArchived(),
                event.getUser() != null ? event.getUser().getUsername() : null,
                event.getUser() != null ? event.getUser().getUsername() : null, // User doesn't have fullName
                event.getTags() != null ? event.getTags().stream()
                .map(tag -> new de.stella.agora_web.tags.dto.TagSummaryDTO(tag.getId(), tag.getName(), false))
                .collect(Collectors.toList()) : null,
                event.getImages() != null ? event.getImages().stream()
                .map(img -> "/api/v1/all/event-images/" + img.getId())
                .collect(Collectors.toList()) : null,
                event.getAttendees() != null ? event.getAttendees().size() : 0
        );
    }

}
