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

import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.services.impl.UserServiceImpl;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@RestController
@RequestMapping(path = "${api-endpoint}/users")
public class UserController {

    UserServiceImpl service;

    public UserController(UserServiceImpl service) {
        this.service = service;
    }

    @GetMapping(path = "")
    public List<User> index() {
        return service.getAll();
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

    @PostMapping(path = "")
    public ResponseEntity<User> create(@NonNull @RequestBody User user) {
        User newUser = service.save(user);
        return ResponseEntity.status(201).body(newUser);
    }

}