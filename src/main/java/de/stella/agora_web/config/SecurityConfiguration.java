package de.stella.agora_web.config;

import java.util.Arrays;

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

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import de.stella.agora_web.auth.KeyUtils;
import de.stella.agora_web.jwt.JWTtoUserConverter;
import de.stella.agora_web.security.JpaUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    private static final String ADMIN = "ADMIN";
    private static final String COMMENTS_PATH = "/comments/**";

    @Value("${api-endpoint}")
    String endpoint;

    private final JWTtoUserConverter jwtToUserConverter;
    private final KeyUtils keyUtils;
    private final JpaUserDetailsService jpaUserDetailsService;

    public SecurityConfiguration(JpaUserDetailsService jpaUserDetailsService,
            JWTtoUserConverter jwtToUserConverter,
            KeyUtils keyUtils) {
        this.jpaUserDetailsService = jpaUserDetailsService;
        this.jwtToUserConverter = jwtToUserConverter;
        this.keyUtils = keyUtils;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults()).csrf(csrf -> csrf.disable()).formLogin(form -> form.disable())
                .logout(logout -> logout.logoutUrl(endpoint + "/logout").deleteCookies("JSESSIONID"))
                .authorizeHttpRequests(auth -> auth.requestMatchers(PathRequest.toH2Console()).permitAll()
                .requestMatchers(PathRequest.toH2Console()).permitAll()
                .requestMatchers("/error").permitAll()
                .requestMatchers(endpoint + "/all/**").permitAll()
                .requestMatchers(endpoint + "/public/**").permitAll() // ✅ PERMITIR ACCESO PÚBLICO A ENDPOINTS PÚBLICOS
                .requestMatchers(endpoint + "/any/user/settings/**").permitAll() // ✅ PERMITIR ACCESO PÚBLICO A SETTINGS
                .requestMatchers(HttpMethod.GET, "/posts/**").hasAnyRole("USER", ADMIN)
                .requestMatchers("/posts/**").hasRole(ADMIN)
                .requestMatchers(HttpMethod.GET, "/replies/**").hasAnyRole("USER", ADMIN)
                .requestMatchers("/replies/**").hasRole(ADMIN)
                .requestMatchers(HttpMethod.GET, COMMENTS_PATH).hasAnyRole(ADMIN, "USER")
                .requestMatchers(HttpMethod.PUT, COMMENTS_PATH).hasAnyRole(ADMIN, "USER")
                .requestMatchers(COMMENTS_PATH).hasRole(ADMIN)
                .requestMatchers(endpoint + "/any/**").hasAnyRole(ADMIN, "USER")
                .requestMatchers(endpoint + "/admin/**").hasRole(ADMIN)
                .requestMatchers(endpoint + "/user/**").hasRole("USER")
                .requestMatchers(HttpMethod.GET, "/legal/**").hasRole("USER") // <-- aquí
                .requestMatchers("/legal/**").hasRole(ADMIN) // <-- aquí
                // ========== CONFIGURACIÓN DE IMÁGENES ESTÁTICAS ==========
                .requestMatchers(HttpMethod.GET, "/temp_images/**").permitAll() // Allow public access to static images
                // ========== CONFIGURACIÓN DE ENDPOINTS DE IMÁGENES - REPLICANDO PATRÓN TEXT-IMAGES ==========
                // Event-images: GET públicos (igual que text-images), resto ADMIN
                .requestMatchers(HttpMethod.GET, endpoint + "/event-images/**").permitAll()
                .requestMatchers(endpoint + "/event-images/**").hasRole(ADMIN)
                // Post-images: GET requiere autenticación USER/ADMIN (posts privados), resto ADMIN  
                .requestMatchers(HttpMethod.GET, endpoint + "/post-images/**").hasAnyRole("USER", ADMIN)
                .requestMatchers(endpoint + "/post-images/**").hasRole(ADMIN)
                // Text-images: GET públicos (ya configurado implícitamente), resto ADMIN
                .requestMatchers(HttpMethod.GET, endpoint + "/text-images/**").permitAll()
                .requestMatchers(endpoint + "/text-images/**").hasRole(ADMIN)
                .anyRequest().permitAll()
                )
                .userDetailsService(jpaUserDetailsService).httpBasic(basic -> basic.disable())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtToUserConverter)))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                .accessDeniedHandler(new BearerTokenAccessDeniedHandler()));

        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    @Bean
    @Primary
    @SuppressWarnings("unused")
    JwtDecoder jwtAccessTokenDecoder() {
        return NimbusJwtDecoder.withPublicKey(keyUtils.getAccessTokenPublicKey()).build();
    }

    @Bean
    @Primary
    @SuppressWarnings("unused")
    JwtEncoder jwtAccessTokenEncoder() {
        JWK jwk = new RSAKey.Builder(keyUtils.getAccessTokenPublicKey()).privateKey(keyUtils.getAccessTokenPrivateKey())
                .build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    JwtDecoder jwtRefreshTokenDecoder() {
        return NimbusJwtDecoder.withPublicKey(keyUtils.getRefreshTokenPublicKey()).build();
    }

    @Bean
    @SuppressWarnings("unused")
    JwtEncoder jwtRefreshTokenEncoder() {
        JWK jwk = new RSAKey.Builder(keyUtils.getRefreshTokenPublicKey()).privateKey(keyUtils.getRefreshTokenPrivateKey())
                .build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    @SuppressWarnings("unused")
    JwtAuthenticationProvider jwtRefreshTokenAuthProvider() {
        JwtAuthenticationProvider provider = new JwtAuthenticationProvider(jwtRefreshTokenDecoder());
        provider.setJwtAuthenticationConverter(jwtToUserConverter);
        return provider;
    }

    @Bean
    @SuppressWarnings("unused")
    DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(jpaUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    @SuppressWarnings("unused")
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);

        configuration
                .setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:5174", "http://localhost:8080"));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(("/**"), configuration);
        return source;
    }
}
