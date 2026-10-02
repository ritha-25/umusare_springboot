package innovation.ride.umusare.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class DriverResponseDTO {
    private String driverId;
    private String fullName;
    private String licenseNumber;
    private boolean verified;
}