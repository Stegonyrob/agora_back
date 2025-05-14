package de.stella.agora_web.avatar.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import de.stella.agora_web.avatar.controller.dto.AvatarDTO;
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
        return avatarRepository.findByPreloadedTrue().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public AvatarDTO saveAvatar(AvatarDTO avatarDTO) {
        Avatar avatar = toEntity(avatarDTO);
        avatar = avatarRepository.save(avatar);
        return toDTO(avatar);
    }

    @Override
    public void deleteAvatar(Long id) {
        if (!avatarRepository.existsById(id)) {
            throw new AvatarNotFoundException("Avatar not found with id: " + id);
        }
        avatarRepository.deleteById(id);
    }

    private AvatarDTO toDTO(Avatar avatar) {
        return AvatarDTO.builder().id(avatar.getId()).imageName(avatar.getImageName()).imageData(avatar.getImageData())
                .preloaded(avatar.isPreloaded()).build();
    }

    private Avatar toEntity(AvatarDTO dto) {
        return Avatar.builder().id(dto.getId()).imageName(dto.getImageName()).imageData(dto.getImageData())
                .preloaded(dto.isPreloaded()).build();
    }
}