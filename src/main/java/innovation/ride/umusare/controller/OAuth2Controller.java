package innovation.ride.umusare.controller;

import innovation.ride.umusare.dtos.AuthResponseDTO;
import innovation.ride.umusare.dtos.OAuth2LoginRequestDTO;
import innovation.ride.umusare.service.OAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;

    @PostMapping("/oauth2/google")
    public AuthResponseDTO googleLogin(@RequestBody OAuth2LoginRequestDTO request) {
        return oAuth2Service.loginWithGoogle(request.getIdToken());
    }
}
