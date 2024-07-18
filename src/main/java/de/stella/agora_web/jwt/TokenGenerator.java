package de.stella.agora_web.jwt;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
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

        JwtClaimsSet claimsSet = JwtClaimsSet
                .builder().issuer("http://localhost:8080").issuedAt(now).expiresAt(now.plus(5, ChronoUnit.MINUTES))
                .subject(Long.toString(securityUser.getId())).claim("role", securityUser.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                .audience(Arrays.asList("AgoraApp")).build();
        return accessTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

    private String createRefreshToken(Authentication authentication) {

        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Instant now = Instant.now();

        JwtClaimsSet claimsSet = JwtClaimsSet
                .builder().issuer("http://localhost:8080").issuedAt(now).expiresAt(now.plus(5, ChronoUnit.MINUTES))
                .subject(Long.toString(securityUser.getId())).claim("role", securityUser.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                .audience(Arrays.asList("AgoraApp")).build();
        return refreshTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

    public TokenDTO createToken(Authentication authentication) {

        if (!(authentication.getPrincipal() instanceof SecurityUser securityUser)) {
            throw new BadCredentialsException(MessageFormat.format("principal {0} is not of User type",
                    authentication.getPrincipal().getClass()));
        }
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

}
