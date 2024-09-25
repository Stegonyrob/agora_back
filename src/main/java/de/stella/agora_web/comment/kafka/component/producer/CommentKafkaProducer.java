package de.stella.agora_web.comment.kafka.component.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import de.stella.agora_web.comment.kafka.dto.CommentNotificationDTO;

// de.stella.agora_web.comment.kafka
@Component
public class CommentKafkaProducer {

    @Autowired
    private KafkaTemplate<String, CommentNotificationDTO> kafkaTemplate;

    public void sendCommentNotification(CommentNotificationDTO notification) {
        kafkaTemplate.send("comments", notification);
    }
}