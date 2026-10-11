package innovation.ride.umusare.repository;

import innovation.ride.umusare.entity.Ride;
import innovation.ride.umusare.entity.enums.RideStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RideRepository extends JpaRepository<Ride, String> {

    List<Ride> findByPassenger_UserIdOrderByRequestedAtDesc(String passengerId);
    List<Ride> findByDriver_UserIdAndStatus(String driverId, RideStatus status);
    List<Ride> findByDriver_UserIdAndStatusInOrderByRequestedAtDesc(String driverId, List<RideStatus> statuses);
    List<Ride> findByDriver_UserIdOrderByRequestedAtDesc(String driverId);
}
