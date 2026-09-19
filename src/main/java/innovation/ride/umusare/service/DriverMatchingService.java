package innovation.ride.umusare.service;

import innovation.ride.umusare.entity.Driver;
import innovation.ride.umusare.entity.Location;
import innovation.ride.umusare.entity.Vehicle;
import innovation.ride.umusare.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DriverMatchingService {

    private final DriverRepository driverRepository;

    public Optional<Driver> findBestMatch(Location pickupLocation, Vehicle vehicle, Set<String> excludedDriverIds) {
        List<Driver> eligible = driverRepository.findEligibleDrivers(
                pickupLocation.getLocationId(),
                vehicle.getVehicleType(),
                vehicle.getTransmission()
        );

        return eligible.stream()
                .filter(driver -> excludedDriverIds == null || !excludedDriverIds.contains(driver.getUserId()))
                .max(Comparator.comparing(
                        Driver::getAverageRating,
                        Comparator.nullsFirst(Comparator.naturalOrder())
                ));
    }
}
