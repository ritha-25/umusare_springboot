package innovation.ride.umusare.service.Impl;

import innovation.ride.umusare.dtos.CompleteRideRequestDTO;
import innovation.ride.umusare.dtos.DriverProfileRequestDTO;
import innovation.ride.umusare.dtos.DriverProfileResponseDTO;
import innovation.ride.umusare.dtos.RideResponseDTO;
import innovation.ride.umusare.dtos.StartRideRequestDTO;
import innovation.ride.umusare.entity.Driver;
import innovation.ride.umusare.entity.DriverVehicleSkill;
import innovation.ride.umusare.entity.Location;
import innovation.ride.umusare.entity.Ride;
import innovation.ride.umusare.entity.enums.RideStatus;
import innovation.ride.umusare.exception.InvalidRideOperationException;
import innovation.ride.umusare.exception.ResourceNotFoundException;
import innovation.ride.umusare.repository.DriverRepository;
import innovation.ride.umusare.repository.LocationRepository;
import innovation.ride.umusare.repository.RideRepository;
import innovation.ride.umusare.service.DriverRideService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriverRideServiceImpl implements DriverRideService {

    private final RideRepository rideRepository;
    private final DriverRepository driverRepository;
    private final LocationRepository locationRepository;

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

    @Override
    @Transactional
    public DriverProfileResponseDTO updateProfile(String driverId, DriverProfileRequestDTO request) {
        Driver driver = getDriver(driverId);

        Set<Location> areas = new HashSet<>();
        for (String locationId : request.getServiceAreaIds()) {
            Location loc = locationRepository.findById(locationId)
                    .orElseThrow(() -> new ResourceNotFoundException("Location not found: " + locationId));
            areas.add(loc);
        }
        driver.setServiceAreas(areas);

        driver.getVehicleSkills().clear();
        for (DriverProfileRequestDTO.VehicleSkillDTO skillDTO : request.getVehicleSkills()) {
            DriverVehicleSkill skill = new DriverVehicleSkill();
            skill.setDriver(driver);
            skill.setVehicleType(skillDTO.getVehicleType());
            skill.setTransmission(skillDTO.getTransmission());
            driver.getVehicleSkills().add(skill);
        }

        driverRepository.save(driver);
        return toProfileDTO(driver);
    }

    @Override
    public DriverProfileResponseDTO getProfile(String driverId) {
        Driver driver = getDriver(driverId);
        return toProfileDTO(driver);
    }

    private DriverProfileResponseDTO toProfileDTO(Driver driver) {
        Set<String> areaIds = driver.getServiceAreas().stream()
                .map(Location::getLocationId)
                .collect(Collectors.toSet());

        List<DriverProfileResponseDTO.VehicleSkillInfo> skills = driver.getVehicleSkills().stream()
                .map(s -> DriverProfileResponseDTO.VehicleSkillInfo.builder()
                        .vehicleType(s.getVehicleType())
                        .transmission(s.getTransmission())
                        .build())
                .toList();

        return DriverProfileResponseDTO.builder()
                .driverId(driver.getUserId())
                .fullName(driver.getFullName())
                .verified(driver.isVerified())
                .available(driver.isAvailable())
                .averageRating(driver.getAverageRating())
                .serviceAreaIds(areaIds)
                .vehicleSkills(skills)
                .build();
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