package de.stella.agora_web.avatar.module;

import de.stella.agora_web.profiles.model.Profile;
import de.stella.agora_web.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "avatars")
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String imageName;

    @Lob
    @Column(nullable = false)
    private byte[] imageData;

    // Si el avatar es precargado o personalizado
    @Column(nullable = false)
    private boolean preloaded;

    // Relación con User (opcional)
    @OneToOne(mappedBy = "avatar")
    private User user;

    // Relación con Profile (opcional)
    @OneToOne(mappedBy = "avatar")
    private Profile profile;
}