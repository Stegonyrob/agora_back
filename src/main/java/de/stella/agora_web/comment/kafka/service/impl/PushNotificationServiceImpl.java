package de.stella.agora_web.comment.kafka.service.impl;

import org.springframework.kafka.core.KafkaTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.stella.agora_web.comment.kafka.service.IPushNotificationService;
import de.stella.agora_web.messages.PushNotificationMessage;

// @Service // TEMPORALMENTE DESHABILITADO - Habilitar cuando Kafka esté configurado
public class PushNotificationServiceImpl implements IPushNotificationService {

    // @Autowired // TEMPORALMENTE DESHABILITADO
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
