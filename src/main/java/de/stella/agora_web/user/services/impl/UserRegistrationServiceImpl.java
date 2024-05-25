package de.stella.agora_web.user.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.stella.agora_web.user.controller.dto.UserDTO;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.services.UserRegistrationService;

@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {
    @Autowired
    private UserServiceImpl userService;

    @Override
    public User registerUser(UserDTO userDTO) {
        return userService.registerUser(userDTO);
    }
}