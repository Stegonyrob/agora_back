package de.stella.agora_web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.stella.agora_web.auth.AuthController;
import de.stella.agora_web.auth.LoginDTO;
import de.stella.agora_web.auth.RefreshTokenDTO;
import de.stella.agora_web.jwt.TokenDTO;
import de.stella.agora_web.jwt.TokenGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
public class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TokenGenerator tokenGenerator;

  @MockBean
  private DaoAuthenticationProvider daoAuthenticationProvider;

  @MockBean
  private JwtAuthenticationProvider refreshTokenAuthProvider;

  @Test
  public void login_should_return_ok_and_token_dto() throws Exception {
    // given
    LoginDTO loginDTO = new LoginDTO("user", "password");
    Authentication authentication = mock(Authentication.class);
    when(
      daoAuthenticationProvider.authenticate(
        UsernamePasswordAuthenticationToken.unauthenticated(
          loginDTO.getUsername(),
          loginDTO.getPassword()
        )
      )
    )
      .thenReturn(authentication);
    when(tokenGenerator.createToken(authentication)).thenReturn(new TokenDTO());

    // when
    String result = mockMvc
      .perform(
        MockMvcRequestBuilders
          .post("/all/login")
          .contentType(MediaType.APPLICATION_JSON)
          .content(new ObjectMapper().writeValueAsString(loginDTO))
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andReturn()
      .getResponse()
      .getContentAsString();

    // then
    assertNotNull(result);
  }

  @Test
  public void token_should_return_ok_and_token_dto() throws Exception {
    // given
    RefreshTokenDTO tokenDTO = new RefreshTokenDTO("token");
    Authentication authentication = mock(Authentication.class);
    when(
      refreshTokenAuthProvider.authenticate(
        new BearerTokenAuthenticationToken(tokenDTO.getRefreshToken())
      )
    )
      .thenReturn(authentication);
    when(tokenGenerator.createToken(authentication)).thenReturn(new TokenDTO());

    // when
    String result = mockMvc
      .perform(
        MockMvcRequestBuilders
          .post("/all/token")
          .contentType(MediaType.APPLICATION_JSON)
          .content(new ObjectMapper().writeValueAsString(tokenDTO))
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andReturn()
      .getResponse()
      .getContentAsString();

    // then
    assertNotNull(result);
  }
}
