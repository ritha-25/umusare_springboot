package innovation.ride.umusare.dtos;

import innovation.ride.umusare.entity.enums.LocationType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationRequestDTO {
    private String locationName;
    private String locationCode;
    private LocationType type;
    private String parentLocationId;
}