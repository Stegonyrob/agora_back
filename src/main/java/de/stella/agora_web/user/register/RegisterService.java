package de.stella.agora_web.user.register;



import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import de.stella.agora_web.auth.SignUpDTO;
import de.stella.agora_web.encryptations.EncoderFacade;
import de.stella.agora_web.jwt.TokenGenerator;
import de.stella.agora_web.profiles.model.Profile;
import de.stella.agora_web.profiles.repository.ProfileRepository;
import de.stella.agora_web.roles.model.Role;
import de.stella.agora_web.roles.service.RoleService;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.repository.UserRepository;
import lombok.AllArgsConstructor;
@Service
@AllArgsConstructor
public class RegisterService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final EncoderFacade encoder;
    private final ProfileRepository profileRepository;
    @SuppressWarnings("unused")
    private final TokenGenerator tokenGenerator;

    public String createUser(SignUpDTO signupDTO) {
        User newUser = new User();
        String passwordDecoded = encoder.decode("base64", newUser.getPassword());
        String passwordEncoded = encoder.encode("bcrypt", passwordDecoded);
        newUser.setPassword(passwordEncoded);
        assignDefaultRole(newUser);
        newUser = userRepository.save(newUser);
        Profile newProfile = Profile.builder()
                .id(newUser.getId())
                .user(newUser)
                .email(newUser.getUsername())
                .firstName("")
                .firstLastName("")
                .secondLastName("")
                .address("")
                .postalCode("")
                .numberPhone("")
                .city("")
                .province("")
                .build();
        profileRepository.save(newProfile);
        return "User with the username " + newUser.getUsername() + " is successfully created.";
    }

    private void assignDefaultRole(User user) {
        Role defaultRole = roleService.getById(2L);
        Set<Role> roles = new HashSet<>();
        roles.add(defaultRole);
        user.setRoles(roles);
    }
}
