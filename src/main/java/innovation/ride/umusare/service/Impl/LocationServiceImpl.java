package innovation.ride.umusare.service.Impl;

import innovation.ride.umusare.dtos.LocationRequestDTO;
import innovation.ride.umusare.dtos.LocationResponseDTO;
import innovation.ride.umusare.entity.Location;
import innovation.ride.umusare.exception.ResourceNotFoundException;
import innovation.ride.umusare.repository.LocationRepository;
import innovation.ride.umusare.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public LocationResponseDTO create(LocationRequestDTO request) {
        Location location = new Location();
        location.setLocationName(request.getLocationName());
        location.setLocationCode(request.getLocationCode());
        location.setType(request.getType());

        if (request.getParentLocationId() != null) {
            Location parent = locationRepository.findById(request.getParentLocationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent location not found"));
            location.setParent(parent);
        }

        Location saved = locationRepository.save(location);
        return toDTO(saved);
    }

    @Override
    public List<LocationResponseDTO> getAll() {
        List<Location> locations = locationRepository.findAll();
        return locations.stream().map(this::toDTO).toList();
    }

    @Override
    public LocationResponseDTO getById(String locationId) {
        Location location = getEntity(locationId);
        return toDTO(location);
    }

    @Override
    public LocationResponseDTO update(String locationId, LocationRequestDTO request) {
        Location location = getEntity(locationId);
        location.setLocationName(request.getLocationName());
        location.setLocationCode(request.getLocationCode());
        location.setType(request.getType());

        if (request.getParentLocationId() != null) {
            Location parent = locationRepository.findById(request.getParentLocationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent location not found"));
            location.setParent(parent);
        } else {
            location.setParent(null);
        }

        Location updated = locationRepository.save(location);
        return toDTO(updated);
    }

    @Override
    public void delete(String locationId) {
        Location location = getEntity(locationId);
        locationRepository.delete(location);
    }

    private Location getEntity(String locationId) {
        return locationRepository.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found: " + locationId));
    }

    private LocationResponseDTO toDTO(Location location) {
        LocationResponseDTO dto = new LocationResponseDTO();
        dto.setLocationId(location.getLocationId());
        dto.setLocationName(location.getLocationName());
        dto.setLocationCode(location.getLocationCode());
        dto.setType(location.getType());
        if (location.getParent() != null) {
            dto.setParentLocationId(location.getParent().getLocationId());
        }
        return dto;
    }
}