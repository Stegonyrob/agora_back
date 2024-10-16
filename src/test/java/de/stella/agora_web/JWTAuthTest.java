package de.stella.agora_web;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@TestPropertySource(
  properties = {
    "ACCESS_TOKEN_PRIVATE_KEY_PATH=src/main/java/de/mark/jewelsstorebackend/auth/keys/access-token-private.key",
    "ACCESS_TOKEN_PUBLIC_KEY_PATH=src/main/java/dev/mark/jewelsstorebackend/auth/keys/access-token-public.key",
    "REFRESH_TOKEN_PRIVATE_KEY_PATH=src/main/java/dev/mark/jewelsstorebackend/auth/keys/refresh-token-private.key",
    "REFRESH_TOKEN_PUBLIC_KEY_PATH=src/main/java/dev/mark/jewelsstorebackend/auth/keys/refresh-token-public.key",
    "STRIPE_PUBLIC_KEY=pk_test_123123",
    "STRIPE_SECRET_KEY=sk_test_123123",
    "JWT-ISSUER=http://localhost:8080",
    "JWT-AUDIENCE=AgoraApp",
  }
)
@AutoConfigureMockMvc
public class JWTAuthTest {

  @Autowired
  JwtEncoder jwtEncoder;

  @Autowired
  private MockMvc mvc;

  @Test
  void shouldNotAllowTokensWithAnInvalidAudience() throws Exception {
    String token = mint(claims -> claims.audience(List.of("https://wrong")));
    this.mvc.perform(
        get("/user/getById/2").header("Authorization", "Bearer " + token)
      )
      .andExpect(status().isUnauthorized())
      .andExpect(
        header()
          .string("WWW-Authenticate", containsString("aud claim is not valid"))
      );
  }

  @Test
  void shouldNotAllowTokensThatAreExpired() throws Exception {
    String token = mint(claims ->
      claims
        .issuedAt(Instant.now().minusSeconds(3600))
        .expiresAt(Instant.now().minusSeconds(3599))
    );
    this.mvc.perform(
        get("/user/getById/2").header("Authorization", "Bearer " + token)
      )
      .andExpect(status().isUnauthorized())
      .andExpect(
        header().string("WWW-Authenticate", containsString("Jwt expired"))
      );
  }

  @Test
  void shouldShowAllTokenValidationErrors() throws Exception {
    String expired = mint(claims ->
      claims
        .audience(List.of("https://wrong"))
        .issuedAt(Instant.now().minusSeconds(3600))
        .expiresAt(Instant.now().minusSeconds(3599))
    );
    this.mvc.perform(
        get("/cashcards").header("Authorization", "Bearer " + expired)
      )
      .andExpect(status().isUnauthorized())
      .andExpect(header().exists("WWW-Authenticate"))
      .andExpect(
        jsonPath("$.errors..description")
          .value(
            containsInAnyOrder(
              containsString("Jwt expired"),
              containsString("aud claim is not valid")
            )
          )
      );
  }

  private String mint(Consumer<JwtClaimsSet.Builder> consumer) {
    JwtClaimsSet.Builder builder = JwtClaimsSet
      .builder()
      .issuer("http://localhost:8080")
      .issuedAt(Instant.now())
      .expiresAt(Instant.now().plus(5, ChronoUnit.MINUTES))
      .subject(Long.toString(2))
      .audience(Arrays.asList("JuliaJewelsApp"));
    consumer.accept(builder);
    JwtEncoderParameters parameters = JwtEncoderParameters.from(
      builder.build()
    );
    return this.jwtEncoder.encode(parameters).getTokenValue();
  }
}
