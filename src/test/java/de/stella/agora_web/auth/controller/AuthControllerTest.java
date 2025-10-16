package de.stella.agora_web.auth.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.stella.agora_web.auth.LoginDTO;
import de.stella.agora_web.config.TestConfig;
import de.stella.agora_web.jwt.TokenDTO;
import de.stella.agora_web.jwt.TokenGenerator;

/**
 * Tests para AuthController - Autenticación y generación de tokens JWT
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
@TestPropertySource(properties = {
    "spring.h2.console.enabled=false",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Import(TestConfig.class)
@Transactional
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${api-endpoint}")
    private String apiEndpoint;

    @MockBean
    private TokenGenerator tokenGenerator;

    @MockBean
    private DaoAuthenticationProvider daoAuthenticationProvider;

    @MockBean
    private JwtAuthenticationProvider refreshTokenAuthProvider;

    private LoginDTO validLoginDTO;
    private TokenDTO mockTokenDTO;

    @BeforeEach
    void setUp() {
        // Configurar LoginDTO válido
        validLoginDTO = new LoginDTO("testuser", "password123", null);

        // Configurar TokenDTO mock
        mockTokenDTO = new TokenDTO();
        mockTokenDTO.setUserId(1L);
        mockTokenDTO.setAccessToken("mock-access-token");
        mockTokenDTO.setRefreshToken("mock-refresh-token");
    }

    // ============ TESTS LOGIN ENDPOINT ============
    @Test
    void testLogin_WithValidCredentials_ShouldReturn200WithTokens() throws Exception {
        // Configurar mocks
        Authentication mockAuth = org.mockito.Mockito.mock(Authentication.class);
        when(daoAuthenticationProvider.authenticate(any())).thenReturn(mockAuth);
        when(tokenGenerator.createToken(mockAuth)).thenReturn(mockTokenDTO);

        mockMvc.perform(post(apiEndpoint + "/all/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLoginDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId", notNullValue()))
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andExpect(jsonPath("$.refreshToken", notNullValue()));
    }

    @Test
    void testLogin_WithInvalidCredentials_ShouldReturn401() throws Exception {
        // Configurar mock para credenciales inválidas
        when(daoAuthenticationProvider.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        LoginDTO invalidLoginDTO = new LoginDTO("invalid", "wrong", null);

        mockMvc.perform(post(apiEndpoint + "/all/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidLoginDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLogin_WithEmptyCredentials_ShouldReturn400() throws Exception {
        LoginDTO emptyLoginDTO = new LoginDTO("", "", null);

        mockMvc.perform(post(apiEndpoint + "/all/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyLoginDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLogin_WithNullUsername_ShouldReturn400() throws Exception {
        LoginDTO nullUsernameDTO = new LoginDTO(null, "password", null);

        mockMvc.perform(post(apiEndpoint + "/all/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nullUsernameDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLogin_WithNullPassword_ShouldReturn400() throws Exception {
        LoginDTO nullPasswordDTO = new LoginDTO("username", null, null);

        mockMvc.perform(post(apiEndpoint + "/all/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nullPasswordDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLogin_WithMalformedJSON_ShouldReturn400() throws Exception {
        String malformedJson = "{\"username\":\"test\",\"password\":}";

        mockMvc.perform(post(apiEndpoint + "/all/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(malformedJson))
                .andExpect(status().isBadRequest());
    }

    // ============ TESTS TOKEN REFRESH ENDPOINT ============
    @Test
    void testRefreshToken_WithValidToken_ShouldReturn200WithNewTokens() throws Exception {
        // Configurar token DTO para refresh
        TokenDTO refreshTokenDTO = new TokenDTO();
        refreshTokenDTO.setRefreshToken("valid-refresh-token");

        // Configurar mocks
        Authentication mockAuth = org.mockito.Mockito.mock(Authentication.class);
        when(refreshTokenAuthProvider.authenticate(any())).thenReturn(mockAuth);
        when(tokenGenerator.createToken(mockAuth)).thenReturn(mockTokenDTO);

        mockMvc.perform(post(apiEndpoint + "/all/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshTokenDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId", notNullValue()))
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andExpect(jsonPath("$.refreshToken", notNullValue()));
    }

    @Test
    void testRefreshToken_WithInvalidToken_ShouldReturn401() throws Exception {
        TokenDTO invalidTokenDTO = new TokenDTO();
        invalidTokenDTO.setRefreshToken("invalid-refresh-token");

        when(refreshTokenAuthProvider.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid refresh token"));

        mockMvc.perform(post(apiEndpoint + "/all/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidTokenDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testRefreshToken_WithExpiredToken_ShouldReturn401() throws Exception {
        TokenDTO expiredTokenDTO = new TokenDTO();
        expiredTokenDTO.setRefreshToken("expired-refresh-token");

        when(refreshTokenAuthProvider.authenticate(any()))
                .thenThrow(new org.springframework.security.oauth2.core.OAuth2AuthenticationException("Token expired"));

        mockMvc.perform(post(apiEndpoint + "/all/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(expiredTokenDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testRefreshToken_WithEmptyToken_ShouldReturn400() throws Exception {
        TokenDTO emptyTokenDTO = new TokenDTO();
        emptyTokenDTO.setRefreshToken("");

        mockMvc.perform(post(apiEndpoint + "/all/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyTokenDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRefreshToken_WithNullToken_ShouldReturn400() throws Exception {
        TokenDTO nullTokenDTO = new TokenDTO();
        nullTokenDTO.setRefreshToken(null);

        mockMvc.perform(post(apiEndpoint + "/all/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nullTokenDTO)))
                .andExpect(status().isBadRequest());
    }

    // ============ TESTS INTEGRATION SCENARIOS ============
    @Test
    void testLoginAndRefreshFlow_ShouldWork() throws Exception {
        // Test login primero
        Authentication mockAuth = org.mockito.Mockito.mock(Authentication.class);
        when(daoAuthenticationProvider.authenticate(any())).thenReturn(mockAuth);
        when(tokenGenerator.createToken(mockAuth)).thenReturn(mockTokenDTO);

        // Login exitoso
        mockMvc.perform(post(apiEndpoint + "/all/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLoginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.refreshToken", notNullValue()));

        // Ahora test refresh token
        TokenDTO refreshTokenDTO = new TokenDTO();
        refreshTokenDTO.setRefreshToken(mockTokenDTO.getRefreshToken());

        when(refreshTokenAuthProvider.authenticate(any())).thenReturn(mockAuth);
        when(tokenGenerator.createToken(mockAuth)).thenReturn(mockTokenDTO);

        mockMvc.perform(post(apiEndpoint + "/all/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshTokenDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", notNullValue()));
    }

    // ============ TESTS SECURITY SCENARIOS ============
    @Test
    void testLogin_WithSQLInjectionAttempt_ShouldBeHandledSafely() throws Exception {
        LoginDTO sqlInjectionDTO = new LoginDTO("admin'; DROP TABLE users; --", "password", null);

        when(daoAuthenticationProvider.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        mockMvc.perform(post(apiEndpoint + "/all/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sqlInjectionDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLogin_WithVeryLongPassword_ShouldBeHandledSafely() throws Exception {
        String longPassword = "a".repeat(10000);
        LoginDTO longPasswordDTO = new LoginDTO("testuser", longPassword, longPassword);

        when(daoAuthenticationProvider.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        mockMvc.perform(post(apiEndpoint + "/all/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(longPasswordDTO)))
                .andExpect(status().isUnauthorized());
    }

    // ============ TESTS ERROR HANDLING ============
    @Test
    void testLogin_WhenServiceThrowsException_ShouldReturn500() throws Exception {
        when(daoAuthenticationProvider.authenticate(any()))
                .thenThrow(new RuntimeException("Database connection failed"));

        mockMvc.perform(post(apiEndpoint + "/all/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLoginDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testRefreshToken_WhenTokenGeneratorFails_ShouldReturn500() throws Exception {
        Authentication mockAuth = org.mockito.Mockito.mock(Authentication.class);
        when(refreshTokenAuthProvider.authenticate(any())).thenReturn(mockAuth);
        when(tokenGenerator.createToken(mockAuth))
                .thenThrow(new RuntimeException("Token generation failed"));

        TokenDTO refreshTokenDTO = new TokenDTO();
        refreshTokenDTO.setRefreshToken("valid-refresh-token");

        mockMvc.perform(post(apiEndpoint + "/all/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshTokenDTO)))
                .andExpect(status().isInternalServerError());
    }
}
