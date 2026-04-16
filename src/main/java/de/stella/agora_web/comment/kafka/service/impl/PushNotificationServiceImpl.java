package de.stella.agora_web.comment.kafka.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.stella.agora_web.comment.kafka.service.IPushNotificationService;
import de.stella.agora_web.messages.PushNotificationMessage;

@Service // Habilitado para trabajar con Kafka
public class PushNotificationServiceImpl implements IPushNotificationService {

    private static final Logger log = LoggerFactory.getLogger(PushNotificationServiceImpl.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    public PushNotificationServiceImpl(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendPushNotification(String topic, String title, String message) {
        // Crear un mensaje de push y enviarlo a través de Kafka
        PushNotificationMessage pushNotificationMessage = new PushNotificationMessage(topic, title, message);
        ObjectMapper mapper = new ObjectMapper();
        String jsonMessage;
        try {
            jsonMessage = mapper.writeValueAsString(pushNotificationMessage);
            kafkaTemplate.send("push-notifications", jsonMessage);
            log.info("Push notification sent to Kafka topic 'push-notifications': {}", jsonMessage);
        } catch (JsonProcessingException e) {
            log.error("Error serializing push notification message: {}", e.getMessage(), e);
        }
    }
}
