package innovation.ride.umusare.dtos;

import innovation.ride.umusare.entity.enums.TransmissionType;
import innovation.ride.umusare.entity.enums.VehicleType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class DriverProfileRequestDTO {
    private Set<String> serviceAreaIds;
    private List<VehicleSkillDTO> vehicleSkills;

    @Getter
    @Setter
    public static class VehicleSkillDTO {
        private VehicleType vehicleType;
        private TransmissionType transmission;
    }
}
