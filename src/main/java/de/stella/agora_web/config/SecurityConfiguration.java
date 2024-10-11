package de.stella.agora_web.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import de.stella.agora_web.auth.KeyUtils;
import de.stella.agora_web.jwt.JWTtoUserConverter;
import de.stella.agora_web.security.JpaUserDetailsService;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

  @Value("${api-endpoint}")
  String endpoint;

  @Value("${jwt-issuer}")
  String issuer;

  @Value("${jwt-audience}")
  String audience;

  @Autowired
  JWTtoUserConverter jwtToUserConverter;

  @Autowired
  KeyUtils keyUtils;

  @Autowired
  PasswordEncoder passwordEncoder;

  JpaUserDetailsService jpaUserDetailsService;

  public SecurityConfiguration(JpaUserDetailsService jpaUserDetailsService) {
    this.jpaUserDetailsService = jpaUserDetailsService;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
    throws Exception {
    http
      .cors(Customizer.withDefaults())
      .csrf(csrf -> csrf.disable())
      .formLogin(form -> form.disable())
      .logout(out ->
        out.logoutUrl(endpoint + "/logout").deleteCookies("JSESSIONID")
      )
      .authorizeHttpRequests(authorize ->
        authorize
          .requestMatchers(PathRequest.toH2Console())
          .permitAll()
          .requestMatchers("/error") //recordar que va 1/mas especifica 2/mas abierta 3/las generales
          .permitAll()
          .requestMatchers(endpoint + "/all/**")
          .permitAll()
          // Permite GET para USER en Posts
          .requestMatchers(HttpMethod.GET, "/posts/**")
          .hasRole("USER")
          // Permite todos los métodos para ADMIN en Posts
          .requestMatchers("/posts/**")
          .hasRole("ADMIN")
          // Permite GET para USER en Replies
          .requestMatchers(HttpMethod.GET, "/replies/**")
          .hasRole("USER")
          // Permite todos los métodos para ADMIN en replies
          .requestMatchers("/replies/**")
          .hasRole("ADMIN")
          // Permite GET para USER en comment
          .requestMatchers("/comments/**")
          .hasRole("USER")
          // Permitir GET y DELETE para ADMIN
          .requestMatchers(HttpMethod.GET, "/comments/**")
          .hasRole("ADMIN")
          .requestMatchers(HttpMethod.DELETE, "/comments/**")
          .hasRole("ADMIN")
          // Permite todos los métodos para ADMIN en comment
          .requestMatchers("/posts/**")
          .hasRole("ADMIN")
          .requestMatchers(endpoint + "/any/**") //(get endpoint /posts).hasAnyRoles(admin, user)
          .hasAnyRole("ADMIN", "USER") //(enpoitns/post/**).hasRole(ADMIN) revisar docuemntacion pra no tener que poner los cuatro metods
          .requestMatchers(endpoint + "/admin/**")
          .hasRole("ADMIN")
          .requestMatchers(endpoint + "/user/**")
          .hasRole("USER")
          .anyRequest()
          .permitAll() //cambiar a autneticado .authenticated()
      )
      .userDetailsService(jpaUserDetailsService)
      .httpBasic(basic -> basic.disable())
      .oauth2ResourceServer(oauth2 ->
        oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtToUserConverter))
      )
      .sessionManagement(session ->
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      )
      .exceptionHandling(exceptions ->
        exceptions
          .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
          .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
      );

    http.headers(header -> header.frameOptions(frame -> frame.sameOrigin()));

    return http.build();
  }

  @Bean
  @Primary
  JwtDecoder jwtAccessTokenDecoder() {
    return NimbusJwtDecoder
      .withPublicKey(keyUtils.getAccessTokenPublicKey())
      .build();
  }

  @Bean
  @Primary
  JwtEncoder jwtAccessTokenEncoder() {
    JWK jwk = new RSAKey.Builder(keyUtils.getAccessTokenPublicKey())
      .privateKey(keyUtils.getAccessTokenPrivateKey())
      .build();
    JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
    return new NimbusJwtEncoder(jwks);
  }

  @Bean
  @Qualifier("jwtRefreshTokenDecoder")
  JwtDecoder jwtRefreshTokenDecoder() {
    return NimbusJwtDecoder
      .withPublicKey(keyUtils.getRefreshTokenPublicKey())
      .build();
  }

  @Bean
  @Qualifier("jwtRefreshTokenEncoder")
  JwtEncoder jwtRefreshTokenEncoder() {
    JWK jwk = new RSAKey.Builder(keyUtils.getRefreshTokenPublicKey())
      .privateKey(keyUtils.getRefreshTokenPrivateKey())
      .build();
    JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
    return new NimbusJwtEncoder(jwks);
  }

  @Bean
  @Qualifier("jwtRefreshTokenAuthProvider")
  JwtAuthenticationProvider jwtRefreshTokenAuthProvider() {
    JwtAuthenticationProvider provider = new JwtAuthenticationProvider(
      jwtRefreshTokenDecoder()
    );
    provider.setJwtAuthenticationConverter(jwtToUserConverter);
    return provider;
  }

  @Bean
  DaoAuthenticationProvider daoAuthenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder);
    provider.setUserDetailsService(jpaUserDetailsService);
    return provider;
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowCredentials(true);

    configuration.setAllowedOrigins(
      Arrays.asList(
        "http://localhost:5173",
        "http://localhost:5174",
        "http://localhost:8080"
      )
    );

    configuration.setAllowedMethods(
      Arrays.asList("GET", "POST", "PUT", "DELETE")
    );
    configuration.setAllowedHeaders(
      Arrays.asList("Content-Type", "Authorization")
    );
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration(("/**"), configuration);
    return source;
  }
  // @Bean
  // PasswordEncoder passwordEncoder() {
  //   return new BCryptPasswordEncoder();
  // }
}
