package de.stella.agora_web.comment.kafka.service;

public interface IPushNotificationService {
    void sendPushNotification(String topic, String title, String message);
}