package de.stella.agora_web.events.controller.dto;

import java.time.LocalDate;
import java.util.List;

import de.stella.agora_web.attendee.controller.dto.AttendeeDTO;
import de.stella.agora_web.tags.dto.TagSummaryDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EventDTO {

    private Long id;
    @NotBlank(message = "Title cannot be blank")
    @Size(max = 100, message = "Title must be less than 100 characters")
    private String title;
    @NotBlank(message = "Message cannot be blank")
    @Size(max = 300, message = "Message must be less than 300 characters")
    private String message;
    private boolean archived;
    private List<AttendeeDTO> attendees;
    private int capacity;
    private int attendeesCount;
    private List<TagSummaryDTO> tags;
    private java.time.LocalDateTime creationDate;
    @NotBlank(message = "Event date cannot be blank")
    private LocalDate eventDate;
    private String eventTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public List<AttendeeDTO> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<AttendeeDTO> attendees) {
        this.attendees = attendees;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getAttendeesCount() {
        return attendeesCount;
    }

    public void setAttendeesCount(int attendeesCount) {
        this.attendeesCount = attendeesCount;
    }

    public List<TagSummaryDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagSummaryDTO> tags) {
        this.tags = tags;
    }

    public java.time.LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(java.time.LocalDateTime creationDate) {
        this.creationDate = creationDate;
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

    public EventDTO() {
    }

    public EventDTO(Long id, String title, String message, boolean archived, List<AttendeeDTO> attendees, int capacity,
            int attendeesCount, List<TagSummaryDTO> tags, java.time.LocalDateTime creationDate, LocalDate eventDate, String eventTime) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.archived = archived;
        this.attendees = attendees;
        this.capacity = capacity;
        this.attendeesCount = attendeesCount;
        this.tags = tags;
        this.creationDate = creationDate;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
    }
}
