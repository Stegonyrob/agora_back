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
        // Verificar si ya existen avatares precargados
        List<Avatar> existingPreloaded = avatarRepository.findPreloadedAvatars();

        if (!existingPreloaded.isEmpty()) {
            log.info("Avatares precargados ya existen. Total: {}", existingPreloaded.size());
            return;
        }

        log.info("Inicializando avatares precargados...");

        // Crear avatares precargados por defecto
        createPreloadedAvatar("default-avatar.png", "Avatar por Defecto", true);
        createPreloadedAvatar("avatar-student-1.png", "Estudiante 1", false);
        createPreloadedAvatar("avatar-student-2.png", "Estudiante 2", false);
        createPreloadedAvatar("avatar-student-3.png", "Estudiante 3", false);
        createPreloadedAvatar("avatar-student-4.png", "Estudiante 4", false);
        createPreloadedAvatar("avatar-teacher-1.png", "Profesor 1", false);
        createPreloadedAvatar("avatar-teacher-2.png", "Profesor 2", false);
        createPreloadedAvatar("avatar-teacher-3.png", "Profesor 3", false);
        createPreloadedAvatar("avatar-professional-1.png", "Profesional 1", false);
        createPreloadedAvatar("avatar-professional-2.png", "Profesional 2", false);
        createPreloadedAvatar("avatar-casual-1.png", "Casual 1", false);
        createPreloadedAvatar("avatar-casual-2.png", "Casual 2", false);
        createPreloadedAvatar("avatar-animal-1.png", "Mascota 1", false);
        createPreloadedAvatar("avatar-animal-2.png", "Mascota 2", false);
        createPreloadedAvatar("avatar-fantasy-1.png", "Fantasía 1", false);
        createPreloadedAvatar("avatar-fantasy-2.png", "Fantasía 2", false);

        log.info("Avatares precargados inicializados correctamente");
    }

    private void createPreloadedAvatar(String imageName, String displayName, boolean isDefault) {
        // Verificar si ya existe este avatar específico
        if (avatarRepository.existsByImageNameAndPreloadedTrue(imageName)) {
            log.debug("Avatar precargado '{}' ya existe, omitiendo...", imageName);
            return;
        }

        Avatar avatar = Avatar.builder()
                .imageName(imageName)
                .displayName(displayName)
                .preloaded(true)
                .isDefault(isDefault)
                .imageData(null) // Los avatares precargados no almacenan datos binarios
                .build();

        avatarRepository.save(avatar);
        log.debug("Avatar precargado creado: {} ({})", displayName, imageName);
    }
}
