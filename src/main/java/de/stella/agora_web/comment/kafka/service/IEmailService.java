package de.stella.agora_web.comment.kafka.service;

public interface IEmailService {
    void sendEmail(String toAddress, String subject, String body);
}