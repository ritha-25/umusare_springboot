package innovation.ride.umusare.controller;

import innovation.ride.umusare.dtos.ConfirmRideRequestDTO;
import innovation.ride.umusare.dtos.RideMatchRequestDTO;
import innovation.ride.umusare.dtos.RideMatchResponseDTO;
import innovation.ride.umusare.dtos.RideResponseDTO;
import innovation.ride.umusare.service.RideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/passengers/{passengerId}/rides")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;

    @PostMapping("/find-match")
    public ResponseEntity<RideMatchResponseDTO> findMatch(
            @PathVariable String passengerId,
            @Valid @RequestBody RideMatchRequestDTO request) {
        return ResponseEntity.ok(rideService.findMatch(passengerId, request));
    }

    @PostMapping
    public ResponseEntity<RideResponseDTO> confirmRide(
            @PathVariable String passengerId,
            @Valid @RequestBody ConfirmRideRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(rideService.confirmRide(passengerId, request));
    }

    @DeleteMapping("/{rideId}")
    public ResponseEntity<Void> cancelRide(
            @PathVariable String passengerId,
            @PathVariable String rideId) {
        rideService.cancelRide(passengerId, rideId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<RideResponseDTO>> getRideHistory(@PathVariable String passengerId) {
        return ResponseEntity.ok(rideService.getRideHistory(passengerId));
    }
}
