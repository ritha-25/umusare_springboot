package innovation.ride.umusare.service.Impl;

import innovation.ride.umusare.dtos.DriverResponseDTO;
import innovation.ride.umusare.dtos.RideEventMessage;
import innovation.ride.umusare.entity.Driver;
import innovation.ride.umusare.exception.ResourceNotFoundException;
import innovation.ride.umusare.repository.DriverRepository;
import innovation.ride.umusare.service.AdminService;
import innovation.ride.umusare.service.RideNotificationPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final DriverRepository driverRepository;
    private final RideNotificationPublisher notificationPublisher;

    @Override
    public List<DriverResponseDTO> getAllDrivers() {
        return driverRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public List<DriverResponseDTO> getPendingDrivers() {
        return driverRepository.findByVerifiedFalse()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public DriverResponseDTO verifyDriver(String driverId) {
        Driver driver = getDriver(driverId);
        driver.setVerified(true);
        Driver saved = driverRepository.save(driver);

        notificationPublisher.publish(new RideEventMessage(
                null,
                "DRIVER_APPROVED",
                saved.getEmail(),
                saved.getFullName(),
                "Your driver account has been approved. You can now log in and start accepting rides."
        ));

        return toDTO(saved);
    }

    @Override
    @Transactional
    public void rejectDriver(String driverId) {
        Driver driver = getDriver(driverId);

        notificationPublisher.publish(new RideEventMessage(
                null,
                "DRIVER_REJECTED",
                driver.getEmail(),
                driver.getFullName(),
                "Your driver application was not approved. Contact support for more information."
        ));

        driverRepository.delete(driver);
    }

    private Driver getDriver(String driverId) {
        return driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found: " + driverId));
    }

    private DriverResponseDTO toDTO(Driver driver) {
        return DriverResponseDTO.builder()
                .driverId(driver.getUserId())
                .fullName(driver.getFullName())
                .licenseNumber(driver.getLicenseNumber())
                .verified(driver.isVerified())
                .build();
    }
}
