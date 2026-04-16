package de.stella.agora_web.events.exceptions;

public class EventNotFoundException extends EventException {
    public EventNotFoundException(String message) {
        super(message);
    }

    public EventNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventNotFoundException(Long eventId) {
        super("Event with ID " + eventId + " not found");
    }
}