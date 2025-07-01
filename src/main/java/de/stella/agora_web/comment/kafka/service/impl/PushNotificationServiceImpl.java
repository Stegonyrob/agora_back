package de.stella.agora_web.comment.kafka.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.stella.agora_web.comment.kafka.service.IPushNotificationService;
import de.stella.agora_web.messages.PushNotificationMessage;

@Service // Habilitado para trabajar con Kafka
public class PushNotificationServiceImpl implements IPushNotificationService {

    @Autowired // Habilitado para trabajar con Kafka
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sendPushNotification(String topic, String title, String message) {
        // Crear un mensaje de push y enviarlo a través de Kafka
        PushNotificationMessage pushNotificationMessage = new PushNotificationMessage(topic, title, message);
        ObjectMapper mapper = new ObjectMapper();
        String jsonMessage;
        try {
            jsonMessage = mapper.writeValueAsString(pushNotificationMessage);
            kafkaTemplate.send("push-notifications", jsonMessage);
            System.out.println("Push notification sent to Kafka topic 'push-notifications': " + jsonMessage);
        } catch (JsonProcessingException e) {
            System.err.println("Error serializing push notification message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
