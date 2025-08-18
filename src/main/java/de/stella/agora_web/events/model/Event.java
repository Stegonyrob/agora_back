package de.stella.agora_web.events.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import de.stella.agora_web.attendee.model.Attendee;
import de.stella.agora_web.image.module.EventImage;
import de.stella.agora_web.tags.model.Tag;
import de.stella.agora_web.user.model.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 1, max = 100)
    @Column(name = "title")
    private String title;

    @NotBlank
    @Size(min = 1, max = 250)
    @Column(name = "message", length = 250)
    private String message;

    @Column(name = "creation_date")
    private LocalDateTime creationDate = LocalDateTime.now();

    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @Column(name = "event_time", nullable = false)
    private String eventTime;

    @Column(name = "archived")
    private boolean archived;

    @Column(name = "capacity")
    private int capacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToMany
    @JoinTable(name = "event_tag", joinColumns = @JoinColumn(name = "event_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "event_attendee", joinColumns = @JoinColumn(name = "event_id"), inverseJoinColumns = @JoinColumn(name = "attendee_id"))
    private List<Attendee> attendees = new ArrayList<>();

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<EventImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<EventLove> eventLoves = new HashSet<>();

    // Constructor vacío
    public Event() {
    }

    // Constructor con parámetros
    public Event(Long id, String title, String message, Long userId, boolean archived, int capacity) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.archived = archived;
        this.capacity = capacity;
        this.creationDate = LocalDateTime.now();
        // Eliminada inicialización de eventDate
    }

    // Getter y Setter para id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter y Setter para title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Getter y Setter para message
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // Getter y Setter para creationDate
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    // Getter y Setter para archived
    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    // Alias para isArchived()
    public boolean getArchived() {
        return isArchived();
    }

    // Getter y Setter para tags
    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
    }

    // Getter y Setter para attendees
    public List<Attendee> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<Attendee> attendees) {
        this.attendees = attendees;
    }

    public void addAttendee(Attendee attendee) {
        this.attendees.add(attendee);
    }

    public void removeAttendee(Attendee attendee) {
        this.attendees.remove(attendee);
    }

    // Getter y Setter para images
    public List<EventImage> getImages() {
        return images;
    }

    public void setImages(List<EventImage> images) {
        this.images = images;
    }

    public void addImage(EventImage image) {
        this.images.add(image);
        image.setEvent(this);
    }

    public void removeImage(EventImage image) {
        this.images.remove(image);
        image.setEvent(null);
    }

    public void removeAllImages() {
        for (EventImage image : new ArrayList<>(images)) {
            image.setEvent(null);
        }
        images.clear();
    }

    // Método para establecer el ID del usuario
    public void setUserId(Long userId) {
        if (this.user == null) {
            this.user = new User();
        }
        this.user.setId(userId);
    }

    // Método para establecer el path de las imágenes
    public void setImagePath(String imagePath) {
        for (EventImage image : images) {
            image.setImageName(imagePath);
        }
    }

    // Método toString
    @Override
    public String toString() {
        return "Event{" + "id=" + id + ", title='" + title + '\'' + ", message='" + message + '\'' + ", creationDate="
                + creationDate + ", archived=" + archived + ", user=" + (user != null ? user.getId() : "null")
                + ", tags=" + tags + ", attendees=" + attendees + ", images=" + images + ", capacity=" + capacity + '}';
    }

    // Métodos auxiliares (opcional)
    public String getName() {
        return this.title;
    }

    public void setName(String name) {
        this.title = name;
    }

    public String getDescription() {
        return this.message;
    }

    public void setDescription(String description) {
        this.message = description;
    }

    public boolean getActive() {
        return !this.archived;
    }

    public void setActive(boolean active) {
        this.archived = !active;
    }

    public List<Attendee> getAttendeesList() {
        return this.attendees;
    }

    public void setAttendeesList(List<Attendee> attendees) {
        this.attendees = attendees;
    }

    public void addAttendeeToList(Attendee attendee) {
        if (this.attendees == null) {
            this.attendees = new ArrayList<>();
        }
        this.attendees.add(attendee);
    }

    public void removeAttendeeFromList(Attendee attendee) {
        if (this.attendees != null) {
            this.attendees.remove(attendee);
        }
    }

    // Getter y Setter para user
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getAvailableSeats() {
        // El número de plazas libres es el aforo menos los asistentes actuales
        return capacity - (attendees != null ? attendees.size() : 0);
    }

    public boolean hasAvailableSeats() {
        return getAvailableSeats() > 0;
    }

    public Set<EventLove> getEventLoves() {
        return eventLoves;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }
}
