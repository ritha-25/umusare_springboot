package innovation.ride.umusare.controller;

import innovation.ride.umusare.dto.VehicleRequestDTO;
import innovation.ride.umusare.dto.VehicleResponseDTO;
import innovation.ride.umusare.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/passengers/{passengerId}/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<VehicleResponseDTO> addVehicle(
            @PathVariable String passengerId,
            @Valid @RequestBody VehicleRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vehicleService.registerVehicle(passengerId, request));
    }

    @GetMapping
    public ResponseEntity<List<VehicleResponseDTO>> listVehicles(@PathVariable String passengerId) {
        return ResponseEntity.ok(vehicleService.getActiveVehicles(passengerId));
    }

    @PutMapping("/{vehicleId}")
    public ResponseEntity<VehicleResponseDTO> updateVehicle(
            @PathVariable String passengerId,
            @PathVariable String vehicleId,
            @Valid @RequestBody VehicleRequestDTO request) {
        return ResponseEntity.ok(vehicleService.updateVehicle(passengerId, vehicleId, request));
    }


    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<Void> removeVehicle(
            @PathVariable String passengerId,
            @PathVariable String vehicleId) {
        vehicleService.deactivateVehicle(passengerId, vehicleId);
        return ResponseEntity.noContent().build();
    }
}
