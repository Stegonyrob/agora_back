package de.stella.agora_web;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static org.hamcrest.Matchers.containsString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests de autenticación JWT Verifica validación de tokens, audiencia,
 * expiración
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
@DisplayName("JWT Authentication - Tests de validación de tokens")
class JWTAuthTest {

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private MockMvc mvc;

    @Value("${jwt-issuer}")
    private String jwtIssuer;

    @Value("${jwt-audience}")
    private String jwtAudience;

    @Test
    @DisplayName("Debe rechazar tokens con audiencia inválida")
    void shouldNotAllowTokensWithAnInvalidAudience() throws Exception {
        String token = mint(claims -> claims.audience(List.of("https://wrong-audience")));

        this.mvc.perform(
                get("/api/v1/any/test").header("Authorization", "Bearer " + token)
        )
                .andExpect(status().isUnauthorized())
                .andExpect(
                        header().string("WWW-Authenticate", containsString("aud claim is not valid"))
                );
    }

    @Test
    @DisplayName("Debe rechazar tokens expirados")
    void shouldNotAllowTokensThatAreExpired() throws Exception {
        String token = mint(claims
                -> claims
                        .issuedAt(Instant.now().minusSeconds(3600))
                        .expiresAt(Instant.now().minusSeconds(3599))
        );

        this.mvc.perform(
                get("/api/v1/any/test").header("Authorization", "Bearer " + token)
        )
                .andExpect(status().isUnauthorized())
                .andExpect(
                        header().string("WWW-Authenticate", containsString("Jwt expired"))
                );
    }

    @Test
    @DisplayName("Debe mostrar todos los errores de validación del token")
    void shouldShowAllTokenValidationErrors() throws Exception {
        String expired = mint(claims
                -> claims
                        .audience(List.of("https://wrong-audience"))
                        .issuedAt(Instant.now().minusSeconds(3600))
                        .expiresAt(Instant.now().minusSeconds(3599))
        );

        this.mvc.perform(
                get("/api/v1/any/test").header("Authorization", "Bearer " + expired)
        )
                .andExpect(status().isUnauthorized())
                .andExpect(header().exists("WWW-Authenticate"));
    }

    /**
     * Helper method para crear tokens JWT de prueba
     */
    private String mint(Consumer<JwtClaimsSet.Builder> consumer) {
        JwtClaimsSet.Builder builder = JwtClaimsSet
                .builder()
                .issuer(jwtIssuer)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(5, ChronoUnit.MINUTES))
                .subject("1")
                .audience(Arrays.asList(jwtAudience));

        consumer.accept(builder);
        JwtEncoderParameters parameters = JwtEncoderParameters.from(builder.build());
        return this.jwtEncoder.encode(parameters).getTokenValue();
    }
}
