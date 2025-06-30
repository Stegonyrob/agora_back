package de.stella.agora_web.avatar.module;

import de.stella.agora_web.profiles.model.Profile;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
    // Para avatares personalizados: los datos binarios (soporte para archivos grandes)
    @Lob
    @Column(name = "image_data", columnDefinition = "LONGBLOB")
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

    // Relación con Profile (un avatar puede ser usado por muchos perfiles, pero esta referencia es solo informativa)
    @OneToOne(mappedBy = "avatar", fetch = FetchType.LAZY)
    private Profile profile;

    public Avatar(Long id, String imageName, byte[] imageData, boolean preloaded, boolean isDefault, String displayName,
            String imageUrl) {
        this.id = id;
        this.imageName = imageName;
        this.imageData = imageData;
        this.preloaded = preloaded;
        this.isDefault = isDefault;
        this.displayName = displayName;
        this.imageUrl = imageUrl;
    }

}
