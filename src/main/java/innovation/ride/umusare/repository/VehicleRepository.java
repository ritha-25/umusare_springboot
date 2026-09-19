package innovation.ride.umusare.repository;

import innovation.ride.umusare.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, String> {

    List<Vehicle> findByPassenger_UserIdAndActiveTrue(String passengerId);

    Optional<Vehicle> findByPlateNumberAndActiveTrue(String plateNumber);
}
