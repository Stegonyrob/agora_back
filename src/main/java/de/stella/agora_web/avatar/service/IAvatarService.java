package de.stella.agora_web.avatar.service;

import java.util.List;

import de.stella.agora_web.avatar.controller.dto.AvatarDTO;

public interface IAvatarService {
    AvatarDTO getAvatarById(Long id);

    AvatarDTO getAvatarByImageName(String imageName);

    List<AvatarDTO> getAllPreloadedAvatars();

    AvatarDTO saveAvatar(AvatarDTO avatarDTO);

    void deleteAvatar(Long id);
}