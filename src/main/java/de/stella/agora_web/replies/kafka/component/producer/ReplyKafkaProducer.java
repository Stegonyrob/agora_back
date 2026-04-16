package de.stella.agora_web.replies.kafka.component.producer;

import de.stella.agora_web.replies.kafka.dto.ReplyNotificationDTO;

/**
 * Interfaz para el producer de Kafka para replies - será implementada según la
 * configuración
 */
public abstract class ReplyKafkaProducer {

    public abstract void sendReplyNotification(ReplyNotificationDTO notification);
}
