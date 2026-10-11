package innovation.ride.umusare.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import innovation.ride.umusare.dtos.AuthResponseDTO;
import innovation.ride.umusare.entity.Passenger;
import innovation.ride.umusare.entity.enums.Role;
import innovation.ride.umusare.exception.InvalidRideOperationException;
import innovation.ride.umusare.repository.PassengerRepository;
import innovation.ride.umusare.repository.UserRepository;
import innovation.ride.umusare.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class OAuth2Service {

    private final UserRepository userRepository;
    private final PassengerRepository passengerRepository;
    private final JwtUtil jwtUtil;

    @Value("${app.oauth2.google.client-id}")
    private String googleClientId;

    @Transactional
    public AuthResponseDTO loginWithGoogle(String idToken) {
        GoogleIdToken.Payload payload = verifyGoogleToken(idToken);

        String email = payload.getEmail();
        String fullName = (String) payload.get("name");

        Passenger passenger = passengerRepository.findByEmail(email)
                .orElseGet(() -> createPassengerFromGoogle(email, fullName));

        String token = jwtUtil.generateToken(passenger.getUserId(), passenger.getRole().name());
        return new AuthResponseDTO(token, passenger.getUserId(), passenger.getFullName(), passenger.getRole(), null);
    }

    private Passenger createPassengerFromGoogle(String email, String fullName) {
        Passenger passenger = new Passenger();
        passenger.setEmail(email);
        passenger.setFullName(fullName != null ? fullName : email);
        passenger.setRole(Role.PASSENGER);
        passenger.setOauth2Provider("google");
        return passengerRepository.save(passenger);
    }

    private GoogleIdToken.Payload verifyGoogleToken(String idToken) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken googleIdToken = verifier.verify(idToken);
            if (googleIdToken == null) {
                throw new InvalidRideOperationException("Invalid Google token");
            }
            return googleIdToken.getPayload();
        } catch (InvalidRideOperationException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidRideOperationException("Failed to verify Google token");
        }
    }
}
