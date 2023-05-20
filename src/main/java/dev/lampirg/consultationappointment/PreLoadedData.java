package dev.lampirg.consultationappointment;

import dev.lampirg.consultationappointment.data.student.Student;
import dev.lampirg.consultationappointment.data.student.StudentRepository;
import dev.lampirg.consultationappointment.data.teacher.DatePeriod;
import dev.lampirg.consultationappointment.data.teacher.Teacher;
import dev.lampirg.consultationappointment.data.teacher.TeacherRepository;
import dev.lampirg.consultationappointment.service.teacher.ConsultationMaker;
import dev.lampirg.consultationappointment.service.teacher.ConsultationScheduler;
import dev.lampirg.consultationappointment.web.fetch.ConsultationInfo;
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
    private ConsultationScheduler consultationScheduler;
    private ConsultationMaker consultationMaker;

    public PreLoadedData(StudentRepository studentRepository, TeacherRepository teacherRepository, ConsultationScheduler consultationScheduler, ConsultationMaker consultationMaker) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.consultationScheduler = consultationScheduler;
        this.consultationMaker = consultationMaker;
    }


    @Bean
    @Order(15)
    public CommandLineRunner dataLoader() throws Exception {
        return args -> {
            studentRepository.save(new Student("Василий", "Сакович", "Владиславович", "vasilii@gmail.com", "{noop}qwerty", "ИВБ-911"));
            studentRepository.save(new Student("Георгий", "Васильев", "Анатольевич", "georgii_vas@mail.ru", "{noop}qwerty", "КИБ-113"));
            Teacher teacher = new Teacher("Сергей", "Иванов", "Александрович", "ivanov@pgups.ru", "{noop}qwerty");
            teacherRepository.save(teacher);
            teacher = new Teacher("Александр", "Щёлоков", "Анатольевич", "vasilii@gmail.com", "{noop}qwerty");
            teacherRepository.save(teacher);
            ConsultationPattern pattern = new ConsultationPattern();
            ConsultationInfo info = new ConsultationInfo();
            info.setClassroom("1-101");
            info.setDate(LocalDate.now());
            info.setStartTime(LocalTime.now().minusHours(1));
            info.setEndTime(LocalTime.now().plusMinutes(1));
            pattern.setConsultationInfo(info);
            pattern.setUntil(LocalDate.now().plusDays(14));
            pattern.setFromDate(LocalDate.now());
            pattern.setTeacher(teacher);
            consultationScheduler.savePattern(pattern);
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

    @Bean
    @Order(25)
    public CommandLineRunner consultations() throws Exception {
        return args -> {
            Teacher teacher = teacherRepository.findByEmail("ivanov@pgups.ru");
            consultationMaker.createConsultation(teacherRepository.findById(teacher.getId()).orElseThrow(), new DatePeriod(
                    "7-421",
                    LocalDateTime.now().plusDays(2),
                    LocalDateTime.now().plusDays(2).plusMinutes(90)));
            teacher = teacherRepository.findByEmail("vasilii@gmail.com");
            consultationMaker.createConsultation(teacherRepository.findById(teacher.getId()).orElseThrow(), new DatePeriod(
                    "1-204",
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(1)));
            consultationMaker.createConsultation(teacherRepository.findById(teacher.getId()).orElseThrow(), new DatePeriod(
                    "4-301",
                    LocalDateTime.now().plusDays(7),
                    LocalDateTime.now().plusDays(7).plusMinutes(90)));
        };
    }
}
