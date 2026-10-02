package innovation.ride.umusare.dtos;

import innovation.ride.umusare.entity.enums.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterPassengerRequestDTO {
    private String fullName;
    private String nid;
    private String phoneNumber;
    private String password;
    private Gender gender;
}