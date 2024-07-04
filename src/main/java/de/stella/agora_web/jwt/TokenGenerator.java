package de.stella.agora_web.jwt;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import de.stella.agora_web.security.SecurityUser;

@Component
public class TokenGenerator {

    @Autowired
    JwtEncoder accessTokenEncoder;

    @Autowired
    @Qualifier("jwtRefreshTokenEncoder")
    JwtEncoder refreshTokenEncoder;

    private String createAccessToken(Authentication authentication) {

        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        System.out.println(securityUser);

        Instant now = Instant.now();

        JwtClaimsSet claimsSet = JwtClaimsSet.builder().issuer("myApp").issuedAt(now)
                .expiresAt(now.plus(30, ChronoUnit.DAYS)).subject(Long.toString(securityUser.getId()))
                .claim("role", securityUser.getAuthorities().stream().map(authority -> authority.getAuthority())
                        .collect(Collectors.joining(",")))
                .build();
        return accessTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

    private String createRefreshToken(Authentication authentication) {

        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Instant now = Instant.now();

        JwtClaimsSet claimsSet = JwtClaimsSet.builder().issuer("myApp").issuedAt(now)
                .expiresAt(now.plus(30, ChronoUnit.DAYS)).subject(Long.toString(securityUser.getId()))
                .claim("role", securityUser.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(",")))
                .build();

        return refreshTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

    public TokenDTO createToken(Authentication authentication) {

        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        System.out.println(authentication.getPrincipal());

        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setUserId(securityUser.getId());
        tokenDTO.setAccessToken(createAccessToken(authentication));

        String refreshToken;
        if (authentication.getCredentials() instanceof Jwt jwt) {

            Instant now = Instant.now();

            Instant expiresAt = jwt.getExpiresAt();

            Duration duration = Duration.between(now, expiresAt);
            long daysUntilExpired = duration.toDays();

            if (daysUntilExpired < 7) {
                refreshToken = createRefreshToken(authentication);
            } else {
                refreshToken = jwt.getTokenValue();
            }
        } else {

            refreshToken = createRefreshToken(authentication);
        }

        tokenDTO.setRefreshToken(refreshToken);

        return tokenDTO;
    }

    public String createAccessToken(String username) {
        JwtClaimsSet claimsSet = JwtClaimsSet.builder().issuer("myApp").issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(30, ChronoUnit.DAYS)).subject(username).build();
        return accessTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

}
