package de.stella.agora_web.auth.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialLoginRequestDTO {

    private String idToken;      // Para Google
    private String accessToken;  // Para Facebook
    private String email;
    private String name;
    private String provider;     // "google" o "facebook"
}
