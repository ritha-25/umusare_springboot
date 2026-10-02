package innovation.ride.umusare.service;

import innovation.ride.umusare.dtos.AuthResponseDTO;
import innovation.ride.umusare.dtos.LoginRequestDTO;
import innovation.ride.umusare.dtos.RegisterDriverRequestDTO;
import innovation.ride.umusare.dtos.RegisterPassengerRequestDTO;

public interface AuthService {
    AuthResponseDTO registerPassenger(RegisterPassengerRequestDTO request);
    AuthResponseDTO registerDriver(RegisterDriverRequestDTO request);
    AuthResponseDTO login(LoginRequestDTO request);
}