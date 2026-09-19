package innovation.ride.umusare.dto;

import innovation.ride.umusare.entity.enums.TransmissionType;
import innovation.ride.umusare.entity.enums.VehicleType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleResponseDTO {
    private String vehicleId;
    private String plateNumber;
    private String model;
    private String color;
    private TransmissionType transmission;
    private VehicleType vehicleType;
    private boolean active;
}
