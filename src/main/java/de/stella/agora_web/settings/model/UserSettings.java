package de.stella.agora_web.settings.model;

import de.stella.agora_web.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_settings")
@Getter
@Setter
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    private Integer fontSize;
    private Boolean highContrast;
    private Boolean animations;
    private Boolean daltonic;
    private Boolean showPersonalInfo;
    private Boolean twoFA;

    @Column(columnDefinition = "TEXT")
    private String socialLinks; // Puedes guardar como JSON string

    // ...constructores, getters y setters...
    public UserSettings() {
        // Constructor por defecto
    }

}
