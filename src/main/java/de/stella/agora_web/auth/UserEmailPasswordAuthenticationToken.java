package de.stella.agora_web.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class UserEmailPasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

    public UserEmailPasswordAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public static UserEmailPasswordAuthenticationToken unauthenticated(Object email, Object password) {
        return new UserEmailPasswordAuthenticationToken(email, password);
    }
}
