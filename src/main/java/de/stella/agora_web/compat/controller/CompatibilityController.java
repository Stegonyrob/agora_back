package de.stella.agora_web.compat.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.user.controller.dto.UserListDTO;
import de.stella.agora_web.user.mapper.UserMapper;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;

/**
 * Compatibility controller to handle legacy API endpoints. This controller
 * provides backward compatibility for frontend clients that may still be using
 * the old /api/* endpoints instead of /api/v1/*.
 */
@Slf4j
@RestController
@RequestMapping(path = "/api/any")
public class CompatibilityController {

    private final UserServiceImpl userService;
    private final UserMapper userMapper;

    public CompatibilityController(UserServiceImpl userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    /**
     * Legacy endpoint for getting all users. Maps to /api/any/user for backward
     * compatibility. Clients should migrate to /api/v1/any/user NOW RETURNS
     * OPTIMIZED DTOs instead of full User entities
     */
    @GetMapping(path = "/user")
    public ResponseEntity<List<UserListDTO>> getAllUsers() {
        log.warn("Legacy endpoint /api/any/user accessed. Please migrate to /api/v1/any/user");
        List<User> users = userService.getAllUsers(); // Usar método optimizado
        List<UserListDTO> userDTOs = userMapper.toUserListDTOs(users);
        return ResponseEntity.ok(userDTOs);
    }

    /**
     * Legacy endpoint for getting a user by ID. Maps to /api/any/{userId} for
     * backward compatibility. Clients should migrate to /api/v1/any/{userId}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        log.warn("Legacy endpoint /api/any/{} accessed. Please migrate to /api/v1/any/{}", userId, userId);
        Optional<User> user = userService.findById(userId);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
