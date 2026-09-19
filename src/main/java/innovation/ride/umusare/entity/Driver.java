package innovation.ride.umusare.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "drivers")
@PrimaryKeyJoinColumn(name = "user_id")
public class Driver extends User {

    @Column(name = "license_number", nullable = false, unique = true)
    private String licenseNumber;

    @Column(name = "license_expiry")
    private LocalDate licenseExpiry;

    private boolean verified = false;

    private boolean available = false;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "driver_service_area",
            joinColumns = @JoinColumn(name = "driver_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    private Set<Location> serviceAreas = new HashSet<>();

    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL)
    private List<DriverVehicleSkill> vehicleSkills = new ArrayList<>();


    @Column(name = "average_rating")
    private Double averageRating = 0.0;
}
