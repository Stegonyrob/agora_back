package de.stella.agora_web.user.register;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.auth.SignUpDTO;
import de.stella.agora_web.auth.TokenGenerator;


@RestController
@RequestMapping(path = "${api-endpoint}")
public class RegisterController {

    RegisterService service;

    TokenGenerator tokenGenerator; 



    @PostMapping("/all/register") 
    public ResponseEntity<String> register(@RequestBody SignUpDTO signupDTO) { 

        String message = service.createUser(signupDTO);
  
        return ResponseEntity.ok(message); 
    }

}
