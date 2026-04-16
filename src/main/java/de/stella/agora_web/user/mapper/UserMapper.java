package de.stella.agora_web.user.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import de.stella.agora_web.banned.model.Banned;
import de.stella.agora_web.profiles.model.Profile;
import de.stella.agora_web.roles.model.Role;
import de.stella.agora_web.user.controller.dto.UserListDTO;
import de.stella.agora_web.user.model.User;

@Component
public class UserMapper {

    public UserListDTO toUserListDTO(User user) {
        if (user == null) {
            return null;
        }

        UserListDTO.UserListDTOBuilder dtoBuilder = UserListDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .acceptedRules(user.isAcceptedRules());

        // Mapear roles
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            String[] roleNames = user.getRoles().stream()
                    .map(Role::getName)
                    .toArray(String[]::new);
            dtoBuilder.roles(roleNames);
        } else {
            dtoBuilder.roles(new String[0]);
        }

        // Mapear información de ban
        if (user.getBanned() != null && !user.getBanned().isEmpty()) {
            // Buscar el ban más reciente
            Banned latestBan = user.getBanned().stream()
                    .reduce((ban1, ban2) -> ban1.getBanDate().after(ban2.getBanDate()) ? ban1 : ban2)
                    .orElse(null);

            if (latestBan != null) {
                dtoBuilder.isBanned(true);
                dtoBuilder.banReason(latestBan.getBanReason());
            } else {
                dtoBuilder.isBanned(false);
            }
        } else {
            dtoBuilder.isBanned(false);
        }

        // Mapear información del perfil
        Profile profile = user.getProfile();
        if (profile != null) {
            dtoBuilder.profileId(profile.getId()) // ✅ AGREGAR ESTO
                    .firstName(profile.getFirstName())
                    .lastName1(profile.getLastName1())
                    .lastName2(profile.getLastName2());

            // Mapear avatar
            if (profile.getAvatar() != null) {
                dtoBuilder.avatarId(profile.getAvatar().getId())
                        .avatarUrl(profile.getAvatar().getImageUrl())
                        .avatarDisplayName(profile.getAvatar().getDisplayName());
            }
        }

        return dtoBuilder.build();
    }

    public List<UserListDTO> toUserListDTOs(List<User> users) {
        if (users == null) {
            return List.of();
        }

        return users.stream()
                .map(this::toUserListDTO)
                .collect(Collectors.toList());
    }
}
