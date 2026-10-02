package innovation.ride.umusare.dtos;

import innovation.ride.umusare.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String userId;
    private String fullName;
    private Role role;
}