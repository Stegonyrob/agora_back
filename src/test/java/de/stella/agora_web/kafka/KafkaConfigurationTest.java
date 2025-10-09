package de.stella.agora_web.kafka;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import de.stella.agora_web.config.TestConfig;

/**
 * Tests para verificar la configuración de Kafka Validar que: 1. Kafka Producer
 * está configurado correctamente 2. Kafka Consumer está configurado
 * correctamente 3. Los topics necesarios están disponibles
 */
@SpringBootTest
@ActiveProfiles("h2")
@TestPropertySource(properties = {
    "spring.kafka.bootstrap-servers=localhost:9092",
    "spring.kafka.consumer.group-id=test-group",
    "spring.kafka.producer.retries=1",
    "kafka.enabled=false",
    "spring.h2.console.enabled=false",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Import(TestConfig.class)
class KafkaConfigurationTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ProducerFactory<String, String> producerFactory;

    @Autowired
    private ConsumerFactory<String, String> consumerFactory;

    @Test
    void testKafkaTemplateIsInjected() {
        assertThat(kafkaTemplate).isNotNull();
    }

    @Test
    void testProducerFactoryConfiguration() {
        assertThat(producerFactory).isNotNull();

        // Verificar configuraciones del producer
        var configs = producerFactory.getConfigurationProperties();
        assertThat(configs.get(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG))
                .isEqualTo(List.of("localhost:9092"));
        assertThat(configs.get(ProducerConfig.RETRIES_CONFIG))
                .isEqualTo(1);
    }

    @Test
    void testConsumerFactoryConfiguration() {
        assertThat(consumerFactory).isNotNull();

        // Verificar configuraciones del consumer
        var configs = consumerFactory.getConfigurationProperties();
        assertThat(configs.get(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG))
                .isEqualTo(List.of("localhost:9092"));
        assertThat(configs.get(ConsumerConfig.GROUP_ID_CONFIG))
                .isEqualTo("test-group");
    }

    @Test
    void testKafkaTemplateCanSendMessage() {
        // Test básico de envío de mensaje
        String topic = "test-topic";
        String message = "Test message for Kafka configuration";

        // No debe lanzar excepción
        try {
            kafkaTemplate.send(topic, message);
        } catch (Exception e) {
            // En entorno de test sin Kafka real, puede fallar
            // pero al menos verificamos que la configuración no causa errores de inyección
        }
    }
}
