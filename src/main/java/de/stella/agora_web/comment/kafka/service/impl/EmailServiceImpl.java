package de.stella.agora_web.comment.kafka.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.stella.agora_web.comment.kafka.service.IEmailService;
import de.stella.agora_web.messages.EmailMessage;

@Service // Habilitado para trabajar con Kafka
public class EmailServiceImpl implements IEmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public EmailServiceImpl(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendEmail(String toAddress, String subject, String body) {
        // Crear un mensaje de email y enviarlo a través de Kafka
        EmailMessage emailMessage = new EmailMessage(toAddress, subject, body);
        try {
            String emailMessageJson = objectMapper.writeValueAsString(emailMessage);
            kafkaTemplate.send("emails", emailMessageJson);
            log.info("Email notification sent to Kafka topic 'emails': {}", emailMessageJson);
        } catch (JsonProcessingException e) {
            log.error("Error serializing email message: {}", e.getMessage(), e);
        }
    }
}
