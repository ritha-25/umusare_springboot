package innovation.ride.umusare.controller;

import innovation.ride.umusare.dtos.LocationRequestDTO;
import innovation.ride.umusare.dtos.LocationResponseDTO;
import innovation.ride.umusare.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @PostMapping
    public LocationResponseDTO create(@RequestBody LocationRequestDTO request) {
        return locationService.create(request);
    }

    @GetMapping
    public List<LocationResponseDTO> getAll() {
        return locationService.getAll();
    }

    @GetMapping("/{locationId}")
    public LocationResponseDTO getById(@PathVariable String locationId) {
        return locationService.getById(locationId);
    }

    @PutMapping("/{locationId}")
    public LocationResponseDTO update(@PathVariable String locationId, @RequestBody LocationRequestDTO request) {
        return locationService.update(locationId, request);
    }

    @DeleteMapping("/{locationId}")
    public void delete(@PathVariable String locationId) {
        locationService.delete(locationId);
    }
}