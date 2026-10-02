package innovation.ride.umusare.service;

import innovation.ride.umusare.dtos.DriverResponseDTO;

import java.util.List;

public interface AdminService {
    List<DriverResponseDTO> getPendingDrivers();
    DriverResponseDTO verifyDriver(String driverId);
    void rejectDriver(String driverId);
}