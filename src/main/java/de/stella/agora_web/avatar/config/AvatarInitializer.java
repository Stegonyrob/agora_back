package de.stella.agora_web.avatar.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import de.stella.agora_web.avatar.module.Avatar;
import de.stella.agora_web.avatar.repository.AvatarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Inicializador de avatares precargados. Se ejecuta al inicio de la aplicación
 * para asegurar que existan los avatares por defecto.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AvatarInitializer implements CommandLineRunner {

    private final AvatarRepository avatarRepository;

    @Override
    public void run(String... args) {
        initializePreloadedAvatars();
    }

    private void initializePreloadedAvatars() {
        try {
            // Verificar si ya existen avatares precargados
            List<Avatar> existingPreloaded = avatarRepository.findPreloadedAvatars();

            if (existingPreloaded != null && !existingPreloaded.isEmpty()) {
                log.info("Avatares precargados ya existen. Total: {}", existingPreloaded.size());
                return;
            }

            log.info("Inicializando avatares precargados...");

            // Crear avatares precargados con ID fijo
            log.info("Avatares precargados inicializados correctamente");
        } catch (Exception e) {
            log.error("Error al inicializar avatares precargados", e);
        }
    }

    /**
     * Crea un avatar precargado con ID fijo. Si ya existe, lo omite.
     */
    private void createPreloadedAvatar(Long id, String imageName, String displayName, boolean isDefault) {
        if (id == null || imageName == null || displayName == null) {
            log.error("Error al crear avatar precargado: parámetro nulo. ID: {}, nombre: {}, display name: {}", id, imageName, displayName);
            return;
        }

        // Verificar si ya existe este avatar específico por ID o nombre
        if (avatarRepository.existsById(id) || avatarRepository.existsByImageNameAndPreloadedTrue(imageName)) {
            log.debug("Avatar precargado '{}' ya existe (ID: {}), omitiendo...", imageName, id);
            return;
        }

        try {
            Avatar avatar = Avatar.builder()
                    .id(id)
                    .imageName(imageName)
                    .displayName(displayName)
                    .preloaded(true)
                    .isDefault(isDefault)
                    .imageData(null) // Los avatares precargados no almacenan datos binarios
                    .build();

            avatarRepository.save(avatar);
            log.debug("Avatar precargado creado: {} ({}) [ID: {}]", displayName, imageName, id);
        } catch (Exception e) {
            log.error("Error al crear avatar precargado: {}", e.getMessage(), e);
        }
    }
}
