package de.stella.agora_web.favorite.controller.dto;

import de.stella.agora_web.posts.controller.dto.PostDTO;
import de.stella.agora_web.profiles.controller.dto.ProfileDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteDTO {

    private Long id;

    private ProfileDTO profile;

    private PostDTO post;

}