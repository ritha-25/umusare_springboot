package innovation.ride.umusare.controller;

import com.umusare.dto.ConfirmRideRequestDTO;
import com.umusare.dto.RideMatchRequestDTO;
import com.umusare.dto.RideMatchResponseDTO;
import com.umusare.dto.RideResponseDTO;
import com.umusare.service.RideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Two-step ride flow (Option C matching):
 *   1. POST /find-match   -> system proposes one driver + price, nothing saved yet
 *   2. POST /             -> passenger confirms that driver, Ride is created (status REQUESTED)
 * Calling /find-match again with the previous driverId in excludedDriverIds
 * gets the next-best candidate instead of repeating the same one.
 */
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

    /** FR009: allowed only before the ride is accepted/started - enforced in the service layer. */
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
