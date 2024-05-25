package de.stella.agora_web.user.services;

import de.stella.agora_web.user.controller.dto.UserDTO;
import de.stella.agora_web.user.model.User;

public interface UserRegistrationService {
    User registerUser(UserDTO userDTO);
}