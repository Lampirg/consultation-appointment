package dev.lampirg.consultationappointment;

import dev.lampirg.consultationappointment.data.appointment.AppointmentRepository;
import dev.lampirg.consultationappointment.data.schedule.PatternSchedule;
import dev.lampirg.consultationappointment.data.student.Student;
import dev.lampirg.consultationappointment.data.student.StudentRepository;
import dev.lampirg.consultationappointment.data.teacher.DatePeriod;
import dev.lampirg.consultationappointment.data.teacher.Teacher;
import dev.lampirg.consultationappointment.data.teacher.TeacherRepository;
import dev.lampirg.consultationappointment.service.teacher.ConsultationScheduler;
import dev.lampirg.consultationappointment.web.fetch.ConsultationPattern;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Configuration
@Profile("dev")
public class PreLoadedData  {

    private StudentRepository studentRepository;
    private TeacherRepository teacherRepository;
    private AppointmentRepository appointmentRepository;
    private ConsultationScheduler consultationScheduler;

    public PreLoadedData(StudentRepository studentRepository, TeacherRepository teacherRepository, AppointmentRepository appointmentRepository, ConsultationScheduler consultationScheduler) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.appointmentRepository = appointmentRepository;
        this.consultationScheduler = consultationScheduler;
    }

    @Bean
    @Order(15)
    public CommandLineRunner dataLoader() throws Exception {
        return args -> {
            studentRepository.save(new Student("Василий", "Сакович", "Владиславович", "vasilii@gmail.com", "{noop}qwerty", "ИВБ-911"));
            Student student = studentRepository.save(new Student("Георгий", "Васильев", "Анатольевич", "georgii_vas@mail.ru", "{noop}qwerty", "КИБ-113"));
            Teacher teacher = new Teacher("Сергей", "Иванов", "Александрович", "ivanov@pgups.ru", "{noop}qwerty");
            teacher.getDatePeriods().add(new DatePeriod(
                    "7-421",
                    LocalDateTime.of(2023, 5, 23, 12, 15),
                    LocalDateTime.of(2023, 5, 23, 14, 15))
            );
            teacherRepository.save(teacher);
            teacher = new Teacher("Александр", "Щёлоков", "Анатольевич", "vasilii@gmail.com", "{noop}qwerty");
            teacher.getDatePeriods().add(new DatePeriod(
                    "1-204",
                    LocalDateTime.of(2023, 5, 11, 11, 15),
                    LocalDateTime.of(2023, 5, 11, 12, 15))
            );
            teacher.getDatePeriods().add(new DatePeriod(
                    "4-301",
                    LocalDateTime.of(2023, 5, 15, 11, 15),
                    LocalDateTime.of(2023, 5, 15, 12, 15))
            );
            teacherRepository.save(teacher);
            PatternSchedule schedule = new PatternSchedule();
            schedule.setFromDate(LocalDate.now());
            schedule.setDate(LocalDate.now());
            schedule.setUntil(LocalDate.now().plusDays(14));
            schedule.setTeacher(teacher);
            schedule.setStartTime(LocalTime.now().minusHours(1));
            schedule.setEndTime(LocalTime.now().plusMinutes(1));
            schedule.setClassroom("1-105");
            consultationScheduler.savePattern(ConsultationPattern.fromPatternSchedule(schedule));
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
