package de.stella.agora_web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;

import de.stella.agora_web.comment.kafka.component.producer.CommentKafkaProducer;
import de.stella.agora_web.comment.kafka.dto.CommentNotificationDTO;

/**
 * Configuración para cuando Kafka está habilitado
 */
@Configuration
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true", matchIfMissing = false)
@EnableKafka
public class KafkaConfig {

    @Bean
    public CommentKafkaProducer commentKafkaProducer(@Autowired KafkaTemplate<String, CommentNotificationDTO> kafkaTemplate) {
        return new CommentKafkaProducer() {
            @Override
            public void sendCommentNotification(CommentNotificationDTO notification) {
                kafkaTemplate.send("comments", notification);
            }
        };
    }
}

/**
 * Configuración para cuando Kafka está deshabilitado
 */
@Configuration
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "false", matchIfMissing = true)
class KafkaDisabledConfig {

    /**
     * Bean dummy que no hace nada cuando Kafka está deshabilitado
     */
    @Bean
    public CommentKafkaProducer commentKafkaProducer() {
        return new CommentKafkaProducer() {
            @Override
            public void sendCommentNotification(CommentNotificationDTO notification) {
                // No hacer nada cuando Kafka está deshabilitado
            }
        };
    }
}
