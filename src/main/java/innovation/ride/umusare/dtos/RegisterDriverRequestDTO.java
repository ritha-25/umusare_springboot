package innovation.ride.umusare.dtos;

import innovation.ride.umusare.entity.enums.Gender;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RegisterDriverRequestDTO {
    private String fullName;
    private String nid;
    private String phoneNumber;
    private String password;
    private Gender gender;
    private String licenseNumber;
    private LocalDate licenseExpiry;
}