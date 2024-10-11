package de.stella.agora_web.messages;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PushNotificationMessage {
    private String topic;
    private String title;
    private String message;

    public PushNotificationMessage(String topic, String title, String message) {
        this.topic = topic;
        this.title = title;
        this.message = message;
    }

}