package de.stella.agora_web.replies.kafka.component.consumer;

/**
 * Reservado para una futura implementación alternativa del consumidor de
 * replies. El consumidor activo es {@link ReplyNotificationConsumer}.
 *
 * No añadir @KafkaListener aquí: causaría conflicto con
 * ReplyNotificationConsumer al competir por los mismos mensajes del topic
 * "replies".
 */
public class ReplyNotificationConsumerEnabled {
    // No instanciar — ver ReplyNotificationConsumer
}
