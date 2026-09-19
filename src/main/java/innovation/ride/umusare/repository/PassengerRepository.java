package innovation.ride.umusare.repository;

import innovation.ride.umusare.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, String> {
}
