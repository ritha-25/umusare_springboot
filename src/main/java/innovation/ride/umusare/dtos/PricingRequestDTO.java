package innovation.ride.umusare.dtos;

import innovation.ride.umusare.entity.enums.VehicleType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PricingRequestDTO {
    private String fromLocationId;
    private String toLocationId;
    private VehicleType vehicleType;
    private Double price;
}
