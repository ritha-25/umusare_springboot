package innovation.ride.umusare.service;

import innovation.ride.umusare.dtos.ConfirmRideRequestDTO;
import innovation.ride.umusare.dtos.RideMatchRequestDTO;
import innovation.ride.umusare.dtos.RideMatchResponseDTO;
import innovation.ride.umusare.dtos.RideResponseDTO;

import java.util.List;

public interface RideService {


    RideMatchResponseDTO findMatch(String passengerId, RideMatchRequestDTO request);


    RideResponseDTO confirmRide(String passengerId, ConfirmRideRequestDTO request);


    void cancelRide(String passengerId, String rideId);

    List<RideResponseDTO> getRideHistory(String passengerId);
}
