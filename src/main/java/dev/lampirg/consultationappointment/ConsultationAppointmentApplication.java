package dev.lampirg.consultationappointment;

import dev.lampirg.consultationappointment.data.student.Student;
import dev.lampirg.consultationappointment.data.student.StudentRepository;
import dev.lampirg.consultationappointment.data.teacher.DatePeriod;
import dev.lampirg.consultationappointment.data.teacher.Teacher;
import dev.lampirg.consultationappointment.data.teacher.TeacherRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class ConsultationAppointmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsultationAppointmentApplication.class, args);
	}

	@Bean
	public CommandLineRunner dataLoader(StudentRepository studentRepository, TeacherRepository teacherRepository) {
		return args -> {
			studentRepository.save(new Student("Василий", "Сакович", "Владиславович", "lampirg1@gmail.com", "ИВБ-911"));
			studentRepository.save(new Student("Георгий", "Васильев", "Анатольевич", "georgii_vas@mail.ru", "КИБ-113"));
			Teacher teacher = new Teacher();
			teacher.setFirstName("Сергей");
			teacher.setLastName("Иванов");
			teacher.setPatronymic("Александрович");
			teacher.getDatePeriod().add(new DatePeriod(LocalDateTime.of(2020, 5, 23, 12, 15),
					LocalDateTime.of(2020, 5, 23, 12, 15)));
		};
	}

}
