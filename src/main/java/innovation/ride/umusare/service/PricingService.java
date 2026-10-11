package innovation.ride.umusare.service;

import innovation.ride.umusare.dtos.PricingRequestDTO;
import innovation.ride.umusare.dtos.PricingResponseDTO;

import java.util.List;

public interface PricingService {
    PricingResponseDTO create(PricingRequestDTO request);
    List<PricingResponseDTO> getAll();
    void delete(Long id);
}
