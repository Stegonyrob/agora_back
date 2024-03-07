package de.stella.agora_web.encryptations.bcryptfacade;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import de.stella.agora_web.encryptations.IEncoder;




public class BcryptEncoder implements IEncoder {
    
    BCryptPasswordEncoder encoder;

    public BcryptEncoder(BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public String encode(String data) {
        String dataEncoded = encoder.encode(data);
        return dataEncoded;
    }

}
