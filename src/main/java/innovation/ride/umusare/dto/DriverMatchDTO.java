package innovation.ride.umusare.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverMatchDTO {
    private String driverId;
    private String fullName;
    private boolean verified;
    private Double averageRating;
}
