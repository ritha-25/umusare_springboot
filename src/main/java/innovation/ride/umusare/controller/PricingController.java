package innovation.ride.umusare.controller;

import innovation.ride.umusare.dtos.PricingRequestDTO;
import innovation.ride.umusare.dtos.PricingResponseDTO;
import innovation.ride.umusare.service.PricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/pricing")
@RequiredArgsConstructor
public class PricingController {

    private final PricingService pricingService;

    @PostMapping
    public PricingResponseDTO create(@RequestBody PricingRequestDTO request) {
        return pricingService.create(request);
    }

    @GetMapping
    public List<PricingResponseDTO> getAll() {
        return pricingService.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        pricingService.delete(id);
    }
}
