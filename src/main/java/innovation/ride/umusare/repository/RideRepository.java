package innovation.ride.umusare.repository;

import innovation.ride.umusare.entity.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RideRepository extends JpaRepository<Ride, String> {

    List<Ride> findByPassenger_UserIdOrderByRequestedAtDesc(String passengerId);
}
