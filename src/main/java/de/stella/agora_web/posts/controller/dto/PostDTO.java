package de.stella.agora_web.posts.controller.dto;

import java.time.LocalDateTime;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Builder
public class PostDTO<ReplyDTO, TagDTO> {
    
    private String title;
    private String message;
    private LocalDateTime creationDate;
    
    // private List<ReplyDTO> replies;
    // private TagDTO course;
    // private UserDTO user;


}
