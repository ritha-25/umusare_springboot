package innovation.ride.umusare.controller;

import innovation.ride.umusare.dtos.VehicleRequestDTO;
import innovation.ride.umusare.dtos.VehicleResponseDTO;
import innovation.ride.umusare.security.UserPrincipal;
import innovation.ride.umusare.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<VehicleResponseDTO> addVehicle(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody VehicleRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vehicleService.registerVehicle(currentUser.getUserId(), request));
    }

    @GetMapping
    public ResponseEntity<List<VehicleResponseDTO>> listVehicles(@AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(vehicleService.getActiveVehicles(currentUser.getUserId()));
    }

    @PutMapping("/{vehicleId}")
    public ResponseEntity<VehicleResponseDTO> updateVehicle(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable String vehicleId,
            @Valid @RequestBody VehicleRequestDTO request) {
        return ResponseEntity.ok(vehicleService.updateVehicle(currentUser.getUserId(), vehicleId, request));
    }


    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<Void> removeVehicle(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable String vehicleId) {
        vehicleService.deactivateVehicle(currentUser.getUserId(), vehicleId);
        return ResponseEntity.noContent().build();
    }
}
