package innovation.ride.umusare.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    private String phoneNumber;
    private String password;
}