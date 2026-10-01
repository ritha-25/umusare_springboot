package innovation.ride.umusare.controller;

import innovation.ride.umusare.dtos.ConfirmRideRequestDTO;
import innovation.ride.umusare.dtos.RideMatchRequestDTO;
import innovation.ride.umusare.dtos.RideMatchResponseDTO;
import innovation.ride.umusare.dtos.RideResponseDTO;
import innovation.ride.umusare.security.UserPrincipal;
import innovation.ride.umusare.service.RideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/rides")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;

    @PostMapping("/find-match")
    public ResponseEntity<RideMatchResponseDTO> findMatch(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestBody RideMatchRequestDTO request) {
        return ResponseEntity.ok(rideService.findMatch(currentUser.getUserId(), request));
    }

    @PostMapping
    public ResponseEntity<RideResponseDTO> confirmRide(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestBody ConfirmRideRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(rideService.confirmRide(currentUser.getUserId(), request));
    }

    @DeleteMapping("/{rideId}")
    public ResponseEntity<Void> cancelRide(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable String rideId) {
        rideService.cancelRide(currentUser.getUserId(), rideId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<RideResponseDTO>> getRideHistory(@AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(rideService.getRideHistory(currentUser.getUserId()));
    }
}
