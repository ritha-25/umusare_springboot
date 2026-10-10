package innovation.ride.umusare.dtos;

import innovation.ride.umusare.entity.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PricingResponseDTO {
    private Long id;
    private String fromLocationId;
    private String fromLocationName;
    private String toLocationId;
    private String toLocationName;
    private VehicleType vehicleType;
    private Double price;
    private boolean active;
}
