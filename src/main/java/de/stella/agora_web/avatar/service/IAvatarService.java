package de.stella.agora_web.avatar.service;

import java.util.List;

import de.stella.agora_web.avatar.controller.dto.AvatarDTO;
import de.stella.agora_web.avatar.dto.AvatarSelectorDTO;

public interface IAvatarService {

    AvatarDTO getAvatarById(Long id);

    AvatarDTO getAvatarByImageName(String imageName);

    List<AvatarDTO> getAllPreloadedAvatars();

    List<AvatarSelectorDTO> getPreloadedAvatarsForSelector();

    AvatarDTO getDefaultAvatar();

    AvatarDTO saveAvatar(AvatarDTO avatarDTO);

    AvatarDTO uploadCustomAvatar(String imageName, byte[] imageData, String displayName);

    void deleteAvatar(Long id);
}
