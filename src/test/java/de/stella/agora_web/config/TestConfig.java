package de.stella.agora_web.config;

import org.springframework.boot.autoconfigure.h2.H2ConsoleProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * Configuración de test para proporcionar beans necesarios durante las pruebas.
 *
 * Esta configuración resuelve el problema de H2ConsoleProperties no disponible
 * en el contexto de test cuando se usa el perfil h2.
 */
@TestConfiguration
public class TestConfig {

    /**
     * Proporciona un bean H2ConsoleProperties para tests.
     *
     * @return H2ConsoleProperties configurado para tests
     */
    @Bean
    @Primary
    H2ConsoleProperties h2ConsoleProperties() {
        H2ConsoleProperties properties = new H2ConsoleProperties();
        properties.setEnabled(false); // Deshabilitado en tests
        return properties;
    }
}
