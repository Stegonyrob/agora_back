package de.stella.agora_web.comment.kafka.services;

public interface IEmailService {
    void sendEmail(String toAddress, String subject, String body);
}