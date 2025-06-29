package de.stella.agora_web.avatar.config;

import java.util.Set;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * Configuración para la gestión de avatares.
 */
@Configuration
@ConfigurationProperties(prefix = "app.avatar")
@Data
public class AvatarConfig {

    /**
     * Tamaño máximo permitido para archivos de avatar en bytes (por defecto
     * 2MB)
     */
    private long maxFileSize = 2 * 1024 * 1024; // 2MB

    /**
     * Tipos MIME permitidos para avatares
     */
    private Set<String> allowedMimeTypes = Set.of(
            "image/jpeg",
            "image/jpg",
            "image/png",
            "image/gif",
            "image/webp"
    );

    /**
     * Extensiones de archivo permitidas
     */
    private Set<String> allowedExtensions = Set.of(
            "jpg",
            "jpeg",
            "png",
            "gif",
            "webp"
    );

    /**
     * Directorio donde se almacenan los avatares estáticos en el frontend
     */
    private String staticAvatarPath = "/avatars/static/";

    /**
     * Prefijo para nombres de avatares personalizados
     */
    private String customAvatarPrefix = "custom_avatar_";
}
