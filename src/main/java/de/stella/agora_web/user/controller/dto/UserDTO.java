package de.stella.agora_web.user.controller.dto;

import de.stella.agora_web.user.model.User;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter  
@Builder
@Data
public class UserDTO { 
    private Long id; 
    private String username; 
  
    public static UserDTO from(User user) { 
        return UserDTO.builder()  // Corrected the reference to the builder 
                .id(user.getId()) 
                .username(user.getUsername()) 
                .build(); 
    } 
} 
// @Builder revisar esto es de mark ver cual es mas apropiado
// @Data
// public class UserDTO { 
//     private Long id; 
//     private String username; 
  
//     public static UserDTO from(User user) { 
//         return UserDTO.builder()  // Corrected the reference to the builder 
//                 .id(user.getId()) 
//                 .username(user.getUsername()) 
//                 .build(); 
//     } 