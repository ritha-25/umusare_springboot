package innovation.ride.umusare.service.Impl;

import innovation.ride.umusare.dtos.DriverResponseDTO;
import innovation.ride.umusare.entity.Driver;
import innovation.ride.umusare.exception.ResourceNotFoundException;
import innovation.ride.umusare.repository.DriverRepository;
import innovation.ride.umusare.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final DriverRepository driverRepository;

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
        return toDTO(driverRepository.save(driver));
    }

    @Override
    @Transactional
    public void rejectDriver(String driverId) {
        Driver driver = getDriver(driverId);
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