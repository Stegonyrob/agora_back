package de.stella.agora_web.user.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.stella.agora_web.auth.SignUpDTO;
import de.stella.agora_web.config.TestConfig;
import de.stella.agora_web.gdpr.service.GdprService;
import de.stella.agora_web.roles.model.Role;
import de.stella.agora_web.roles.repository.RoleRepository;
import de.stella.agora_web.user.controller.dto.UserListDTO;
import de.stella.agora_web.user.mapper.UserMapper;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.register.RegisterService;
import de.stella.agora_web.user.repository.UserRepository;
import de.stella.agora_web.user.service.impl.UserServiceImpl;

/**
 * Tests para UserController - Gestión de usuarios y operaciones administrativas
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
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${api-endpoint}")
    private String apiEndpoint;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private RegisterService registerService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private GdprService gdprService;

    private User testUser;
    private User adminUser;
    private User secondAdminUser;
    private Role userRole;
    private Role adminRole;
    private UserListDTO testUserDTO;
    private UserListDTO adminUserDTO;

    @BeforeEach
    void setUp() {
        // Limpiar datos
        userRepository.deleteAll();
        roleRepository.deleteAll();

        // Crear roles
        userRole = new Role();
        userRole.setName("USER");
        userRole = roleRepository.save(userRole);

        adminRole = new Role();
        adminRole.setName("ADMIN");
        adminRole = roleRepository.save(adminRole);

        // Crear usuarios de prueba
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.getRoles().add(userRole);
        testUser.setSanctionStatus(User.SanctionStatus.NONE);
        // testUser.setCreationDate(LocalDateTime.now());
        testUser = userRepository.save(testUser);

        adminUser = new User();
        adminUser.setId(2L);
        adminUser.setUsername("admin");
        adminUser.setEmail("admin@example.com");
        adminUser.setPassword("admin123");
        adminUser.getRoles().add(adminRole);
        adminUser.setSanctionStatus(User.SanctionStatus.NONE);
        // adminUser.setCreationDate(LocalDateTime.now());
        adminUser = userRepository.save(adminUser);

        secondAdminUser = new User();
        secondAdminUser.setId(3L);
        secondAdminUser.setUsername("admin2");
        secondAdminUser.setEmail("admin2@example.com");
        secondAdminUser.setPassword("admin123");
        secondAdminUser.getRoles().add(adminRole);
        secondAdminUser.setSanctionStatus(User.SanctionStatus.NONE);
        // secondAdminUser.setCreationDate(LocalDateTime.now());
        secondAdminUser = userRepository.save(secondAdminUser);

        // Crear DTOs de prueba
        testUserDTO = new UserListDTO();
        testUserDTO.setId(testUser.getId());
        testUserDTO.setUsername(testUser.getUsername());
        testUserDTO.setEmail(testUser.getEmail());

        adminUserDTO = new UserListDTO();
        adminUserDTO.setId(adminUser.getId());
        adminUserDTO.setUsername(adminUser.getUsername());
        adminUserDTO.setEmail(adminUser.getEmail());
    }

    // ============ TESTS GET ALL USERS ============
    @Test
    void testGetAllUsers_ShouldReturn200() throws Exception {
        List<User> users = Arrays.asList(testUser, adminUser);
        List<UserListDTO> userDTOs = Arrays.asList(testUserDTO, adminUserDTO);

        when(userService.getAllUsers()).thenReturn(users);
        when(userMapper.toUserListDTOs(users)).thenReturn(userDTOs);

        mockMvc.perform(get(apiEndpoint + "/any/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(testUser.getId().intValue())))
                .andExpect(jsonPath("$[0].username", is("testuser")))
                .andExpect(jsonPath("$[0].email", is("test@example.com")))
                .andExpect(jsonPath("$[1].id", is(adminUser.getId().intValue())))
                .andExpect(jsonPath("$[1].username", is("admin")))
                .andExpect(jsonPath("$[1].email", is("admin@example.com")));
    }

    @Test
    void testGetAllUsers_EmptyList_ShouldReturn200() throws Exception {
        when(userService.getAllUsers()).thenReturn(Arrays.asList());
        when(userMapper.toUserListDTOs(Arrays.asList())).thenReturn(Arrays.asList());

        mockMvc.perform(get(apiEndpoint + "/any/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // ============ TESTS GET USER BY ID ============
    @Test
    void testGetUserById_ExistingUser_ShouldReturn200() throws Exception {
        when(userService.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(userMapper.toUserListDTO(testUser)).thenReturn(testUserDTO);

        mockMvc.perform(get(apiEndpoint + "/any/user/{userId}", testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testUser.getId().intValue())))
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.email", is("test@example.com")));
    }

    @Test
    void testGetUserById_NonExistentUser_ShouldReturn404() throws Exception {
        when(userService.findById(99999L)).thenReturn(Optional.empty());

        mockMvc.perform(get(apiEndpoint + "/any/user/{userId}", 99999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetUserById_NullId_ShouldReturn400() throws Exception {
        mockMvc.perform(get(apiEndpoint + "/any/user/{userId}", "null"))
                .andExpect(status().isBadRequest());
    }

    // ============ TESTS CREATE USER ============
    @Test
    void testCreateUser_ValidData_ShouldReturn201() throws Exception {
        User newUser = new User();
        newUser.setId(4L);
        newUser.setUsername("newuser");
        newUser.setEmail("newuser@example.com");
        newUser.setPassword("password123");

        UserListDTO newUserDTO = new UserListDTO();
        newUserDTO.setId(4L);
        newUserDTO.setUsername("newuser");
        newUserDTO.setEmail("newuser@example.com");

        when(userService.save(any(User.class))).thenReturn(newUser);
        when(userMapper.toUserListDTO(newUser)).thenReturn(newUserDTO);

        mockMvc.perform(post(apiEndpoint + "/any/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.username", is("newuser")))
                .andExpect(jsonPath("$.email", is("newuser@example.com")));
    }

    @Test
    void testCreateUser_InvalidData_ShouldReturn400() throws Exception {
        User invalidUser = new User();
        // Sin username ni email

        when(userService.save(any(User.class)))
                .thenThrow(new RuntimeException("Validation error"));

        mockMvc.perform(post(apiEndpoint + "/any/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testCreateUser_NullBody_ShouldReturn400() throws Exception {
        mockMvc.perform(post(apiEndpoint + "/any/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // ============ TESTS USER REGISTRATION ============
    @Test
    void testRegisterUser_ValidData_ShouldReturn200() throws Exception {
        SignUpDTO signUpDTO = new SignUpDTO();
        signUpDTO.setUsername("newregisteruser");
        signUpDTO.setEmail("register@example.com");
        signUpDTO.setPassword("SecurePassword123!");
        signUpDTO.setRulesAccepted(true);

        when(registerService.createUser(any(SignUpDTO.class)))
                .thenReturn("Usuario registrado exitosamente");

        mockMvc.perform(post(apiEndpoint + "/any/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario registrado exitosamente"));
    }

    @Test
    void testRegisterUser_EmptyPassword_ShouldReturn400() throws Exception {
        SignUpDTO signUpDTO = new SignUpDTO();
        signUpDTO.setUsername("newuser");
        signUpDTO.setEmail("test@example.com");
        signUpDTO.setPassword(""); // Contraseña vacía
        signUpDTO.setRulesAccepted(true);

        mockMvc.perform(post(apiEndpoint + "/any/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("La contraseña es obligatoria"));
    }

    @Test
    void testRegisterUser_NullPassword_ShouldReturn400() throws Exception {
        SignUpDTO signUpDTO = new SignUpDTO();
        signUpDTO.setUsername("newuser");
        signUpDTO.setEmail("test@example.com");
        signUpDTO.setPassword(null); // Contraseña null
        signUpDTO.setRulesAccepted(true);

        mockMvc.perform(post(apiEndpoint + "/any/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("La contraseña es obligatoria"));
    }

    @Test
    void testRegisterUser_RulesNotAccepted_ShouldReturn400() throws Exception {
        SignUpDTO signUpDTO = new SignUpDTO();
        signUpDTO.setUsername("newuser");
        signUpDTO.setEmail("test@example.com");
        signUpDTO.setPassword("SecurePassword123!");
        signUpDTO.setRulesAccepted(false); // No acepta las normas

        mockMvc.perform(post(apiEndpoint + "/any/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Debes aceptar las normas del blog para registrarte"));
    }

    @Test
    void testRegisterUser_DuplicateEmail_ShouldReturn400() throws Exception {
        SignUpDTO signUpDTO = new SignUpDTO();
        signUpDTO.setUsername("newuser");
        signUpDTO.setEmail("test@example.com");
        signUpDTO.setPassword("SecurePassword123!");
        signUpDTO.setRulesAccepted(true);

        when(registerService.createUser(any(SignUpDTO.class)))
                .thenReturn("El email ya está registrado");

        mockMvc.perform(post(apiEndpoint + "/any/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El email ya está registrado"));
    }

    @Test
    void testRegisterUser_DuplicateUsername_ShouldReturn400() throws Exception {
        SignUpDTO signUpDTO = new SignUpDTO();
        signUpDTO.setUsername("testuser");
        signUpDTO.setEmail("newemail@example.com");
        signUpDTO.setPassword("SecurePassword123!");
        signUpDTO.setRulesAccepted(true);

        when(registerService.createUser(any(SignUpDTO.class)))
                .thenReturn("El nombre de usuario ya existe");

        mockMvc.perform(post(apiEndpoint + "/any/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El nombre de usuario ya existe"));
    }

    // ============ TESTS DELETE USER (ADMIN ONLY) ============
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteUser_AsAdmin_ShouldReturn200() throws Exception {
        when(gdprService.canDeleteUser(testUser.getId())).thenReturn(true);

        mockMvc.perform(delete(apiEndpoint + "/any/user/{userId}", testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario eliminado exitosamente"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteUser_LastAdmin_ShouldReturn400() throws Exception {
        when(gdprService.canDeleteUser(adminUser.getId())).thenReturn(false);

        mockMvc.perform(delete(apiEndpoint + "/any/user/{userId}", adminUser.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No se puede eliminar el último administrador del sistema"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteUser_ServiceException_ShouldReturn400() throws Exception {
        when(gdprService.canDeleteUser(testUser.getId())).thenReturn(true);
        doThrow(new RuntimeException("Database error"))
                .when(gdprService).deleteAllUserData(testUser.getId());

        mockMvc.perform(delete(apiEndpoint + "/any/user/{userId}", testUser.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error al eliminar usuario: Database error"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testDeleteUser_AsUser_ShouldReturn403() throws Exception {
        mockMvc.perform(delete(apiEndpoint + "/any/user/{userId}", testUser.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeleteUser_WithoutAuthentication_ShouldReturn401() throws Exception {
        mockMvc.perform(delete(apiEndpoint + "/any/user/{userId}", testUser.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteUser_NonExistentUser_ShouldReturn400() throws Exception {
        when(gdprService.canDeleteUser(99999L)).thenReturn(true);
        doThrow(new RuntimeException("User not found"))
                .when(gdprService).deleteAllUserData(99999L);

        mockMvc.perform(delete(apiEndpoint + "/any/user/{userId}", 99999L))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error al eliminar usuario: User not found"));
    }

    // ============ TESTS EDGE CASES AND VALIDATION ============
    @Test
    void testGetAllUsers_ServiceException_ShouldReturn500() throws Exception {
        when(userService.getAllUsers()).thenThrow(new RuntimeException("Database connection error"));

        mockMvc.perform(get(apiEndpoint + "/any/user"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testCreateUser_ServiceException_ShouldReturn500() throws Exception {
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setEmail("newuser@example.com");

        when(userService.save(any(User.class)))
                .thenThrow(new RuntimeException("Database constraint violation"));

        mockMvc.perform(post(apiEndpoint + "/any/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testRegisterUser_ValidationError_ShouldReturn400() throws Exception {
        SignUpDTO signUpDTO = new SignUpDTO();
        signUpDTO.setUsername(""); // Username vacío
        signUpDTO.setEmail("invalid-email"); // Email inválido
        signUpDTO.setPassword("123"); // Contraseña muy corta
        signUpDTO.setRulesAccepted(true);

        mockMvc.perform(post(apiEndpoint + "/any/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterUser_MalformedJSON_ShouldReturn400() throws Exception {
        mockMvc.perform(post(apiEndpoint + "/any/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{invalid json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteUser_SelfDeletion_ShouldReturn400() throws Exception {
        // Admin tratando de eliminarse a sí mismo
        when(gdprService.canDeleteUser(adminUser.getId())).thenReturn(false);

        mockMvc.perform(delete(apiEndpoint + "/any/user/{userId}", adminUser.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No se puede eliminar el último administrador del sistema"));
    }

    @Test
    void testCreateUser_LargePayload_ShouldHandleGracefully() throws Exception {
        User newUser = new User();
        newUser.setUsername("user");
        newUser.setEmail("user@example.com");
        newUser.setPassword("a".repeat(1000)); // Contraseña muy larga

        when(userService.save(any(User.class)))
                .thenThrow(new RuntimeException("Payload too large"));

        mockMvc.perform(post(apiEndpoint + "/any/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isInternalServerError());
    }
}
