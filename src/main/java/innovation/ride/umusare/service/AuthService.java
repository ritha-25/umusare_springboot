package innovation.ride.umusare.service;

import innovation.ride.umusare.dto.AuthResponseDTO;
import innovation.ride.umusare.dto.LoginRequestDTO;
import innovation.ride.umusare.dto.RegisterDriverRequestDTO;
import innovation.ride.umusare.dto.RegisterPassengerRequestDTO;

public interface AuthService {
    AuthResponseDTO registerPassenger(RegisterPassengerRequestDTO request);
    AuthResponseDTO registerDriver(RegisterDriverRequestDTO request);
    AuthResponseDTO login(LoginRequestDTO request);
}