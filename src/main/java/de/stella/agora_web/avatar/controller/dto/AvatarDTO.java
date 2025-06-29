package de.stella.agora_web.avatar.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvatarDTO {

    private Long id;
    private String imageName;
    private byte[] imageData;
    private boolean preloaded;
    private boolean isDefault;
    private String displayName;
}
