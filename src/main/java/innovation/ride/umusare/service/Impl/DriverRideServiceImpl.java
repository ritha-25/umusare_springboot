package innovation.ride.umusare.service.Impl;

import innovation.ride.umusare.dtos.CompleteRideRequestDTO;
import innovation.ride.umusare.dtos.RideResponseDTO;
import innovation.ride.umusare.dtos.StartRideRequestDTO;
import innovation.ride.umusare.entity.Driver;
import innovation.ride.umusare.entity.Ride;
import innovation.ride.umusare.entity.enums.RideStatus;
import innovation.ride.umusare.exception.InvalidRideOperationException;
import innovation.ride.umusare.exception.ResourceNotFoundException;
import innovation.ride.umusare.repository.DriverRepository;
import innovation.ride.umusare.repository.RideRepository;
import innovation.ride.umusare.service.DriverRideService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverRideServiceImpl implements DriverRideService {

    private final RideRepository rideRepository;
    private final DriverRepository driverRepository;

    @Override
    @Transactional
    public void setAvailability(String driverId, boolean available) {
        Driver driver = getDriver(driverId);
        driver.setAvailable(available);
        driverRepository.save(driver);
    }

    @Override
    public List<RideResponseDTO> getIncomingRequests(String driverId) {
        return rideRepository.findByDriver_UserIdAndStatus(driverId, RideStatus.REQUESTED)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public RideResponseDTO acceptRide(String driverId, String rideId) {
        Ride ride = getOwnedRide(driverId, rideId);

        if (ride.getStatus() != RideStatus.REQUESTED) {
            throw new InvalidRideOperationException("Ride is no longer awaiting acceptance.");
        }

        ride.setStatus(RideStatus.ACCEPTED);
        ride.setAcceptedAt(LocalDateTime.now());
        return toDTO(rideRepository.save(ride));
    }

    @Override
    @Transactional
    public void rejectRide(String driverId, String rideId) {
        Ride ride = getOwnedRide(driverId, rideId);

        if (ride.getStatus() != RideStatus.REQUESTED) {
            throw new InvalidRideOperationException("Ride is no longer awaiting acceptance.");
        }

        ride.getDeclinedDriverIds().add(driverId);
        ride.setDriver(null);
        rideRepository.save(ride);
    }

    @Override
    @Transactional
    public RideResponseDTO startRide(String driverId, String rideId, StartRideRequestDTO request) {
        Ride ride = getOwnedRide(driverId, rideId);

        if (ride.getStatus() != RideStatus.ACCEPTED) {
            throw new InvalidRideOperationException("Ride must be accepted before it can start.");
        }

        ride.setStartOdometer(request.getStartOdometer());
        ride.setCarConditionNotes(request.getCarConditionNotes());
        ride.setStatus(RideStatus.IN_PROGRESS);
        ride.setStartedAt(LocalDateTime.now());
        return toDTO(rideRepository.save(ride));
    }

    @Override
    @Transactional
    public RideResponseDTO completeRide(String driverId, String rideId, CompleteRideRequestDTO request) {
        Ride ride = getOwnedRide(driverId, rideId);

        if (ride.getStatus() != RideStatus.IN_PROGRESS) {
            throw new InvalidRideOperationException("Ride must be in progress before it can be completed.");
        }

        ride.setEndOdometer(request.getEndOdometer());
        ride.setStatus(RideStatus.COMPLETED);
        ride.setCompletedAt(LocalDateTime.now());
        return toDTO(rideRepository.save(ride));
    }

    @Override
    public List<RideResponseDTO> getRideHistory(String driverId) {
        return rideRepository.findByDriver_UserIdOrderByRequestedAtDesc(driverId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private Driver getDriver(String driverId) {
        return driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found: " + driverId));
    }

    private Ride getOwnedRide(String driverId, String rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found: " + rideId));
        if (ride.getDriver() == null || !ride.getDriver().getUserId().equals(driverId)) {
            throw new InvalidRideOperationException("This ride is not assigned to you.");
        }
        return ride;
    }

    private RideResponseDTO toDTO(Ride ride) {
        return RideResponseDTO.builder()
                .rideId(ride.getRideId())
                .status(ride.getStatus())
                .price(ride.getPrice())
                .pickupLocation(ride.getPickupLocation().getLocationName())
                .destinationLocation(ride.getDestinationLocation().getLocationName())
                .vehiclePlateNumber(ride.getVehicle().getPlateNumber())
                .driverName(ride.getDriver() != null ? ride.getDriver().getFullName() : null)
                .passengerName(ride.getPassenger().getFullName())
                .requestedAt(ride.getRequestedAt())
                .build();
    }
}