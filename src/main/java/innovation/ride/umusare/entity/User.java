package innovation.ride.umusare.entity;

import innovation.ride.umusare.entity.enums.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User extends Audit {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(nullable = false, unique = true)
    private String nid;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;
}
