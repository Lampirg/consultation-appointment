package dev.lampirg.consultationappointment;

import dev.lampirg.consultationappointment.data.student.Student;
import dev.lampirg.consultationappointment.data.student.StudentRepository;
import dev.lampirg.consultationappointment.data.teacher.DatePeriod;
import dev.lampirg.consultationappointment.data.teacher.Teacher;
import dev.lampirg.consultationappointment.data.teacher.TeacherRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.UUID;

@Configuration
public class PreLoadedData  {

    StudentRepository studentRepository;
    TeacherRepository teacherRepository;

    public PreLoadedData(StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    @Bean
    public CommandLineRunner dataLoader() throws Exception {
        return args -> {
            studentRepository.save(new Student("Василий", "Сакович", "Владиславович", "vasilii@gmail.com", "{noop}qwerty", "ИВБ-911"));
            studentRepository.save(new Student("Георгий", "Васильев", "Анатольевич", "georgii_vas@mail.ru", "{noop}qwerty", "КИБ-113"));
            Teacher teacher = new Teacher("Сергей", "Иванов", "Александрович", "ivanov@pgups.ru", "{noop}qwerty");
            teacher.getDatePeriods().add(new DatePeriod(LocalDateTime.of(2023, 5, 23, 12, 15),
                    LocalDateTime.of(2023, 5, 23, 14, 15)));
            teacherRepository.save(teacher);
            teacher = new Teacher("Александр", "Щёлоков", "Анатольевич", "vasilii@gmail.com", "{noop}qwerty");
            teacher.getDatePeriods().add(new DatePeriod(LocalDateTime.of(2023, 5, 11, 11, 15),
                    LocalDateTime.of(2023, 5, 11, 12, 15)));
            teacher.getDatePeriods().add(new DatePeriod(LocalDateTime.of(2023, 5, 15, 11, 15),
                    LocalDateTime.of(2023, 5, 15, 12, 15)));
            teacherRepository.save(teacher);
            for (int i = 0; i < 15; i++) {
                String firstName = UUID.randomUUID().toString().substring(0, 5);
                String lastName = "Королевич";
                String patronymic = UUID.randomUUID().toString().substring(0, 5);
                String email = UUID.randomUUID().toString().substring(0, 5) + "_multi@pgups.ru";
                teacher = new Teacher(firstName, lastName, patronymic, email, "{noop}qwerty");
                teacherRepository.save(teacher);
            }
        };
    }
}
