package innovation.ride.umusare.repository;

import innovation.ride.umusare.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, String> {
}