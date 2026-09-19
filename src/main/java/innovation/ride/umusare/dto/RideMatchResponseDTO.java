package innovation.ride.umusare.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RideMatchResponseDTO {

    private Double estimatedPrice;


    private DriverMatchDTO matchedDriver;
}
