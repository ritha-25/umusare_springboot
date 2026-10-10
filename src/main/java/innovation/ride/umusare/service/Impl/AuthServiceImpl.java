package innovation.ride.umusare.service.Impl;

import innovation.ride.umusare.dtos.*;
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
import innovation.ride.umusare.service.RideNotificationPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PassengerRepository passengerRepository;
    private final DriverRepository driverRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RideNotificationPublisher notificationPublisher;

    @Override
    public AuthResponseDTO registerPassenger(RegisterPassengerRequestDTO request) {
        checkAvailable(request.getPhoneNumber(), request.getNid(), request.getEmail());

        Passenger passenger = new Passenger();
        passenger.setFullName(request.getFullName());
        passenger.setNid(request.getNid());
        passenger.setEmail(request.getEmail());
        passenger.setPhoneNumber(request.getPhoneNumber());
        passenger.setPassword(passwordEncoder.encode(request.getPassword()));
        passenger.setGender(request.getGender());
        passenger.setRole(Role.PASSENGER);

        Passenger saved = passengerRepository.save(passenger);

        notificationPublisher.publish(new RideEventMessage(
                null,
                "PASSENGER_REGISTERED",
                saved.getEmail(),
                saved.getFullName(),
                "Your Umusare account has been created. You can now log in."
        ));

        return buildAuthResponse(saved);
    }

    @Override
    public AuthResponseDTO registerDriver(RegisterDriverRequestDTO request) {
        checkAvailable(request.getPhoneNumber(), request.getNid(), request.getEmail());

        Driver driver = new Driver();
        driver.setFullName(request.getFullName());
        driver.setNid(request.getNid());
        driver.setEmail(request.getEmail());
        driver.setPhoneNumber(request.getPhoneNumber());
        driver.setPassword(passwordEncoder.encode(request.getPassword()));
        driver.setGender(request.getGender());
        driver.setRole(Role.DRIVER);
        driver.setLicenseNumber(request.getLicenseNumber());
        driver.setLicenseExpiry(request.getLicenseExpiry());
        driver.setVerified(false);
        driver.setAvailable(false);

        Driver saved = driverRepository.save(driver);

        notificationPublisher.publish(new RideEventMessage(
                null,
                "DRIVER_REGISTERED",
                saved.getEmail(),
                saved.getFullName(),
                "Your driver registration has been received and is pending admin approval."
        ));

        return buildAuthResponse(saved);
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO request) {
        String identifier = request.getIdentifier();

        User user = userRepository.findByPhoneNumber(identifier)
                .or(() -> userRepository.findByEmail(identifier))
                .orElseThrow(() -> new InvalidRideOperationException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidRideOperationException("Invalid credentials");
        }

        return buildAuthResponse(user);
    }

    private void checkAvailable(String phoneNumber, String nid, String email) {
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new InvalidRideOperationException("Phone number is already registered");
        }
        if (userRepository.existsByNid(nid)) {
            throw new InvalidRideOperationException("NID is already registered");
        }
        if (userRepository.existsByEmail(email)) {
            throw new InvalidRideOperationException("Email is already registered");
        }
    }

    private AuthResponseDTO buildAuthResponse(User user) {
        String token = jwtUtil.generateToken(user.getUserId(), user.getRole().name());
        Boolean verified = null;
        if (user instanceof Driver d) {
            verified = d.isVerified();
        }
        return new AuthResponseDTO(token, user.getUserId(), user.getFullName(), user.getRole(), verified);
    }
}
