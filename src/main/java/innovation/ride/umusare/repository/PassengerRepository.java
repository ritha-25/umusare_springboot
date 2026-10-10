package innovation.ride.umusare.repository;

import innovation.ride.umusare.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger, String> {
    Optional<Passenger> findByEmail(String email);
}
