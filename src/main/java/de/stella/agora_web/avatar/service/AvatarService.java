package de.stella.agora_web.avatar.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import de.stella.agora_web.avatar.dto.AvatarSelectorDTO;
import de.stella.agora_web.avatar.module.Avatar;
import de.stella.agora_web.avatar.repository.AvatarRepository;
import de.stella.agora_web.profiles.model.Profile;
import de.stella.agora_web.profiles.repository.ProfileRepository;

@Service
@Transactional
public class AvatarService {

    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);

    @Autowired
    private AvatarRepository avatarRepository;

    @Autowired
    private ProfileRepository profileRepository;

    /**
     * Obtiene todos los avatares precargados para el selector
     */
    @Transactional(readOnly = true)
    public List<AvatarSelectorDTO> getPreloadedAvatarsForSelector() {
        logger.info("Obteniendo avatares precargados para selector");
        return avatarRepository.findPreloadedAvatars().stream()
                .map(avatar -> new AvatarSelectorDTO(avatar.getId(), avatar.getImageName(), avatar.getDisplayName()))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene el avatar por defecto del sistema
     */
    @Transactional(readOnly = true)
    public Optional<Avatar> getDefaultAvatar() {
        return avatarRepository.findDefaultAvatar();
    }

    /**
     * Asigna un avatar precargado a un perfil
     */
    public boolean assignPreloadedAvatar(Long profileId, String imageName) {
        logger.info("Asignando avatar precargado {} al perfil {}", imageName, profileId);

        // Verificar que el avatar precargado existe
        Optional<Avatar> avatarOpt = avatarRepository.findByImageName(imageName);
        if (avatarOpt.isEmpty() || !avatarOpt.get().isPreloaded()) {
            logger.warn("Avatar precargado no encontrado: {}", imageName);
            return false;
        }

        // Verificar que el perfil existe
        Optional<Profile> profileOpt = profileRepository.findById(profileId);
        if (profileOpt.isEmpty()) {
            logger.warn("Perfil no encontrado: {}", profileId);
            return false;
        }

        Profile profile = profileOpt.get();
        Avatar avatar = avatarOpt.get();

        // Asignar el avatar al perfil
        profile.setAvatar(avatar);
        profileRepository.save(profile);

        logger.info("Avatar {} asignado exitosamente al perfil {}", imageName, profileId);
        return true;
    }

    /**
     * Sube un avatar personalizado para un perfil
     */
    public Avatar uploadCustomAvatar(Long profileId, MultipartFile file) throws IOException {
        logger.info("Subiendo avatar personalizado para perfil {}", profileId);

        // Verificar que el perfil existe
        Optional<Profile> profileOpt = profileRepository.findById(profileId);
        if (profileOpt.isEmpty()) {
            throw new IllegalArgumentException("Perfil no encontrado: " + profileId);
        }

        Profile profile = profileOpt.get();

        // Validar el archivo
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }

        // Validar tipo de archivo (solo imágenes)
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Solo se permiten archivos de imagen");
        }

        // Crear avatar personalizado
        Avatar customAvatar = Avatar.builder()
                .imageName("custom_" + profileId + "_" + System.currentTimeMillis())
                .imageData(file.getBytes())
                .preloaded(false)
                .isDefault(false)
                .displayName("Avatar personalizado")
                .build();

        // Guardar avatar
        customAvatar = avatarRepository.save(customAvatar);

        // Asignar al perfil
        profile.setAvatar(customAvatar);
        profileRepository.save(profile);

        logger.info("Avatar personalizado creado y asignado al perfil {}", profileId);
        return customAvatar;
    }

    /**
     * Obtiene los datos de imagen de un avatar por ID
     */
    @Transactional(readOnly = true)
    public Optional<byte[]> getAvatarImageData(Long avatarId) {
        Optional<Avatar> avatarOpt = avatarRepository.findById(avatarId);
        if (avatarOpt.isPresent() && !avatarOpt.get().isPreloaded()) {
            return Optional.of(avatarOpt.get().getImageData());
        }
        return Optional.empty();
    }

    /**
     * Asigna el avatar por defecto a un perfil
     */
    public void assignDefaultAvatar(Long profileId) {
        logger.info("Asignando avatar por defecto al perfil {}", profileId);

        Optional<Profile> profileOpt = profileRepository.findById(profileId);
        Optional<Avatar> defaultAvatarOpt = getDefaultAvatar();

        if (profileOpt.isPresent() && defaultAvatarOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setAvatar(defaultAvatarOpt.get());
            profileRepository.save(profile);
            logger.info("Avatar por defecto asignado al perfil {}", profileId);
        } else {
            logger.warn("No se pudo asignar avatar por defecto al perfil {}", profileId);
        }
    }

    /**
     * Inicializa los avatares precargados en la base de datos
     */
    @Transactional
    public void initializePreloadedAvatars() {
        logger.info("Inicializando avatares precargados");

        // Lista de avatares precargados (estos deben coincidir con los archivos en public/avatars)
        String[] preloadedAvatars = {
            "avatar1.png", "avatar2.png", "avatar3.png", "avatar4.png",
            "avatar5.png", "avatar6.png", "avatar7.png", "avatar8.png",
            "avatar9.png", "avatar10.png", "avatar11.png", "avatar12.png"
        };

        for (int i = 0; i < preloadedAvatars.length; i++) {
            String imageName = preloadedAvatars[i];

            // Solo crear si no existe
            if (!avatarRepository.existsByImageNameAndPreloadedTrue(imageName)) {
                Avatar avatar = Avatar.builder()
                        .imageName(imageName)
                        .imageData(null) // Los precargados no tienen datos en DB
                        .preloaded(true)
                        .isDefault(i == 0) // El primero es por defecto
                        .displayName("Avatar " + (i + 1))
                        .build();

                avatarRepository.save(avatar);
                logger.info("Avatar precargado creado: {}", imageName);
            }
        }

        logger.info("Inicialización de avatares precargados completada");
    }
}
