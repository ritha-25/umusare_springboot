package innovation.ride.umusare.repository;

import innovation.ride.umusare.entity.Location;
import innovation.ride.umusare.entity.RideDistancePricing;
import innovation.ride.umusare.entity.enums.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RideDistancePricingRepository extends JpaRepository<RideDistancePricing, Long> {

    Optional<RideDistancePricing> findByFromLocationAndToLocationAndVehicleTypeAndActiveTrue(
            Location fromLocation, Location toLocation, VehicleType vehicleType);
}
