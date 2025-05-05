package de.stella.agora_web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.stella.agora_web.auth.AuthController;
import de.stella.agora_web.auth.RefreshTokenDTO;
import de.stella.agora_web.jwt.TokenDTO;
import de.stella.agora_web.jwt.TokenGenerator;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TokenGenerator tokenGenerator;

  @MockBean
  private DaoAuthenticationProvider daoAuthenticationProvider;

  @MockBean
  private JwtAuthenticationProvider refreshTokenAuthProvider;

  @Test
  void login_should_return_ok_and_token_dto() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.post("/auth/login").contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"test\",\"password\":\"password\"}"))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  public void token_should_return_ok_and_token_dto() throws Exception {
    // given
    RefreshTokenDTO tokenDTO = new RefreshTokenDTO("token");
    Authentication authentication = mock(Authentication.class);
    when(refreshTokenAuthProvider.authenticate(new BearerTokenAuthenticationToken(tokenDTO.getRefreshToken())))
        .thenReturn(authentication);
    when(tokenGenerator.createToken(authentication)).thenReturn(new TokenDTO());

    // when
    String result = mockMvc
        .perform(MockMvcRequestBuilders.post("/all/token").contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(tokenDTO)))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();

    // then
    assertNotNull(result);
  }
}
