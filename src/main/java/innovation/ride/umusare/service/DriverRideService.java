package innovation.ride.umusare.service;

import innovation.ride.umusare.dtos.CompleteRideRequestDTO;
import innovation.ride.umusare.dtos.DriverProfileRequestDTO;
import innovation.ride.umusare.dtos.DriverProfileResponseDTO;
import innovation.ride.umusare.dtos.RideResponseDTO;
import innovation.ride.umusare.dtos.StartRideRequestDTO;

import java.util.List;

public interface DriverRideService {
    void setAvailability(String driverId, boolean available);
    List<RideResponseDTO> getIncomingRequests(String driverId);
    RideResponseDTO acceptRide(String driverId, String rideId);
    void rejectRide(String driverId, String rideId);
    RideResponseDTO startRide(String driverId, String rideId, StartRideRequestDTO request);
    RideResponseDTO completeRide(String driverId, String rideId, CompleteRideRequestDTO request);
    List<RideResponseDTO> getRideHistory(String driverId);
    DriverProfileResponseDTO updateProfile(String driverId, DriverProfileRequestDTO request);
    DriverProfileResponseDTO getProfile(String driverId);
}