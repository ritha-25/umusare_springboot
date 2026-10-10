package innovation.ride.umusare.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    private String identifier; // accepts either phone number or email
    private String password;
}