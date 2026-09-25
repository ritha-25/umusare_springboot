package innovation.ride.umusare.controller;

import innovation.ride.umusare.dto.AuthResponseDTO;
import innovation.ride.umusare.dto.LoginRequestDTO;
import innovation.ride.umusare.dto.RegisterDriverRequestDTO;
import innovation.ride.umusare.dto.RegisterPassengerRequestDTO;
import innovation.ride.umusare.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register/passenger")
    public AuthResponseDTO registerPassenger(@RequestBody RegisterPassengerRequestDTO request) {
        return authService.registerPassenger(request);
    }

    @PostMapping("/register/driver")
    public AuthResponseDTO registerDriver(@RequestBody RegisterDriverRequestDTO request) {
        return authService.registerDriver(request);
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody LoginRequestDTO request) {
        return authService.login(request);
    }
}