package de.stella.agora_web.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
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

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import de.stella.agora_web.auth.KeyUtils;
import de.stella.agora_web.jwt.JWTtoUserConverter;
import de.stella.agora_web.user.services.JpaUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  @Value("${api-endpoint}")
  String endpoint;

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
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors(Customizer.withDefaults()).csrf(csrf -> csrf.disable()).formLogin(form -> form.disable())
        .logout(out -> out.logoutUrl(endpoint + "/logout").deleteCookies("JSESSIONID"))
        .authorizeHttpRequests(auth -> auth.requestMatchers(PathRequest.toH2Console()).permitAll()
            .requestMatchers("/error").permitAll().requestMatchers(endpoint + "/all/**").permitAll()
            .requestMatchers(endpoint + "/any/**").hasAnyRole("ADMIN", "USER").requestMatchers(endpoint + "/admin/**")
            .hasRole("ADMIN").requestMatchers(endpoint + "/user/**").hasRole("USER").anyRequest().permitAll())

        .userDetailsService(jpaUserDetailsService).httpBasic(basic -> basic.disable())
        .oauth2ResourceServer((oauth2) -> oauth2.jwt((jwt) -> jwt.jwtAuthenticationConverter(jwtToUserConverter)))
        .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(
            (exceptions) -> exceptions.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                .accessDeniedHandler(new BearerTokenAccessDeniedHandler()));

    http.headers(header -> header.frameOptions(frame -> frame.sameOrigin()));

    return http.build();
  }

  @Bean
  @Primary
  JwtDecoder jwtAccessTokenDecoder() {
    return NimbusJwtDecoder.withPublicKey(keyUtils.getAccessTokenPublicKey()).build();
  }

  @Bean
  @Primary
  JwtEncoder jwtAccessTokenEncoder() {
    JWK jwk = new RSAKey.Builder(keyUtils.getAccessTokenPublicKey()).privateKey(keyUtils.getAccessTokenPrivateKey())
        .build();
    JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
    return new NimbusJwtEncoder(jwks);
  }

  @Bean
  @Qualifier("jwtRefreshTokenDecoder")
  JwtDecoder jwtRefreshTokenDecoder() {
    return NimbusJwtDecoder.withPublicKey(keyUtils.getRefreshTokenPublicKey()).build();
  }

  @Bean
  @Qualifier("jwtRefreshTokenEncoder")
  JwtEncoder jwtRefreshTokenEncoder() {
    JWK jwk = new RSAKey.Builder(keyUtils.getRefreshTokenPublicKey()).privateKey(keyUtils.getRefreshTokenPrivateKey())
        .build();
    JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
    return new NimbusJwtEncoder(jwks);
  }

  @Bean
  @Qualifier("jwtRefreshTokenAuthProvider")
  JwtAuthenticationProvider jwtRefreshTokenAuthProvider() {
    JwtAuthenticationProvider provider = new JwtAuthenticationProvider(jwtRefreshTokenDecoder());
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

    configuration
        .setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:5174", "http://localhost:8080"));

    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
    configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration(("/**"), configuration);
    return source;
  }

}