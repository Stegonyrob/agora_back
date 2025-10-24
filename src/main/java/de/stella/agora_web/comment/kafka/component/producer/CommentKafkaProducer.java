package de.stella.agora_web.comment.kafka.component.producer;

import de.stella.agora_web.comment.kafka.dto.CommentNotificationDTO;

/**
 * Interfaz para el producer de Kafka - será implementada según la configuración
 * Puede tener implementación real (cuando Kafka está habilitado) o dummy
 * (cuando está deshabilitado)
 */
public interface CommentKafkaProducer {

    /**
     * Envía una notificación de comentario a través de Kafka
     *
     * @param notification Datos del comentario para la notificación al admin
     */
    void sendCommentNotification(CommentNotificationDTO notification);
}
