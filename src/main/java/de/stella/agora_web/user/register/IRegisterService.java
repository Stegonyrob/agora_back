package de.stella.agora_web.user.register;

import de.stella.agora_web.auth.SignUpDTO;
import de.stella.agora_web.user.model.User;

public interface IRegisterService {
    String createUser(SignUpDTO signupDTO);
    void assignDefaultRole(User user);
}