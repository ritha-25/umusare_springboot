package innovation.ride.umusare.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmRideRequestDTO {

    @NotBlank(message = "Vehicle is required")
    private String vehicleId;

    @NotBlank(message = "Pickup location is required")
    private String pickupLocationId;

    private String pickupLandmark;

    @NotBlank(message = "Destination location is required")
    private String destinationLocationId;

    private String destinationLandmark;

    @NotBlank(message = "Confirmed driver is required")
    private String driverId;
}
