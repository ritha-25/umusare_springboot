package innovation.ride.umusare.dto;

import innovation.ride.umusare.entity.enums.RideStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RideResponseDTO {
    private String rideId;
    private RideStatus status;
    private Double price;
    private String pickupLocation;
    private String destinationLocation;
    private String vehiclePlateNumber;
    private String driverName;
    private LocalDateTime requestedAt;
}
