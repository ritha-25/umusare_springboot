package innovation.ride.umusare.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RideEventMessage {
    private String rideId;
    private String eventType;
    private String recipientEmail;
    private String recipientName;
    private String details;
}