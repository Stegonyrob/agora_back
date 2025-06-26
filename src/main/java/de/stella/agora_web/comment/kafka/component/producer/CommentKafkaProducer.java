package de.stella.agora_web.comment.kafka.component.producer;

import de.stella.agora_web.comment.kafka.dto.CommentNotificationDTO;

/**
 * Interfaz para el producer de Kafka - será implementada según la configuración
 */
public abstract class CommentKafkaProducer {

    public abstract void sendCommentNotification(CommentNotificationDTO notification);
}
