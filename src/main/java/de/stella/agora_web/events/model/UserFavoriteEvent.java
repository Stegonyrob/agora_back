// UserFavoriteEvent.java
package de.stella.agora_web.events.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_favorite_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserFavoriteEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long eventId;

    public UserFavoriteEvent(Long userId, Long eventId) {
        this.userId = userId;
        this.eventId = eventId;
    }
}
