package innovation.ride.umusare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class UmusareApplication {

	public static void main(String[] args) {
		SpringApplication.run(UmusareApplication.class, args);
	}

}
