package innovation.ride.umusare.controller;

import innovation.ride.umusare.dtos.AvailabilityRequestDTO;
import innovation.ride.umusare.dtos.CompleteRideRequestDTO;
import innovation.ride.umusare.dtos.DriverProfileRequestDTO;
import innovation.ride.umusare.dtos.DriverProfileResponseDTO;
import innovation.ride.umusare.dtos.RideResponseDTO;
import innovation.ride.umusare.dtos.StartRideRequestDTO;
import innovation.ride.umusare.security.UserPrincipal;
import innovation.ride.umusare.service.DriverRideService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/driver")
@RequiredArgsConstructor
public class DriverController {

    private final DriverRideService driverRideService;

    @GetMapping("/profile")
    public DriverProfileResponseDTO getProfile(@AuthenticationPrincipal UserPrincipal currentUser) {
        return driverRideService.getProfile(currentUser.getUserId());
    }

    @PutMapping("/profile")
    public DriverProfileResponseDTO updateProfile(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestBody DriverProfileRequestDTO request) {
        return driverRideService.updateProfile(currentUser.getUserId(), request);
    }

    @PutMapping("/availability")
    public void setAvailability(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestBody AvailabilityRequestDTO request) {
        driverRideService.setAvailability(currentUser.getUserId(), request.isAvailable());
    }

    @GetMapping("/rides/incoming")
    public List<RideResponseDTO> getIncoming(@AuthenticationPrincipal UserPrincipal currentUser) {
        return driverRideService.getIncomingRequests(currentUser.getUserId());
    }

    @PostMapping("/rides/{rideId}/accept")
    public RideResponseDTO accept(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable String rideId) {
        return driverRideService.acceptRide(currentUser.getUserId(), rideId);
    }

    @PostMapping("/rides/{rideId}/reject")
    public void reject(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable String rideId) {
        driverRideService.rejectRide(currentUser.getUserId(), rideId);
    }

    @PostMapping("/rides/{rideId}/start")
    public RideResponseDTO start(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable String rideId,
            @RequestBody StartRideRequestDTO request) {
        return driverRideService.startRide(currentUser.getUserId(), rideId, request);
    }

    @PostMapping("/rides/{rideId}/complete")
    public RideResponseDTO complete(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable String rideId,
            @RequestBody CompleteRideRequestDTO request) {
        return driverRideService.completeRide(currentUser.getUserId(), rideId, request);
    }

    @GetMapping("/rides/history")
    public List<RideResponseDTO> history(@AuthenticationPrincipal UserPrincipal currentUser) {
        return driverRideService.getRideHistory(currentUser.getUserId());
    }
}