package de.stella.agora_web.avatar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import de.stella.agora_web.avatar.module.Avatar;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Long> {

    // Buscar avatar por nombre de imagen (tanto precargados como personalizados)
    Optional<Avatar> findByImageName(String imageName);

    // Obtener todos los avatares precargados (para el selector)
    @Query("SELECT a FROM Avatar a WHERE a.preloaded = true ORDER BY a.displayName")
    List<Avatar> findPreloadedAvatars();

    // Obtener el avatar por defecto
    @Query("SELECT a FROM Avatar a WHERE a.isDefault = true AND a.preloaded = true")
    Optional<Avatar> findDefaultAvatar();

    // Verificar si existe un avatar precargado por nombre
    boolean existsByImageNameAndPreloadedTrue(String imageName);

}
