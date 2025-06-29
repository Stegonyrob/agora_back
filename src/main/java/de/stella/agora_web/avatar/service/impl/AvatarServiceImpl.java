package de.stella.agora_web.avatar.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import de.stella.agora_web.avatar.controller.dto.AvatarDTO;
import de.stella.agora_web.avatar.dto.AvatarSelectorDTO;
import de.stella.agora_web.avatar.exception.AvatarNotFoundException;
import de.stella.agora_web.avatar.module.Avatar;
import de.stella.agora_web.avatar.repository.AvatarRepository;
import de.stella.agora_web.avatar.service.IAvatarService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AvatarServiceImpl implements IAvatarService {

    private final AvatarRepository avatarRepository;

    @Override
    public AvatarDTO getAvatarById(Long id) {
        Avatar avatar = avatarRepository.findById(id)
                .orElseThrow(() -> new AvatarNotFoundException("Avatar not found with id: " + id));
        return toDTO(avatar);
    }

    @Override
    public AvatarDTO getAvatarByImageName(String imageName) {
        Avatar avatar = avatarRepository.findByImageName(imageName)
                .orElseThrow(() -> new AvatarNotFoundException("Avatar not found with name: " + imageName));
        return toDTO(avatar);
    }

    @Override
    public List<AvatarDTO> getAllPreloadedAvatars() {
        return avatarRepository.findPreloadedAvatars().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AvatarSelectorDTO> getPreloadedAvatarsForSelector() {
        return avatarRepository.findPreloadedAvatarsForSelector();
    }

    @Override
    public AvatarDTO getDefaultAvatar() {
        Avatar avatar = avatarRepository.findDefaultAvatar()
                .orElseThrow(() -> new AvatarNotFoundException("Default avatar not found"));
        return toDTO(avatar);
    }

    @Override
    public AvatarDTO saveAvatar(AvatarDTO avatarDTO) {
        Avatar avatar = toEntity(avatarDTO);
        avatar = avatarRepository.save(avatar);
        return toDTO(avatar);
    }

    @Override
    public AvatarDTO uploadCustomAvatar(String imageName, byte[] imageData, String displayName) {
        // Validar tamaño de la imagen
        if (imageData.length > 2 * 1024 * 1024) { // 2MB
            throw new IllegalArgumentException("Avatar file size exceeds maximum allowed (2MB)");
        }

        Avatar avatar = Avatar.builder()
                .imageName(imageName)
                .imageData(imageData)
                .displayName(displayName)
                .preloaded(false)
                .isDefault(false)
                .build();

        avatar = avatarRepository.save(avatar);
        return toDTO(avatar);
    }

    @Override
    public void deleteAvatar(Long id) {
        Avatar avatar = avatarRepository.findById(id)
                .orElseThrow(() -> new AvatarNotFoundException("Avatar not found with id: " + id));

        // No permitir eliminar el avatar por defecto
        if (avatar.isDefault()) {
            throw new IllegalArgumentException("Cannot delete default avatar");
        }

        // No permitir eliminar avatares precargados
        if (avatar.isPreloaded()) {
            throw new IllegalArgumentException("Cannot delete preloaded avatar");
        }

        avatarRepository.deleteById(id);
    }

    private AvatarDTO toDTO(Avatar avatar) {
        return AvatarDTO.builder()
                .id(avatar.getId())
                .imageName(avatar.getImageName())
                .imageData(avatar.getImageData())
                .preloaded(avatar.isPreloaded())
                .isDefault(avatar.isDefault())
                .displayName(avatar.getDisplayName())
                .build();
    }

    private Avatar toEntity(AvatarDTO dto) {
        return Avatar.builder()
                .id(dto.getId())
                .imageName(dto.getImageName())
                .imageData(dto.getImageData())
                .preloaded(dto.isPreloaded())
                .isDefault(dto.isDefault())
                .displayName(dto.getDisplayName())
                .build();
    }
}
