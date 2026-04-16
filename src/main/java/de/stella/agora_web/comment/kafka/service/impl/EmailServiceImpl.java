package de.stella.agora_web.comment.kafka.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import de.stella.agora_web.comment.kafka.service.IEmailService;

/**
 * Implementación de envío de email usando JavaMailSender (SMTP / AWS SES).
 * Cuando SMTP no está configurado (entorno local/dev), registra el email como
 * WARN en lugar de fallar, para no bloquear el flujo de la aplicación.
 */
@Service
public class EmailServiceImpl implements IEmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:noreply@agora.es}")
    private String fromAddress;

    public EmailServiceImpl(@Autowired(required = false) JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String toAddress, String subject, String body) {
        if (mailSender == null) {
            log.warn("SMTP no configurado — email no enviado a '{}'. Asunto: '{}'", toAddress, subject);
            return;
        }
        if (toAddress == null || toAddress.isBlank()) {
            log.warn("Dirección de destino vacía — email descartado. Asunto: '{}'", subject);
            return;
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(toAddress);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            log.info("Email enviado a '{}': {}", toAddress, subject);
        } catch (MailException e) {
            log.error("Error enviando email a '{}': {}", toAddress, e.getMessage(), e);
        }
    }
}
