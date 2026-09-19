package innovation.ride.umusare.repository;

import innovation.ride.umusare.entity.Driver;
import innovation.ride.umusare.entity.enums.TransmissionType;
import innovation.ride.umusare.entity.enums.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, String> {

    @Query("""
            SELECT DISTINCT d FROM Driver d
            JOIN d.serviceAreas sa
            JOIN d.vehicleSkills skill
            WHERE d.verified = true
              AND d.available = true
              AND sa.locationId = :pickupLocationId
              AND skill.vehicleType = :vehicleType
              AND skill.transmission = :transmission
            """)
    List<Driver> findEligibleDrivers(@Param("pickupLocationId") String pickupLocationId,
                                      @Param("vehicleType") VehicleType vehicleType,
                                      @Param("transmission") TransmissionType transmission);
}
