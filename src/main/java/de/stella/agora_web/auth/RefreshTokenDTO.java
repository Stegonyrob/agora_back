package de.stella.agora_web.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenDTO {

  public RefreshTokenDTO(String token) {}

  private String refreshToken;
}
