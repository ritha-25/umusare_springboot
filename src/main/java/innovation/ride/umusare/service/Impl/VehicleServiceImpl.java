package innovation.ride.umusare.service.Impl;

import innovation.ride.umusare.dtos.VehicleRequestDTO;
import innovation.ride.umusare.dtos.VehicleResponseDTO;
import innovation.ride.umusare.entity.Passenger;
import innovation.ride.umusare.entity.Vehicle;
import innovation.ride.umusare.exception.InvalidRideOperationException;
import innovation.ride.umusare.exception.ResourceNotFoundException;
import innovation.ride.umusare.repository.PassengerRepository;
import innovation.ride.umusare.repository.VehicleRepository;
import innovation.ride.umusare.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final PassengerRepository passengerRepository;

    @Override
    @Transactional
    public VehicleResponseDTO registerVehicle(String passengerId, VehicleRequestDTO request) {
        Passenger passenger = passengerRepository.findById(passengerId)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found: " + passengerId));

        vehicleRepository.findByPlateNumberAndActiveTrue(request.getPlateNumber())
                .ifPresent(existing -> {
                    throw new InvalidRideOperationException(
                            "A vehicle with this plate number is already there.");
                });

        Vehicle vehicle = new Vehicle();
        vehicle.setPassenger(passenger);
        vehicle.setPlateNumber(request.getPlateNumber());
        vehicle.setModel(request.getModel());
        vehicle.setColor(request.getColor());
        vehicle.setTransmission(request.getTransmission());
        vehicle.setVehicleType(request.getVehicleType());
        vehicle.setActive(true);

        return toDTO(vehicleRepository.save(vehicle));
    }

    @Override
    public List<VehicleResponseDTO> getActiveVehicles(String passengerId) {
        return vehicleRepository.findByPassenger_UserIdAndActiveTrue(passengerId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public VehicleResponseDTO updateVehicle(String passengerId, String vehicleId, VehicleRequestDTO request) {
        Vehicle vehicle = getOwnedActiveVehicle(passengerId, vehicleId);
        vehicle.setModel(request.getModel());
        vehicle.setColor(request.getColor());
        vehicle.setTransmission(request.getTransmission());
        vehicle.setVehicleType(request.getVehicleType());

        return toDTO(vehicleRepository.save(vehicle));
    }

    @Override
    @Transactional
    public void deactivateVehicle(String passengerId, String vehicleId) {
        Vehicle vehicle = getOwnedActiveVehicle(passengerId, vehicleId);
        vehicle.setActive(false);
        vehicleRepository.save(vehicle);
    }

    private Vehicle getOwnedActiveVehicle(String passengerId, String vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found: " + vehicleId));
        if (!vehicle.getPassenger().getUserId().equals(passengerId)) {
            throw new InvalidRideOperationException("This vehicle does not belong to this passenger.");
        }
        if (!vehicle.isActive()) {
            throw new InvalidRideOperationException("This vehicle has already been removed.");
        }
        return vehicle;
    }

    private VehicleResponseDTO toDTO(Vehicle vehicle) {
        return VehicleResponseDTO.builder()
                .vehicleId(vehicle.getVehicleId())
                .plateNumber(vehicle.getPlateNumber())
                .model(vehicle.getModel())
                .color(vehicle.getColor())
                .transmission(vehicle.getTransmission())
                .vehicleType(vehicle.getVehicleType())
                .active(vehicle.isActive())
                .build();
    }
}
