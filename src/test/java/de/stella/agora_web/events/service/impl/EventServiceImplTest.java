package de.stella.agora_web.events.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import de.stella.agora_web.attendee.model.Attendee;
import de.stella.agora_web.events.controller.dto.EventDTO;
import de.stella.agora_web.events.controller.dto.EventResponseDTO;
import de.stella.agora_web.events.mapper.EventMapper;
import de.stella.agora_web.events.model.Event;
import de.stella.agora_web.events.model.UserFavoriteEvent;
import de.stella.agora_web.events.repository.EventRepository;
import de.stella.agora_web.events.repository.UserFavoriteEventRepository;
import de.stella.agora_web.image.module.EventImage;
import de.stella.agora_web.image.service.IEventImageService;
import de.stella.agora_web.security.SecurityUser;
import de.stella.agora_web.tags.model.Tag;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.repository.UserRepository;

/**
 * Comprehensive tests for EventServiceImpl - Second largest service (820
 * instructions) Tests cover CRUD operations, archiving, favorites, attendee
 * management, and image handling
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("EventServiceImpl Unit Tests")
class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private IEventImageService imageService;

    @Mock
    private EventMapper eventMapper;

    @Mock
    private UserFavoriteEventRepository userFavoriteEventRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private EventServiceImpl eventService;

    private User testUser;
    private Event testEvent;
    private EventDTO eventDTO;
    private Tag testTag;

    @BeforeEach
    void setUp() {
        // Setup test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");

        // Setup test tag
        testTag = new Tag();
        testTag.setId(1L);
        testTag.setName("test-tag");

        // Setup test event
        testEvent = new Event();
        testEvent.setId(1L);
        testEvent.setTitle("Test Event");
        testEvent.setMessage("Test event description");
        testEvent.setCapacity(50);
        testEvent.setEventDate(LocalDate.of(2025, 12, 31));
        testEvent.setEventTime(LocalTime.of(18, 0));
        testEvent.setCreationDate(LocalDateTime.now());
        testEvent.setArchived(false);
        testEvent.setUser(testUser);
        testEvent.setTags(new ArrayList<>(List.of(testTag)));
        testEvent.setImages(new ArrayList<>());
        testEvent.setAttendees(new ArrayList<>());

        // Setup EventDTO
        eventDTO = new EventDTO();
        eventDTO.setId(1L);
        eventDTO.setTitle("Test Event");
        eventDTO.setMessage("Test event description");
        eventDTO.setCapacity(50);
        eventDTO.setEventDate(LocalDate.of(2025, 12, 31));
        eventDTO.setEventTime(LocalTime.of(18, 0));
        eventDTO.setArchived(false);
    }

    // ============ TESTS GET EVENT BY ID ============
    @Test
    @DisplayName("getEventById should return event when found")
    void testGetEventById_ValidId_ShouldReturnEvent() {
        // Given
        when(eventRepository.findById(1L)).thenReturn(Optional.of(testEvent));
        when(eventMapper.toDto(testEvent)).thenReturn(eventDTO);

        // When
        EventDTO result = eventService.getEventById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Test Event");
        verify(eventRepository).findById(1L);
        verify(eventMapper).toDto(testEvent);
    }

    @Test
    @DisplayName("getEventById should throw exception when event not found")
    void testGetEventById_InvalidId_ShouldThrowException() {
        // Given
        when(eventRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> eventService.getEventById(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Event not found with ID: 999");
        verify(eventRepository).findById(999L);
    }

    // ============ TESTS CREATE EVENT ============
    @Test
    @DisplayName("createEvent should create event successfully")
    void testCreateEvent_ValidData_ShouldCreateEvent() {
        // Given
        SecurityUser securityUser = mock(SecurityUser.class);
        when(securityUser.getUser()).thenReturn(testUser);
        when(authentication.getPrincipal()).thenReturn(securityUser);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(eventMapper.toEntity(eventDTO)).thenReturn(testEvent);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(eventRepository.save(any(Event.class))).thenReturn(testEvent);
        when(eventMapper.toDto(testEvent)).thenReturn(eventDTO);

        // When
        EventDTO result = eventService.createEvent(eventDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test Event");
        verify(eventMapper).toEntity(eventDTO);
        verify(eventRepository).save(any(Event.class));
        verify(eventMapper).toDto(testEvent);
    }

    @Test
    @DisplayName("createEvent should use admin user when no authentication")
    void testCreateEvent_NoAuth_ShouldUseAdminUser() {
        // Given
        User adminUser = new User();
        adminUser.setId(1L);
        adminUser.setUsername("admin");

        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        when(eventMapper.toEntity(eventDTO)).thenReturn(testEvent);
        when(userRepository.findById(1L)).thenReturn(Optional.of(adminUser));
        when(eventRepository.save(any(Event.class))).thenReturn(testEvent);
        when(eventMapper.toDto(testEvent)).thenReturn(eventDTO);

        // When
        EventDTO result = eventService.createEvent(eventDTO);

        // Then
        assertThat(result).isNotNull();
        verify(userRepository).findById(1L);
        verify(eventRepository).save(any(Event.class));
    }

    // ============ TESTS UPDATE EVENT ============
    @Test
    @DisplayName("updateEvent should update event successfully")
    void testUpdateEvent_ValidData_ShouldUpdateEvent() {
        // Given
        EventDTO updateDTO = new EventDTO();
        updateDTO.setTitle("Updated Event");
        updateDTO.setMessage("Updated description");
        updateDTO.setCapacity(100);
        updateDTO.setEventDate(LocalDate.of(2026, 1, 15));
        updateDTO.setEventTime(LocalTime.of(20, 0));
        updateDTO.setArchived(false);

        Event updatedEvent = new Event();
        updatedEvent.setId(1L);
        updatedEvent.setTitle("Updated Event");
        updatedEvent.setMessage("Updated description");
        updatedEvent.setCapacity(100);
        updatedEvent.setEventDate(LocalDate.of(2026, 1, 15));
        updatedEvent.setEventTime(LocalTime.of(20, 0));

        when(eventRepository.findById(1L)).thenReturn(Optional.of(testEvent));
        when(eventRepository.save(any(Event.class))).thenReturn(updatedEvent);
        when(eventMapper.toDto(updatedEvent)).thenReturn(updateDTO);

        // When
        EventDTO result = eventService.updateEvent(1L, updateDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Updated Event");
        assertThat(result.getCapacity()).isEqualTo(100);
        verify(eventRepository).findById(1L);
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    @DisplayName("updateEvent should throw exception when event not found")
    void testUpdateEvent_EventNotFound_ShouldThrowException() {
        // Given
        when(eventRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> eventService.updateEvent(999L, eventDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Event not found with ID: 999");
        verify(eventRepository).findById(999L);
        verify(eventRepository, never()).save(any(Event.class));
    }

    // ============ TESTS DELETE EVENT ============
    @Test
    @DisplayName("deleteEvent should delete event and its images")
    void testDeleteEvent_ValidId_ShouldDeleteEvent() {
        // Given
        when(eventRepository.existsById(1L)).thenReturn(true);
        doNothing().when(imageService).deleteImagesByEventId(1L);
        doNothing().when(eventRepository).deleteById(1L);

        // When
        eventService.deleteEvent(1L);

        // Then
        verify(eventRepository).existsById(1L);
        verify(imageService).deleteImagesByEventId(1L);
        verify(eventRepository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteEvent should throw exception when event not found")
    void testDeleteEvent_EventNotFound_ShouldThrowException() {
        // Given
        when(eventRepository.existsById(999L)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> eventService.deleteEvent(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Event not found with ID: 999");
        verify(eventRepository).existsById(999L);
        verify(imageService, never()).deleteImagesByEventId(anyLong());
        verify(eventRepository, never()).deleteById(anyLong());
    }

    // ============ TESTS ARCHIVE/UNARCHIVE EVENT ============
    @Test
    @DisplayName("archiveEvent should archive event")
    void testArchiveEvent_ShouldSetArchivedTrue() {
        // Given
        when(eventRepository.findById(1L)).thenReturn(Optional.of(testEvent));
        when(eventRepository.save(any(Event.class))).thenReturn(testEvent);

        // When
        eventService.archiveEvent(1L);

        // Then
        assertThat(testEvent.getArchived()).isTrue();
        verify(eventRepository).findById(1L);
        verify(eventRepository).save(testEvent);
    }

    @Test
    @DisplayName("unArchiveEvent should unarchive event")
    void testUnArchiveEvent_ShouldSetArchivedFalse() {
        // Given
        testEvent.setArchived(true);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(testEvent));
        when(eventRepository.save(any(Event.class))).thenReturn(testEvent);

        // When
        eventService.unArchiveEvent(1L);

        // Then
        assertThat(testEvent.getArchived()).isFalse();
        verify(eventRepository).findById(1L);
        verify(eventRepository).save(testEvent);
    }

    @Test
    @DisplayName("archiveEvent should throw exception when event not found")
    void testArchiveEvent_EventNotFound_ShouldThrowException() {
        // Given
        when(eventRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> eventService.archiveEvent(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Event not found with ID: 999");
        verify(eventRepository).findById(999L);
    }

    // ============ TESTS GET ALL EVENTS ============
    @Test
    @DisplayName("getAllEvents should return all events")
    void testGetAllEvents_ShouldReturnAllEvents() {
        // Given
        Event event2 = new Event();
        event2.setId(2L);
        event2.setTitle("Second Event");

        EventDTO eventDTO2 = new EventDTO();
        eventDTO2.setId(2L);
        eventDTO2.setTitle("Second Event");

        List<Event> events = Arrays.asList(testEvent, event2);
        List<EventDTO> eventDTOs = Arrays.asList(eventDTO, eventDTO2);

        when(eventRepository.findAll()).thenReturn(events);
        when(eventMapper.toDtoList(events)).thenReturn(eventDTOs);

        // When
        List<EventDTO> result = eventService.getAllEvents();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Test Event");
        assertThat(result.get(1).getTitle()).isEqualTo("Second Event");
        verify(eventRepository).findAll();
        verify(eventMapper).toDtoList(events);
    }

    @Test
    @DisplayName("getAllEvents should return empty list when no events")
    void testGetAllEvents_NoEvents_ShouldReturnEmptyList() {
        // Given
        when(eventRepository.findAll()).thenReturn(Collections.emptyList());
        when(eventMapper.toDtoList(Collections.emptyList())).thenReturn(Collections.emptyList());

        // When
        List<EventDTO> result = eventService.getAllEvents();

        // Then
        assertThat(result).isEmpty();
        verify(eventRepository).findAll();
    }

    // ============ TESTS FAVORITES MANAGEMENT ============
    @Test
    @DisplayName("addFavorite should add favorite when not exists")
    void testAddFavorite_NotExists_ShouldAddFavorite() {
        // Given
        when(userFavoriteEventRepository.existsByUserIdAndEventId(1L, 1L)).thenReturn(false);
        when(userFavoriteEventRepository.save(any(UserFavoriteEvent.class))).thenReturn(new UserFavoriteEvent());

        // When
        eventService.addFavorite(1L, 1L);

        // Then
        verify(userFavoriteEventRepository).existsByUserIdAndEventId(1L, 1L);
        verify(userFavoriteEventRepository).save(any(UserFavoriteEvent.class));
    }

    @Test
    @DisplayName("addFavorite should not add favorite when already exists")
    void testAddFavorite_AlreadyExists_ShouldNotAdd() {
        // Given
        when(userFavoriteEventRepository.existsByUserIdAndEventId(1L, 1L)).thenReturn(true);

        // When
        eventService.addFavorite(1L, 1L);

        // Then
        verify(userFavoriteEventRepository).existsByUserIdAndEventId(1L, 1L);
        verify(userFavoriteEventRepository, never()).save(any(UserFavoriteEvent.class));
    }

    @Test
    @DisplayName("removeFavorite should remove favorite")
    void testRemoveFavorite_ShouldRemoveFavorite() {
        // Given
        doNothing().when(userFavoriteEventRepository).deleteByUserIdAndEventId(1L, 1L);

        // When
        eventService.removeFavorite(1L, 1L);

        // Then
        verify(userFavoriteEventRepository).deleteByUserIdAndEventId(1L, 1L);
    }

    @Test
    @DisplayName("getFavoriteEventsByUser should return user's favorite events")
    void testGetFavoriteEventsByUser_ShouldReturnFavorites() {
        // Given
        UserFavoriteEvent favorite1 = new UserFavoriteEvent(1L, 1L);
        UserFavoriteEvent favorite2 = new UserFavoriteEvent(1L, 2L);
        List<UserFavoriteEvent> favorites = Arrays.asList(favorite1, favorite2);

        Event event2 = new Event();
        event2.setId(2L);
        event2.setTitle("Favorite Event 2");

        List<Event> events = Arrays.asList(testEvent, event2);
        List<EventDTO> eventDTOs = Arrays.asList(eventDTO, new EventDTO());

        when(userFavoriteEventRepository.findByUserId(1L)).thenReturn(favorites);
        when(eventRepository.findAllById(Arrays.asList(1L, 2L))).thenReturn(events);
        when(eventMapper.toDtoList(events)).thenReturn(eventDTOs);

        // When
        List<EventDTO> result = eventService.getFavoriteEventsByUser(1L);

        // Then
        assertThat(result).hasSize(2);
        verify(userFavoriteEventRepository).findByUserId(1L);
        verify(eventRepository).findAllById(Arrays.asList(1L, 2L));
        verify(eventMapper).toDtoList(events);
    }

    @Test
    @DisplayName("getFavoriteEventsByUser should return empty list when no favorites")
    void testGetFavoriteEventsByUser_NoFavorites_ShouldReturnEmptyList() {
        // Given
        when(userFavoriteEventRepository.findByUserId(1L)).thenReturn(Collections.emptyList());
        when(eventRepository.findAllById(Collections.emptyList())).thenReturn(Collections.emptyList());
        when(eventMapper.toDtoList(Collections.emptyList())).thenReturn(Collections.emptyList());

        // When
        List<EventDTO> result = eventService.getFavoriteEventsByUser(1L);

        // Then
        assertThat(result).isEmpty();
        verify(userFavoriteEventRepository).findByUserId(1L);
    }

    // ============ TESTS UPDATE EVENT IMAGE ============
    @Test
    @DisplayName("updateEventImage should update image path")
    void testUpdateEventImage_ValidPath_ShouldUpdateImage() {
        // Given
        String imagePath = "/images/event-image.jpg";
        when(eventRepository.findById(1L)).thenReturn(Optional.of(testEvent));
        when(eventRepository.save(any(Event.class))).thenReturn(testEvent);
        when(eventMapper.toDto(testEvent)).thenReturn(eventDTO);

        // When
        EventDTO result = eventService.updateEventImage(1L, imagePath);

        // Then
        assertThat(result).isNotNull();
        verify(eventRepository).findById(1L);
        verify(eventRepository).save(testEvent);
    }

    @Test
    @DisplayName("updateEventImage should throw exception when event not found")
    void testUpdateEventImage_EventNotFound_ShouldThrowException() {
        // Given
        when(eventRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> eventService.updateEventImage(999L, "/path/image.jpg"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Event not found with ID: 999");
        verify(eventRepository).findById(999L);
        verify(eventRepository, never()).save(any(Event.class));
    }

    // ============ TESTS GET EVENT RESPONSE BY ID ============
    @Test
    @DisplayName("getEventResponseById should return complete event response")
    void testGetEventResponseById_ShouldReturnCompleteResponse() {
        // Given
        EventImage image = new EventImage();
        image.setId(1L);
        image.setImageName("event-image.jpg");
        testEvent.setImages(List.of(image));

        Attendee attendee = new Attendee();
        attendee.setId(1L);
        testEvent.setAttendees(List.of(attendee));

        when(eventRepository.findById(1L)).thenReturn(Optional.of(testEvent));

        // When
        EventResponseDTO result = eventService.getEventResponseById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Test Event");
        assertThat(result.getEventDate()).isEqualTo(LocalDate.of(2025, 12, 31));
        assertThat(result.getEventTime()).isNotNull();
        assertThat(result.getCapacity()).isEqualTo(50);
        assertThat(result.getUserUsername()).isEqualTo("testuser");
        assertThat(result.getTags()).hasSize(1);
        assertThat(result.getImages()).hasSize(1);
        assertThat(result.getParticipantsCount()).isEqualTo(1);
        verify(eventRepository).findById(1L);
    }

    @Test
    @DisplayName("getEventResponseById should throw exception when event not found")
    void testGetEventResponseById_EventNotFound_ShouldThrowException() {
        // Given
        when(eventRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> eventService.getEventResponseById(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Event not found with id: 999");
        verify(eventRepository).findById(999L);
    }

    // ============ TESTS SAVE METHOD ============
    @Test
    @DisplayName("save(Event) should save event entity")
    void testSave_EventEntity_ShouldSaveEvent() {
        // Given
        when(eventRepository.save(testEvent)).thenReturn(testEvent);

        // When
        Event result = eventService.save(testEvent);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(eventRepository).save(testEvent);
    }

    @Test
    @DisplayName("getById should return event when found")
    void testGetById_ValidId_ShouldReturnEvent() {
        // Given
        when(eventRepository.findById(1L)).thenReturn(Optional.of(testEvent));

        // When
        Event result = eventService.getById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(eventRepository).findById(1L);
    }

    @Test
    @DisplayName("getById should throw exception when event not found")
    void testGetById_InvalidId_ShouldThrowException() {
        // Given
        when(eventRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> eventService.getById(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Event not found with ID: 999");
        verify(eventRepository).findById(999L);
    }

    // ============ EDGE CASES ============
    @Test
    @DisplayName("getEventResponseById with null tags should handle gracefully")
    void testGetEventResponseById_NullTags_ShouldHandleGracefully() {
        // Given
        testEvent.setTags(null);
        testEvent.setImages(null);
        testEvent.setAttendees(null);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(testEvent));

        // When
        EventResponseDTO result = eventService.getEventResponseById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTags()).isNull();
        assertThat(result.getImages()).isNull();
        assertThat(result.getParticipantsCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("updateEvent should handle null eventTime")
    void testUpdateEvent_NullEventTime_ShouldUpdate() {
        // Given
        eventDTO.setEventTime(null);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(testEvent));
        when(eventRepository.save(any(Event.class))).thenReturn(testEvent);
        when(eventMapper.toDto(testEvent)).thenReturn(eventDTO);

        // When
        EventDTO result = eventService.updateEvent(1L, eventDTO);

        // Then
        assertThat(result).isNotNull();
        verify(eventRepository).save(any(Event.class));
    }
}
