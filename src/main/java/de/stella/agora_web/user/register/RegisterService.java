package de.stella.agora_web.user.register;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import de.stella.agora_web.auth.SignUpDTO;
import de.stella.agora_web.encryptations.EncoderFacade;
import de.stella.agora_web.profiles.model.Profile;
import de.stella.agora_web.roles.model.Role;
import de.stella.agora_web.roles.service.RoleService;
import de.stella.agora_web.user.controller.dto.UserDTO;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.repository.UserRepository;

@Service
public class RegisterService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final EncoderFacade encoder;

    public RegisterService(UserRepository userRepository, RoleService roleService, EncoderFacade encoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.encoder = encoder;
    }

    public String save(User newUser) {
        validateUser(newUser);

        String passwordEncoded = encodePassword(newUser.getPassword());
        newUser.setPassword(passwordEncoded);
        assignDefaultRole(newUser);

        userRepository.save(newUser);

        return "User stored successfully: " + newUser.getUsername();
    }

    private void validateUser(User user) {
        if (!StringUtils.hasText(user.getUsername()) || !StringUtils.hasText(user.getPassword())) {
            throw new IllegalArgumentException("Username and password must not be empty");
        }
    }

    private String encodePassword(String password) {
        // Directamente codificar con BCrypt sin decodificar base64
        return encoder.encode("bcrypt", password);
    }

    public void assignDefaultRole(User user) {
        Role defaultRole = roleService.getById(2L);
        Set<Role> roles = new HashSet<>();
        roles.add(defaultRole);
        user.setRoles(roles);
    }

    public User registerUser(SignUpDTO signupDTO) {
        User user = new User();
        user.setUsername(signupDTO.getUsername());
        user.setEmail(signupDTO.getEmail()); // Agregar email
        user.setAcceptedRules(true); // Usuario acepta las reglas al registrarse

        String passwordEncoded = encodePassword(signupDTO.getPassword());
        user.setPassword(passwordEncoded);
        assignDefaultRole(user);

        return userRepository.save(user);
    }

    public Profile registerProfile(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setAcceptedRules(true); // Usuario acepta las reglas al registrarse

        String passwordEncoded = encodePassword(userDTO.getPassword());
        user.setPassword(passwordEncoded);
        assignDefaultRole(user);

        userRepository.save(user);

        Profile profile = new Profile();
        profile.setUser(user);

        return profile;
    }

    public String createUser(SignUpDTO signupDTO) {
        // Verificar si ya existe un usuario con ese username o email
        boolean userExists = userRepository.findAll().stream()
                .anyMatch(user -> user.getUsername().equals(signupDTO.getUsername())
                || user.getEmail().equals(signupDTO.getEmail()));

        if (userExists) {
            return "Error: Ya existe un usuario con ese username o email";
        }

        try {
            User user = registerUser(signupDTO);
            return "Usuario creado exitosamente: " + user.getUsername();
        } catch (Exception e) {
            return "Error al crear usuario: " + e.getMessage();
        }
    }
}
