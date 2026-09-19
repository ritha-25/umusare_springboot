package innovation.ride.umusare.service;

import innovation.ride.umusare.dto.ConfirmRideRequestDTO;
import innovation.ride.umusare.dto.RideMatchRequestDTO;
import innovation.ride.umusare.dto.RideMatchResponseDTO;
import innovation.ride.umusare.dto.RideResponseDTO;

import java.util.List;

public interface RideService {


    RideMatchResponseDTO findMatch(String passengerId, RideMatchRequestDTO request);


    RideResponseDTO confirmRide(String passengerId, ConfirmRideRequestDTO request);


    void cancelRide(String passengerId, String rideId);

    List<RideResponseDTO> getRideHistory(String passengerId);
}
