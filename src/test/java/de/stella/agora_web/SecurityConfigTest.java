package de.stella.agora_web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
class SecurityConfigurationTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @WithMockUser(roles = "USER")
  void testUserAccess() throws Exception {
    mockMvc.perform(get("/api/all/me")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testAdminAccess() throws Exception {
    mockMvc.perform(get("/api/admin/me")).andExpect(status().isOk());
  }

  @Test
  void testAnonymousAccess() throws Exception {
    mockMvc.perform(get("/api/any/me")).andExpect(status().isOk());
  }

  @Test
  void testCorsConfiguration() throws Exception {
    mockMvc
      .perform(get("/api/all/me").header("Origin", "http://localhost:5173"))
      .andExpect(status().isOk())
      .andExpect(
        header().string("Access-Control-Allow-Origin", "http://localhost:5173")
      )
      .andExpect(header().string("Access-Control-Allow-Credentials", "true"));
  }
}
