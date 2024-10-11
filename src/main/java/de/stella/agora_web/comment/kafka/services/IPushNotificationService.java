package de.stella.agora_web.comment.kafka.services;

public interface IPushNotificationService {
    void sendPushNotification(String topic, String title, String message);
}