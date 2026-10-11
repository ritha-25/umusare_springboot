package innovation.ride.umusare.controller;

import innovation.ride.umusare.dtos.DriverResponseDTO;
import innovation.ride.umusare.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/drivers")
    public List<DriverResponseDTO> getAllDrivers() {
        return adminService.getAllDrivers();
    }

    @GetMapping("/drivers/pending")
    public List<DriverResponseDTO> getPendingDrivers() {
        return adminService.getPendingDrivers();
    }

    @PutMapping("/drivers/{driverId}/verify")
    public DriverResponseDTO verifyDriver(@PathVariable String driverId) {
        return adminService.verifyDriver(driverId);
    }

    @DeleteMapping("/drivers/{driverId}/reject")
    public void rejectDriver(@PathVariable String driverId) {
        adminService.rejectDriver(driverId);
    }
}