package de.agora.agora.service;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

/**
 * Servicio para cargar imágenes condicionalmente
 *
 * OPCIÓN 1: BLOB + Migración Automática - En desarrollo: app.load-images=false
 * (startup rápido) - En testing/staging: app.load-images=true (datos completos)
 * - En producción: Migración automática a S3
 */
@Service
public class ImageLoaderService implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ImageLoaderService.class);

    private final JdbcTemplate jdbcTemplate;

    @Value("${app.load-images:false}")
    private boolean loadImages;

    @Value("${app.environment:dev}")
    private String environment;

    public ImageLoaderService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        if (loadImages) {
            loadImageData();
        }
    }

    private void loadImageData() {
        try {
            log.info("Cargando imágenes BLOB para entorno: {}", environment);

            // Verificar si ya existen imágenes
            Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM text_images", Integer.class);

            if (count != null && count > 0) {
                log.info("Imágenes ya cargadas ({} encontradas)", count);
                return;
            }

            // Cargar script de imágenes
            ClassPathResource resource = new ClassPathResource("data-images.sql");
            String sql = FileCopyUtils.copyToString(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));

            // Ejecutar script
            jdbcTemplate.execute(sql);

            log.info("Imágenes BLOB cargadas exitosamente");

        } catch (Exception e) {
            log.error("Error cargando imágenes: {}", e.getMessage(), e);
        }
    }
}
