package innovation.ride.umusare.config;

import innovation.ride.umusare.entity.Admin;
import innovation.ride.umusare.entity.enums.Role;
import innovation.ride.umusare.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminBootstrapConfig {

    @Value("${app.admin.phone}")
    private String adminPhone;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @org.springframework.context.annotation.Bean
    public CommandLineRunner seedAdmin() {
        return args -> {
            if (adminRepository.count() == 0) {
                Admin admin = new Admin();
                admin.setFullName("System Admin");
                admin.setNid("ADMIN0000000000");
                admin.setEmail(adminPhone + "@umusare.rw");
                admin.setPhoneNumber(adminPhone);
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setRole(Role.ADMIN);
                adminRepository.save(admin);
            }
        };
    }
}