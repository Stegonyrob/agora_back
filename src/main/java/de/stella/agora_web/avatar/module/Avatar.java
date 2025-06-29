package de.stella.agora_web.avatar.module;

import de.stella.agora_web.profiles.model.Profile;
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

    @Column(nullable = false)
    private String imageName;

    // Para avatares precargados: solo el nombre del archivo
    // Para avatares personalizados: los datos binarios
    @Lob
    @Column(name = "image_data")
    private byte[] imageData;

    // Si el avatar es precargado (archivo estático) o personalizado (base de datos)
    @Column(nullable = false)
    private boolean preloaded;

    // Si es el avatar por defecto del sistema
    @Column(nullable = false)
    private boolean isDefault;

    // Descripción o nombre amigable para mostrar en el selector
    private String displayName;

    private String imageUrl; // URL del avatar para uso en frontend

    // Relación con Profile (avatar belongs to profile, not user)
    @OneToOne(mappedBy = "avatar")
    private Profile profile;

}
