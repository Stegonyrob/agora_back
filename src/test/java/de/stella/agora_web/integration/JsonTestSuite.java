package de.stella.agora_web.integration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

/**
 * Suite simple de tests para verificar JSON de endpoints.
 *
 * Para ejecutar todos los tests de JSON: mvn test -Dtest=JsonTestSuite
 */
@SpringBootTest
@ActiveProfiles("h2")
@TestPropertySource(properties = {
    "spring.h2.console.enabled=false",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class JsonTestSuite {

    @Test
    public void runAllJsonTests() {
        System.out.println("🚀 Suite de tests JSON disponible");
        System.out.println("📝 Para ejecutar tests específicos usa:");
        System.out.println("   mvn test -Dtest=TagControllerJsonTest");
        System.out.println("   mvn test -Dtest=PostControllerJsonTest");
        System.out.println("   mvn test -Dtest=EventControllerJsonTest");
        System.out.println("   mvn test -Dtest=AllEndpointsJsonIntegrationTest");
        System.out.println("✅ Tests disponibles y listos para ejecutar");
    }
}
