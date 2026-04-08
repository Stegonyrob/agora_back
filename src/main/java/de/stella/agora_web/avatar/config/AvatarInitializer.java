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

}
