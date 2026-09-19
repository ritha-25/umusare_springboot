package innovation.ride.umusare.service;

import innovation.ride.umusare.dtos.VehicleRequestDTO;
import innovation.ride.umusare.dtos.VehicleResponseDTO;

import java.util.List;

public interface VehicleService {

    VehicleResponseDTO registerVehicle(String passengerId, VehicleRequestDTO request);

    List<VehicleResponseDTO> getActiveVehicles(String passengerId);

    VehicleResponseDTO updateVehicle(String passengerId, String vehicleId, VehicleRequestDTO request);

    void deactivateVehicle(String passengerId, String vehicleId);
}
