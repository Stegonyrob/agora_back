package de.stella.agora_web.comment.kafka.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.stella.agora_web.comment.kafka.services.IPushNotificationService;
import de.stella.agora_web.messages.PushNotificationMessage;

@Service
public class PushNotificationServiceImpl implements IPushNotificationService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sendPushNotification(String topic, String title, String message) {
        // Crear un mensaje de push y enviarlo a través de Kafka
        PushNotificationMessage pushNotificationMessage = new PushNotificationMessage(topic, title, message);
        ObjectMapper mapper = new ObjectMapper();
        String jsonMessage;
        try {
            jsonMessage = mapper.writeValueAsString(pushNotificationMessage);
        } catch (JsonProcessingException e) {
            // Handle the exception
            return;
        }
        kafkaTemplate.send("push-notifications", jsonMessage);
    }
}