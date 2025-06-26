package de.stella.agora_web.user.register;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.auth.SignUpDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(path = "${api-endpoint}/all")
public class RegisterController {

    RegisterService service;

    @PostMapping(path = "/register")
    public ResponseEntity<String> register(@Valid @RequestBody SignUpDTO signupDTO) {
        // Validar que las contraseñas coincidan
        if (signupDTO.getPassword() == null || signupDTO.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("La contraseña es obligatoria");
        }

        String message = service.createUser(signupDTO);

        if (message.contains("successfully")) {
            return ResponseEntity.ok(message);
        } else {
            return ResponseEntity.badRequest().body(message);
        }
    }
}
