package de.stella.agora_web.image.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * Configuración para el manejo de imágenes en la aplicación
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app.images")
public class ImageConfig {

    /**
     * Ruta base donde se almacenan las imágenes estáticas
     */
    private String staticPath = "temp_images";

    /**
     * Tamaño máximo permitido para archivos de imagen en bytes (por defecto
     * 5MB)
     */
    private long maxFileSize = 5 * 1024 * 1024; // 5MB

    /**
     * Tipos MIME permitidos para imágenes
     */
    private String[] allowedMimeTypes = {
        "image/jpeg",
        "image/jpg",
        "image/png",
        "image/gif",
        "image/webp",
        "image/svg+xml"
    };

    /**
     * Extensiones de archivo permitidas
     */
    private String[] allowedExtensions = {
        "jpg",
        "jpeg",
        "png",
        "gif",
        "webp",
        "svg"
    };

    /**
     * Indica si se deben usar URLs estáticas o endpoints dinámicos
     */
    private boolean useStaticUrls = true;

    /**
     * Prefijo para URLs de imágenes estáticas
     */
    private String staticUrlPrefix = "/api/v1/images/temp_images";

    /**
     * Indica si se deben comprimir las imágenes al subirlas
     */
    private boolean compressImages = true;

    /**
     * Calidad de compresión JPEG (0.0 - 1.0)
     */
    private float jpegQuality = 0.85f;

    /**
     * Ancho máximo para redimensionar imágenes (0 = no redimensionar)
     */
    private int maxWidth = 1920;

    /**
     * Alto máximo para redimensionar imágenes (0 = no redimensionar)
     */
    private int maxHeight = 1080;
}
