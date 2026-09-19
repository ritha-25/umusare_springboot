package innovation.ride.umusare.service.Impl;

import innovation.ride.umusare.dtos.*;
import innovation.ride.umusare.entity.*;
import innovation.ride.umusare.entity.enums.RideStatus;
import innovation.ride.umusare.entity.enums.VehicleType;
import innovation.ride.umusare.exception.InvalidRideOperationException;
import innovation.ride.umusare.exception.ResourceNotFoundException;
import innovation.ride.umusare.repository.*;
import innovation.ride.umusare.service.DriverMatchingService;
import innovation.ride.umusare.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final PassengerRepository passengerRepository;
    private final VehicleRepository vehicleRepository;
    private final LocationRepository locationRepository;
    private final RideDistancePricingRepository pricingRepository;
    private final DriverRepository driverRepository;
    private final DriverMatchingService matchingService;

    @Override
    public RideMatchResponseDTO findMatch(String passengerId, RideMatchRequestDTO request) {
        Vehicle vehicle = getOwnedActiveVehicle(passengerId, request.getVehicleId());
        Location pickup = getLocation(request.getPickupLocationId());
        Location destination = getLocation(request.getDestinationLocationId());

        Double price = calculatePrice(pickup, destination, vehicle.getVehicleType());

        Set<String> excluded = request.getExcludedDriverIds() == null
                ? Collections.emptySet()
                : request.getExcludedDriverIds();

        Optional<Driver> match = matchingService.findBestMatch(pickup, vehicle, excluded);

        return RideMatchResponseDTO.builder()
                .estimatedPrice(price)
                .matchedDriver(match.map(this::toDriverMatchDTO).orElse(null))
                .build();
    }

    @Override
    @Transactional
    public RideResponseDTO confirmRide(String passengerId, ConfirmRideRequestDTO request) {
        Passenger passenger = passengerRepository.findById(passengerId)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found: " + passengerId));
        Vehicle vehicle = getOwnedActiveVehicle(passengerId, request.getVehicleId());
        Location pickup = getLocation(request.getPickupLocationId());
        Location destination = getLocation(request.getDestinationLocationId());

        Driver driver = driverRepository.findById(request.getDriverId())
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found: " + request.getDriverId()));

        if (!driver.isVerified() || !driver.isAvailable()) {
            throw new InvalidRideOperationException("Selected driver is no longer available.");
        }

        Double price = calculatePrice(pickup, destination, vehicle.getVehicleType());

        Ride ride = new Ride();
        ride.setPassenger(passenger);
        ride.setDriver(driver);
        ride.setVehicle(vehicle);
        ride.setPickupLocation(pickup);
        ride.setDestinationLocation(destination);
        ride.setPickupLandmark(request.getPickupLandmark());
        ride.setDestinationLandmark(request.getDestinationLandmark());
        ride.setStatus(RideStatus.REQUESTED);
        ride.setRequestedAt(LocalDateTime.now());
        ride.setPrice(price);

        return toRideResponseDTO(rideRepository.save(ride));
    }

    @Override
    @Transactional
    public void cancelRide(String passengerId, String rideId) {
        Ride ride = getOwnedRide(passengerId, rideId);

        if (ride.getStatus() != RideStatus.REQUESTED && ride.getStatus() != RideStatus.ACCEPTED) {
            throw new InvalidRideOperationException("Ride can only be cancelled before it has started.");
        }

        ride.setStatus(RideStatus.CANCELLED);
        rideRepository.save(ride);
    }

    @Override
    public List<RideResponseDTO> getRideHistory(String passengerId) {
        return rideRepository.findByPassenger_UserIdOrderByRequestedAtDesc(passengerId)
                .stream()
                .map(this::toRideResponseDTO)
                .toList();
    }

    private Double calculatePrice(Location pickup, Location destination, VehicleType vehicleType) {
        RideDistancePricing pricing = pricingRepository
                .findByFromLocationAndToLocationAndVehicleTypeAndActiveTrue(pickup, destination, vehicleType)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No active pricing rule found for this route and vehicle type."));
        return pricing.getPrice();
    }

    private Vehicle getOwnedActiveVehicle(String passengerId, String vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found: " + vehicleId));
        if (!vehicle.getPassenger().getUserId().equals(passengerId) || !vehicle.isActive()) {
            throw new InvalidRideOperationException("Vehicle does not belong to this passenger or is inactive.");
        }
        return vehicle;
    }

    private Location getLocation(String locationId) {
        return locationRepository.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found: " + locationId));
    }

    private Ride getOwnedRide(String passengerId, String rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found: " + rideId));
        if (!ride.getPassenger().getUserId().equals(passengerId)) {
            throw new InvalidRideOperationException("This ride does not belong to this passenger.");
        }
        return ride;
    }

    private DriverMatchDTO toDriverMatchDTO(Driver driver) {
        return DriverMatchDTO.builder()
                .driverId(driver.getUserId())
                .fullName(driver.getFullName())
                .verified(driver.isVerified())
                .averageRating(driver.getAverageRating())
                .build();
    }

    private RideResponseDTO toRideResponseDTO(Ride ride) {
        return RideResponseDTO.builder()
                .rideId(ride.getRideId())
                .status(ride.getStatus())
                .price(ride.getPrice())
                .pickupLocation(ride.getPickupLocation().getLocationName())
                .destinationLocation(ride.getDestinationLocation().getLocationName())
                .vehiclePlateNumber(ride.getVehicle().getPlateNumber())
                .driverName(ride.getDriver() != null ? ride.getDriver().getFullName() : null)
                .requestedAt(ride.getRequestedAt())
                .build();
    }
}
