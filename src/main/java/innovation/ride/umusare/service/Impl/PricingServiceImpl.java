package innovation.ride.umusare.service.Impl;

import innovation.ride.umusare.dtos.PricingRequestDTO;
import innovation.ride.umusare.dtos.PricingResponseDTO;
import innovation.ride.umusare.entity.Location;
import innovation.ride.umusare.entity.RideDistancePricing;
import innovation.ride.umusare.exception.InvalidRideOperationException;
import innovation.ride.umusare.exception.ResourceNotFoundException;
import innovation.ride.umusare.repository.LocationRepository;
import innovation.ride.umusare.repository.RideDistancePricingRepository;
import innovation.ride.umusare.service.PricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PricingServiceImpl implements PricingService {

    private final RideDistancePricingRepository pricingRepository;
    private final LocationRepository locationRepository;

    @Override
    @Transactional
    public PricingResponseDTO create(PricingRequestDTO request) {
        Location from = getLocation(request.getFromLocationId());
        Location to = getLocation(request.getToLocationId());

        if (pricingRepository.findByFromLocationAndToLocationAndVehicleTypeAndActiveTrue(
                from, to, request.getVehicleType()).isPresent()) {
            throw new InvalidRideOperationException("A pricing rule already exists for this route and vehicle type.");
        }

        RideDistancePricing pricing = new RideDistancePricing();
        pricing.setFromLocation(from);
        pricing.setToLocation(to);
        pricing.setVehicleType(request.getVehicleType());
        pricing.setPrice(request.getPrice());
        pricing.setActive(true);

        return toDTO(pricingRepository.save(pricing));
    }

    @Override
    @Transactional
    public List<PricingResponseDTO> getAll() {
        return pricingRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        RideDistancePricing pricing = pricingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pricing rule not found: " + id));
        pricingRepository.delete(pricing);
    }

    private Location getLocation(String locationId) {
        return locationRepository.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found: " + locationId));
    }

    private PricingResponseDTO toDTO(RideDistancePricing pricing) {
        return PricingResponseDTO.builder()
                .id(pricing.getId())
                .fromLocationId(pricing.getFromLocation().getLocationId())
                .fromLocationName(pricing.getFromLocation().getLocationName())
                .toLocationId(pricing.getToLocation().getLocationId())
                .toLocationName(pricing.getToLocation().getLocationName())
                .vehicleType(pricing.getVehicleType())
                .price(pricing.getPrice())
                .active(pricing.isActive())
                .build();
    }
}
