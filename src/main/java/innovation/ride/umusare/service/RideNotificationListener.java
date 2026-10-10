package innovation.ride.umusare.service;

import innovation.ride.umusare.config.Rabbitmq;
import innovation.ride.umusare.dtos.RideEventMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RideNotificationListener {

    private static final Logger log = LoggerFactory.getLogger(RideNotificationListener.class);

    @RabbitListener(queues = Rabbitmq.QUEUE)
    public void handleRideEvent(RideEventMessage message) {
        try {
            log.info("NOTIFICATION [{}] to {} <{}>: {}",
                    message.getEventType(),
                    message.getRecipientName(),
                    message.getRecipientEmail(),
                    message.getDetails());
        } catch (Exception e) {
            log.error("Failed to process notification event {}: {}", message.getEventType(), e.getMessage(), e);
        }
    }
}
