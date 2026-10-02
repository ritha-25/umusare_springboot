package innovation.ride.umusare.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StartRideRequestDTO {
    private Double startOdometer;
    private String carConditionNotes;
}