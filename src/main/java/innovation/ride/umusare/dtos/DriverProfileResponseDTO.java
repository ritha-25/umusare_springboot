package innovation.ride.umusare.dtos;

import innovation.ride.umusare.entity.enums.TransmissionType;
import innovation.ride.umusare.entity.enums.VehicleType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
public class DriverProfileResponseDTO {
    private String driverId;
    private String fullName;
    private boolean verified;
    private boolean available;
    private Double averageRating;
    private Set<String> serviceAreaIds;
    private List<VehicleSkillInfo> vehicleSkills;

    @Getter
    @Setter
    @Builder
    public static class VehicleSkillInfo {
        private VehicleType vehicleType;
        private TransmissionType transmission;
    }
}
