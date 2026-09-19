package innovation.ride.umusare.dto;

import innovation.ride.umusare.entity.enums.TransmissionType;
import innovation.ride.umusare.entity.enums.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleRequestDTO {

    @NotBlank(message = "Plate number is required")
    private String plateNumber;

    @NotBlank(message = "Model is required")
    private String model;

    private String color;

    @NotNull(message = "Transmission irequired")
    private TransmissionType transmission;

    @NotNull(message = "Vehicle type is required")
    private VehicleType vehicleType;
}
