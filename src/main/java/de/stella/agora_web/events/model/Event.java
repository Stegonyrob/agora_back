package de.stella.agora_web.events.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;

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

    @Column(name = "archived")
    private boolean archived;

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

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventImage> images = new ArrayList<>();

    // Constructor vacío
    public Event() {
    }

    // Constructor con parámetros
    public Event(Long id, String title, String message, Long userId, boolean archived) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.user = new User();
        this.user.setId(userId);
        this.archived = archived;
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

    // Getter y Setter para user
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
        image.setEvent(this); // Asociar la imagen con este evento
    }

    public void removeImage(EventImage image) {
        this.images.remove(image);
        image.setEvent(null); // Desvincular la imagen del evento
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
            image.setImageName(imagePath); // Cambiar el atributo según corresponda
        }
    }

    // Método toString
    @Override
    public String toString() {
        return "Event{" + "id=" + id + ", title='" + title + '\'' + ", message='" + message + '\'' + ", creationDate="
                + creationDate + ", archived=" + archived + ", user=" + (user != null ? user.getId() : "null")
                + ", tags=" + tags + ", attendees=" + attendees + ", images=" + images + '}';
    }
}