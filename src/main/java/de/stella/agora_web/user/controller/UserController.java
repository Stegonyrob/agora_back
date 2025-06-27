package de.stella.agora_web.user.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.auth.SignUpDTO;
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

    public UserController(UserServiceImpl service, RegisterService registerService) {
        this.service = service;
        this.registerService = registerService;
    }

    @GetMapping(path = "/user")
    public ResponseEntity<List<User>> index() {
        List<User> users = service.getAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getById(@PathVariable Long userId) {
        Optional<User> user = service.findById(userId);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(path = "/user")
    public ResponseEntity<User> create(@NonNull @RequestBody User user) {
        User newUser = service.save(user);
        return ResponseEntity.status(201).body(newUser);
    }

    // Endpoint de registro que espera el frontend
    @PostMapping(path = "/user/register")
    public ResponseEntity<String> register(@Valid @RequestBody SignUpDTO signupDTO) {
        // Validar que las contraseñas coincidan
        if (signupDTO.getPassword() == null || signupDTO.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("La contraseña es obligatoria");
        }

        // Validar que se hayan aceptado las normas
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

}
