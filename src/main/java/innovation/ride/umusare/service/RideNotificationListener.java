package innovation.ride.umusare.service;

import innovation.ride.umusare.config.Rabbitmq;
import innovation.ride.umusare.dtos.RideEventMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RideNotificationListener {

    private static final Logger log = LoggerFactory.getLogger(RideNotificationListener.class);

    private final EmailService emailService;

    @RabbitListener(queues = Rabbitmq.QUEUE)
    public void handleRideEvent(RideEventMessage message) {
        try {
            String subject = resolveSubject(message.getEventType());
            emailService.send(message.getRecipientEmail(), subject, message.getDetails());
            log.info("Notification [{}] sent to {}", message.getEventType(), message.getRecipientEmail());
        } catch (Exception e) {
            log.error("Failed to process notification [{}]: {}", message.getEventType(), e.getMessage());
        }
    }

    private String resolveSubject(String eventType) {
        return switch (eventType) {
            case "PASSENGER_REGISTERED" -> "Umusare - Registration Successful";
            case "DRIVER_REGISTERED" -> "Umusare - Application Received";
            case "DRIVER_APPROVED" -> "Umusare - Application Approved";
            case "DRIVER_REJECTED" -> "Umusare - Application Update";
            case "RIDE_REQUESTED" -> "Umusare - New Ride Request";
            default -> "Umusare - Notification";
        };
    }
}
