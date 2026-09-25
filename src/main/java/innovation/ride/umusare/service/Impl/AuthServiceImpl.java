package innovation.ride.umusare.service.Impl;

import innovation.ride.umusare.dto.AuthResponseDTO;
import innovation.ride.umusare.dto.LoginRequestDTO;
import innovation.ride.umusare.dto.RegisterDriverRequestDTO;
import innovation.ride.umusare.dto.RegisterPassengerRequestDTO;
import innovation.ride.umusare.entity.Driver;
import innovation.ride.umusare.entity.Passenger;
import innovation.ride.umusare.entity.User;
import innovation.ride.umusare.entity.enums.Role;
import innovation.ride.umusare.exception.InvalidRideOperationException;
import innovation.ride.umusare.repository.DriverRepository;
import innovation.ride.umusare.repository.PassengerRepository;
import innovation.ride.umusare.repository.UserRepository;
import innovation.ride.umusare.security.JwtUtil;
import innovation.ride.umusare.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public AuthResponseDTO registerPassenger(RegisterPassengerRequestDTO request) {
        checkAvailable(request.getPhoneNumber(), request.getNid());

        Passenger passenger = new Passenger();
        passenger.setFullName(request.getFullName());
        passenger.setNid(request.getNid());
        passenger.setPhoneNumber(request.getPhoneNumber());
        passenger.setPassword(passwordEncoder.encode(request.getPassword()));
        passenger.setGender(request.getGender());
        passenger.setRole(Role.PASSENGER);

        Passenger saved = passengerRepository.save(passenger);
        return buildAuthResponse(saved);
    }

    @Override
    public AuthResponseDTO registerDriver(RegisterDriverRequestDTO request) {
        checkAvailable(request.getPhoneNumber(), request.getNid());

        Driver driver = new Driver();
        driver.setFullName(request.getFullName());
        driver.setNid(request.getNid());
        driver.setPhoneNumber(request.getPhoneNumber());
        driver.setPassword(passwordEncoder.encode(request.getPassword()));
        driver.setGender(request.getGender());
        driver.setRole(Role.DRIVER);
        driver.setLicenseNumber(request.getLicenseNumber());
        driver.setLicenseExpiry(request.getLicenseExpiry());
        driver.setVerified(false);
        driver.setAvailable(false);

        Driver saved = driverRepository.save(driver);
        return buildAuthResponse(saved);
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new InvalidRideOperationException("Invalid phone number or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidRideOperationException("Invalid phone number or password");
        }

        return buildAuthResponse(user);
    }

    private void checkAvailable(String phoneNumber, String nid) {
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new InvalidRideOperationException("Phone number is already registered");
        }
        if (userRepository.existsByNid(nid)) {
            throw new InvalidRideOperationException("NID is already registered");
        }
    }

    private AuthResponseDTO buildAuthResponse(User user) {
        String token = jwtUtil.generateToken(user.getUserId(), user.getRole().name());
        return new AuthResponseDTO(token, user.getUserId(), user.getFullName(), user.getRole());
    }
}