package innovation.ride.umusare.service;

import innovation.ride.umusare.dtos.LocationRequestDTO;
import innovation.ride.umusare.dtos.LocationResponseDTO;

import java.util.List;

public interface LocationService {
    LocationResponseDTO create(LocationRequestDTO request);
    List<LocationResponseDTO> getAll();
    LocationResponseDTO getById(String locationId);
    LocationResponseDTO update(String locationId, LocationRequestDTO request);
    void delete(String locationId);
}