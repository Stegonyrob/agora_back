package de.stella.agora_web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;

import de.stella.agora_web.comment.kafka.component.producer.CommentKafkaProducer;
import de.stella.agora_web.comment.kafka.dto.CommentNotificationDTO;
import de.stella.agora_web.replies.kafka.component.producer.ReplyKafkaProducer;
import de.stella.agora_web.replies.kafka.dto.ReplyNotificationDTO;

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

    @Bean
    public ReplyKafkaProducer replyKafkaProducer(@Autowired KafkaTemplate<String, ReplyNotificationDTO> kafkaTemplate) {
        return new ReplyKafkaProducer() {
            @Override
            public void sendReplyNotification(ReplyNotificationDTO notification) {
                kafkaTemplate.send("replies", notification);
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

    /**
     * Bean dummy para Reply producer cuando Kafka está deshabilitado
     */
    @Bean
    public ReplyKafkaProducer replyKafkaProducer() {
        return new ReplyKafkaProducer() {
            @Override
            public void sendReplyNotification(ReplyNotificationDTO notification) {
                // No hacer nada cuando Kafka está deshabilitado
            }
        };
    }
}
