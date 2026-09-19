package innovation.ride.umusare.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RideMatchRequestDTO {

    @NotBlank(message = "Vehicle is required")
    private String vehicleId;

    @NotBlank(message = "Pickup location is required")
    private String pickupLocationId;

    private String pickupLandmark;

    @NotBlank(message = "Destination location is required")
    private String destinationLocationId;

    private String destinationLandmark;

    private Set<String> excludedDriverIds;
}
