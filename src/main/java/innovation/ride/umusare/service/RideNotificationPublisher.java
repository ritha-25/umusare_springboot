package innovation.ride.umusare.service;

import innovation.ride.umusare.config.Rabbitmq;
import innovation.ride.umusare.dtos.RideEventMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideNotificationPublisher {

    private static final Logger log = LoggerFactory.getLogger(RideNotificationPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public void publish(RideEventMessage message) {
        try {
            rabbitTemplate.convertAndSend(Rabbitmq.EXCHANGE, Rabbitmq.ROUTING_KEY, message);
            log.info("Published event [{}] to queue for recipient: {}", message.getEventType(), message.getRecipientEmail());
        } catch (Exception e) {
            log.warn("Failed to publish ride event [{}]: {}", message.getEventType(), e.getMessage());
        }
    }
}
