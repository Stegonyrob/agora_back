package de.stella.agora_web.user.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.auth.SignUpDTO;
import de.stella.agora_web.gdpr.service.GdprService;
import de.stella.agora_web.user.controller.dto.UserListDTO;
import de.stella.agora_web.user.mapper.UserMapper;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.register.RegisterService;
import de.stella.agora_web.user.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@RestController
@RequestMapping(path = "${api-endpoint}/any")
public class UserController {

    UserServiceImpl service;
    RegisterService registerService;
    UserMapper userMapper;
    GdprService gdprService;

    public UserController(UserServiceImpl service, RegisterService registerService, UserMapper userMapper, GdprService gdprService) {
        this.service = service;
        this.registerService = registerService;
        this.userMapper = userMapper;
        this.gdprService = gdprService;
    }

    @GetMapping(path = "/user")
    @ResponseBody
    public List<UserListDTO> getAllUsers() {
        List<User> users = service.getAllUsers();
        return userMapper.toUserListDTOs(users);
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<UserListDTO> getById(@PathVariable Long userId) {
        Optional<User> user = service.findById(userId);
        if (user.isPresent()) {
            UserListDTO userDTO = userMapper.toUserListDTO(user.get());
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(path = "/user")
    public ResponseEntity<UserListDTO> create(@NonNull @RequestBody User user) {
        User newUser = service.save(user);
        UserListDTO userDTO = userMapper.toUserListDTO(newUser);
        return ResponseEntity.status(201).body(userDTO);
    }

    @PostMapping(path = "/user/register")
    public ResponseEntity<String> register(@Valid @RequestBody SignUpDTO signupDTO) {
        if (signupDTO.getPassword() == null || signupDTO.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("La contraseña es obligatoria");
        }

        if (!signupDTO.isRulesAccepted()) {
            return ResponseEntity.badRequest().body("Debes aceptar las normas del blog para registrarte");
        }

        String message = registerService.createUser(signupDTO);

        if (message.contains("exitosamente") || message.contains("successfully")) {
            return ResponseEntity.ok(message);
        } else {
            return ResponseEntity.badRequest().body(message);
        }
    }

    // ============= ENDPOINTS ADMINISTRATIVOS =============
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        try {
            if (!gdprService.canDeleteUser(userId)) {
                return ResponseEntity.badRequest().body("No se puede eliminar el último administrador del sistema");
            }

            // Usar GdprService para eliminación completa en cascada
            gdprService.deleteAllUserData(userId);
            return ResponseEntity.ok("Usuario eliminado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar usuario: " + e.getMessage());
        }
    }

}
