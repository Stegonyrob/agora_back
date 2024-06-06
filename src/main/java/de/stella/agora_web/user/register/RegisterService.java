package de.stella.agora_web.user.register;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import de.stella.agora_web.encryptations.EncoderFacade;
import de.stella.agora_web.roles.model.Role;
import de.stella.agora_web.roles.service.RoleService;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.repository.UserRepository;


@Service
public class RegisterService {

    UserRepository userRepository;
    RoleService roleService;
    EncoderFacade encoder;
    ProfileRepository profileRepository;
    TokenGenerator tokenGenerator;

    public RegisterService(UserRepository userRepository, RoleService roleService, EncoderFacade encoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.encoder = encoder;
    }

  public String createUser(SignUpDTO signupDTO) {
    User newUser = new User(signupDTO.getUsername(), signupDTO.getPassword()); 

    System.out.println(newUser.getId());

        String passwordDecoded = encoder.decode("base64", newUser.getPassword());
        String passwordEncoded = encoder.encode("bcrypt", passwordDecoded);

        newUser.setPassword(passwordEncoded);
        assignDefaultRole(newUser);

        userRepository.save(newUser);

        User savedUser = userRepository.getReferenceById(newUser.getId().toString());
        
        // Creamos un perfil para el usuario que acabamos de crear.
        // Esto es necesario para que el usuario pueda tener un perfil en la BD
        // y poder ser autenticado posteriormente.
        
        Profile newProfile = Profile.builder()
                .id(savedUser.getId())
                .user(savedUser)
                .email(savedUser.getUsername())
                .firstName("")
                .lastName("")
                .address("")
                .postalCode("")
                .numberPhone("")
                .city("")
                .province("")
                .build();

        profileRepository.save(newProfile);

        String message = "User with the username " + newUser.getUsername() + " is successfully created.";

        return message;

    }

    public void assignDefaultRole(User user) {

        Role defaultRole = roleService.getById(2L);
        Set<Role> roles = new HashSet<>();
        roles.add(defaultRole);

        user.setRoles(roles);
    }
}
