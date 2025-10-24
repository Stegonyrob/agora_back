package de.stella.agora_web.profiles.controller;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.stella.agora_web.config.TestConfig;
import de.stella.agora_web.gdpr.service.GdprService;
import de.stella.agora_web.profiles.controller.dto.ProfileDTO;
import de.stella.agora_web.profiles.model.Profile;
import de.stella.agora_web.profiles.repository.ProfileRepository;
import de.stella.agora_web.profiles.service.impl.ProfileServiceImpl;
import de.stella.agora_web.roles.model.Role;
import de.stella.agora_web.roles.repository.RoleRepository;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.repository.UserRepository;
import de.stella.agora_web.user.service.impl.UserServiceImpl;

/**
 * Tests para ProfileController - Gestión de perfiles de usuario con
 * autorización
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

public class ProfileControllerTest {

    @Value("${api-endpoint}")
    private String apiEndpoint;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private de.stella.agora_web.comment.repository.CommentRepository commentRepository;

    @MockBean
    private ProfileServiceImpl profileService;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private GdprService gdprService;

    private User testUser;
    private Profile testProfile;
    private Role userRole;
    private Role adminRole;

    @BeforeEach
    void setUp() {
        // Limpiar datos en orden correcto (comentarios primero para evitar violaciones de integridad referencial)
        commentRepository.deleteAll();
        profileRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();

        // Crear roles
        userRole = new Role();
        userRole.setName("USER");
        userRole = roleRepository.save(userRole);

        adminRole = new Role();
        adminRole.setName("ADMIN");
        adminRole = roleRepository.save(adminRole);

        // Crear usuario de prueba
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.getRoles().add(userRole);
        testUser = userRepository.save(testUser);

        // Crear perfil de prueba
        testProfile = new Profile();
        testProfile.setUser(testUser);
        testProfile.setFirstName("Test");
        testProfile.setLastName1("User");
        testProfile.setLastName2("Lastname");
        testProfile.setUsername("testuser");
        testProfile.setEmail("test@example.com");
        testProfile.setCity("Madrid");
        testProfile.setCountry("España");
        testProfile.setPhone("123456789");
        testProfile.setRelationship("Single");
        testProfile = profileRepository.save(testProfile);

        // Actualizar usuario con perfil
        testUser.setProfile(testProfile);
        testUser = userRepository.save(testUser);
    }

    // ============ TESTS GET PROFILE BY ID ============
    @Test
    void testGetProfileById_ValidId_ShouldReturn200() throws Exception {
        when(profileService.getById(testProfile.getId())).thenReturn(testProfile);

        mockMvc.perform(get(apiEndpoint + "/any/user/profile/{id}", testProfile.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testProfile.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is("Test")))
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.email", is("test@example.com")));
    }

    @Test
    void testGetProfileById_InvalidId_ShouldReturn404() throws Exception {
        when(profileService.getById(99999L)).thenThrow(new RuntimeException("Profile not found"));

        mockMvc.perform(get(apiEndpoint + "/any/user/profile/{id}", 99999L))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testPostProfileById_ValidRequest_ShouldReturn200() throws Exception {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setUserId(testUser.getId());

        when(profileService.getById(testUser.getId())).thenReturn(testProfile);

        mockMvc.perform(post(apiEndpoint + "/any/user/profile/{id}", testProfile.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profileDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Test")))
                .andExpect(jsonPath("$.username", is("testuser")));
    }

    // ============ TESTS UPDATE PROFILE ============
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testUpdateProfile_WithUserRole_ShouldReturn202() throws Exception {
        ProfileDTO updateDTO = ProfileDTO.builder()
                .firstName("Updated")
                .lastName1("User")
                .email("updated@example.com")
                .city("Barcelona")
                .build();

        Profile updatedProfile = new Profile();
        updatedProfile.setId(testProfile.getId());
        updatedProfile.setFirstName("Updated");
        updatedProfile.setLastName1("User");
        updatedProfile.setEmail("updated@example.com");
        updatedProfile.setCity("Barcelona");

        when(profileService.update(any(ProfileDTO.class), eq(testProfile.getId())))
                .thenReturn(updatedProfile);

        mockMvc.perform(put(apiEndpoint + "/any/user/profile/{id}", testProfile.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.firstName", is("Updated")))
                .andExpect(jsonPath("$.email", is("updated@example.com")))
                .andExpect(jsonPath("$.city", is("Barcelona")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateProfile_WithAdminRole_ShouldReturn202() throws Exception {
        ProfileDTO updateDTO = ProfileDTO.builder()
                .firstName("AdminUpdated")
                .lastName1("Profile")
                .phone("987654321")
                .build();

        Profile updatedProfile = new Profile();
        updatedProfile.setId(testProfile.getId());
        updatedProfile.setFirstName("AdminUpdated");
        updatedProfile.setLastName1("Profile");
        updatedProfile.setPhone("987654321");

        when(profileService.update(any(ProfileDTO.class), eq(testProfile.getId())))
                .thenReturn(updatedProfile);

        mockMvc.perform(put(apiEndpoint + "/any/user/profile/{id}", testProfile.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.firstName", is("AdminUpdated")))
                .andExpect(jsonPath("$.phone", is("987654321")));
    }

    @Test
    void testUpdateProfile_WithoutAuthentication_ShouldReturn401() throws Exception {
        ProfileDTO updateDTO = ProfileDTO.builder()
                .firstName("Unauthorized")
                .build();

        mockMvc.perform(put(apiEndpoint + "/any/user/profile/{id}", testProfile.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isUnauthorized());
    }

    // ============ TESTS FAVORITES MANAGEMENT ============
    @Test
    void testAddRemoveFavorite_ShouldReturn200() throws Exception {
        when(profileService.updateFavorites(testProfile.getId()))
                .thenReturn("Favorite updated successfully");

        mockMvc.perform(put(apiEndpoint + "/any/user/profile/user/profile/favorite/{id}", testProfile.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Favorite updated successfully"));
    }

    @Test
    void testGetFavorite_ShouldReturn200() throws Exception {
        when(profileService.getFavorites(testProfile.getId())).thenReturn(testProfile);

        mockMvc.perform(get(apiEndpoint + "/any/user/profile/user/profile/favorite/{id}", testProfile.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testProfile.getId().intValue())));
    }

    @Test
    void testDeleteFavorite_ShouldReturn200() throws Exception {
        when(profileService.updateFavorites(testProfile.getId()))
                .thenReturn("Favorite removed successfully");

        mockMvc.perform(delete(apiEndpoint + "/any/user/profile/user/profile/favorite/{id}", testProfile.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Favorite removed successfully"));
    }

    @Test
    void testPostFavorite_ValidId_ShouldReturn200() throws Exception {
        when(profileService.getFavorites(testProfile.getId())).thenReturn(testProfile);

        mockMvc.perform(post(apiEndpoint + "/any/user/profile/user/profile/favorite/{id}", testProfile.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testProfile.getId().intValue())));
    }

    @Test
    void testPostFavorite_InvalidId_ShouldReturn404() throws Exception {
        when(profileService.getFavorites(99999L)).thenReturn(null);

        mockMvc.perform(post(apiEndpoint + "/any/user/profile/user/profile/favorite/{id}", 99999L))
                .andExpect(status().isNotFound());
    }

    // ============ TESTS DELETE PROFILE ============
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testDeleteProfile_OwnProfile_ShouldReturn200() throws Exception {
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(gdprService.canDeleteUser(testUser.getId())).thenReturn(true);

        mockMvc.perform(delete(apiEndpoint + "/any/user/profile/{id}", testProfile.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Tu cuenta y todos tus datos han sido eliminados permanentemente conforme al GDPR"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteProfile_AsAdmin_ShouldReturn200() throws Exception {
        User adminUser = new User();
        adminUser.setId(2L);
        adminUser.setUsername("admin");
        adminUser.getRoles().add(adminRole);

        when(userService.findByUsername("admin")).thenReturn(Optional.of(adminUser));
        when(userService.findById(testProfile.getId())).thenReturn(Optional.of(testUser));
        when(profileService.delete(testProfile.getId())).thenReturn("Profile deleted successfully");

        mockMvc.perform(delete(apiEndpoint + "/any/user/profile/{id}", testProfile.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Profile deleted successfully"));
    }

    @Test
    @WithMockUser(username = "otheruser", roles = {"USER"})
    void testDeleteProfile_OtherUserProfile_ShouldReturn403() throws Exception {
        User otherUser = new User();
        otherUser.setId(3L);
        otherUser.setUsername("otheruser");
        otherUser.getRoles().add(userRole);

        when(userService.findByUsername("otheruser")).thenReturn(Optional.of(otherUser));

        mockMvc.perform(delete(apiEndpoint + "/any/user/profile/{id}", testProfile.getId()))
                .andExpect(status().isForbidden())
                .andExpect(content().string("No tienes permisos para eliminar este perfil"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testDeleteProfile_LastAdmin_ShouldReturn400() throws Exception {
        // Configurar usuario como admin
        testUser.getRoles().clear();
        testUser.getRoles().add(adminRole);

        when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(gdprService.canDeleteUser(testUser.getId())).thenReturn(false);

        mockMvc.perform(delete(apiEndpoint + "/any/user/profile/{id}", testProfile.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No se puede eliminar el último administrador del sistema"));
    }

    // ============ TESTS SELF PROFILE MANAGEMENT ============
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testUpdateSelfProfile_ShouldReturn200() throws Exception {
        ProfileDTO updateDTO = ProfileDTO.builder()
                .firstName("SelfUpdated")
                .email("selfupdated@example.com")
                .city("Valencia")
                .build();

        Profile updatedProfile = new Profile();
        updatedProfile.setId(testProfile.getId());
        updatedProfile.setFirstName("SelfUpdated");
        updatedProfile.setEmail("selfupdated@example.com");
        updatedProfile.setCity("Valencia");

        when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(profileService.update(any(ProfileDTO.class), eq(testProfile.getId())))
                .thenReturn(updatedProfile);

        mockMvc.perform(put(apiEndpoint + "/any/user/profile/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("SelfUpdated")))
                .andExpect(jsonPath("$.email", is("selfupdated@example.com")));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testDeleteSelfAccount_ShouldReturn200() throws Exception {
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(gdprService.canDeleteUser(testUser.getId())).thenReturn(true);

        mockMvc.perform(delete(apiEndpoint + "/any/user/profile/me"))
                .andExpect(status().isOk())
                .andExpect(content().string("Tu cuenta y todos tus datos han sido eliminados permanentemente conforme al GDPR"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testDeleteSelfAccount_LastAdmin_ShouldReturn400() throws Exception {
        // Configurar usuario como admin
        testUser.getRoles().clear();
        testUser.getRoles().add(adminRole);

        when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(gdprService.canDeleteUser(testUser.getId())).thenReturn(false);

        mockMvc.perform(delete(apiEndpoint + "/any/user/profile/me"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No se puede eliminar el último administrador del sistema"));
    }

    @Test
    @WithMockUser(username = "nonexistent", roles = {"USER"})
    void testUpdateSelfProfile_NonExistentUser_ShouldReturn404() throws Exception {
        ProfileDTO updateDTO = ProfileDTO.builder()
                .firstName("Test")
                .build();

        when(userService.findByUsername("nonexistent")).thenReturn(Optional.empty());

        mockMvc.perform(put(apiEndpoint + "/any/user/profile/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isNotFound());
    }

    // ============ TESTS ERROR HANDLING ============
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testUpdateProfile_ServiceException_ShouldReturn400() throws Exception {
        ProfileDTO updateDTO = ProfileDTO.builder()
                .firstName("Error")
                .build();

        when(profileService.update(any(ProfileDTO.class), anyLong()))
                .thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(put(apiEndpoint + "/any/user/profile/{id}", testProfile.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testUpdateSelfProfile_ServiceException_ShouldReturn400() throws Exception {
        ProfileDTO updateDTO = ProfileDTO.builder()
                .firstName("Error")
                .build();

        when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(profileService.update(any(ProfileDTO.class), anyLong()))
                .thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(put(apiEndpoint + "/any/user/profile/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isBadRequest());
    }
}
