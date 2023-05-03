package dev.lampirg.consultationappointment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ConsultationAppointmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsultationAppointmentApplication.class, args);
	}
}
