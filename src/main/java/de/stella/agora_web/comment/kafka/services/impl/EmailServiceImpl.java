package de.stella.agora_web.comment.kafka.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.stella.agora_web.comment.kafka.services.IEmailService;
import de.stella.agora_web.messages.EmailMessage;

@Service
public class EmailServiceImpl implements IEmailService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void sendEmail(String toAddress, String subject, String body) {
        // Crear un mensaje de email y enviarlo a través de Kafka
        EmailMessage emailMessage = new EmailMessage(toAddress, subject, body);
        try {
            String emailMessageJson = objectMapper.writeValueAsString(emailMessage);
            kafkaTemplate.send("emails", emailMessageJson);
        } catch (JsonProcessingException e) {
            // Handle the exception
        }
    }
}