package innovation.ride.umusare.repository;

import innovation.ride.umusare.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, String> {
}
