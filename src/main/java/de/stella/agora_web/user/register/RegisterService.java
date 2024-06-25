package de.stella.agora_web.user.register;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

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

    UserRepository userRepository;
    RoleService roleService;
    EncoderFacade encoder;

    public RegisterService(UserRepository userRepository, RoleService roleService, EncoderFacade encoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.encoder = encoder;
    }

    public String save(User newUser) {

        String passwordDecoded = encoder.decode("base64", newUser.getPassword());
        String passwordEncoded = encoder.encode("bcrypt", passwordDecoded);

        newUser.setPassword(passwordEncoded);
        assignDefaultRole(newUser);

        userRepository.save(newUser);

        return "user stored successfully :" + newUser.getUsername();

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

        String passwordDecoded = encoder.decode("base64", signupDTO.getPassword());
        String passwordEncoded = encoder.encode("bcrypt", passwordDecoded);

        user.setPassword(passwordEncoded);
        assignDefaultRole(user);

        return userRepository.save(user);
    }

    public Profile registerProfile(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());

        String passwordDecoded = encoder.decode("base64", userDTO.getPassword());
        String passwordEncoded = encoder.encode("bcrypt", passwordDecoded);

        user.setPassword(passwordEncoded);
        assignDefaultRole(user);

        userRepository.save(user);

        Profile profile = new Profile();
        profile.setUser(user);

        return profile;
    }

    public String createUser(SignUpDTO signupDTO) {
        User user = registerUser(signupDTO);
        return "User created successfully: " + user.getUsername();
    }
}