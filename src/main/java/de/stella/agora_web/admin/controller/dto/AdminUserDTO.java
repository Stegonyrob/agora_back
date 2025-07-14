package de.stella.agora_web.admin.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminUserDTO {

    private Long id;
    private String username;
    private String email;
    private boolean admin;
}
