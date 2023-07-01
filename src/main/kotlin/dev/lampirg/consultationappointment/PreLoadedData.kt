package dev.lampirg.consultationappointment

import dev.lampirg.consultationappointment.data.student.Student
import dev.lampirg.consultationappointment.data.student.StudentRepository
import dev.lampirg.consultationappointment.data.teacher.DatePeriod
import dev.lampirg.consultationappointment.data.teacher.Teacher
import dev.lampirg.consultationappointment.data.teacher.TeacherRepository
import dev.lampirg.consultationappointment.service.teacher.ConsultationMaker
import dev.lampirg.consultationappointment.service.teacher.ConsultationScheduler
import dev.lampirg.consultationappointment.web.fetch.ConsultationInfo
import dev.lampirg.consultationappointment.web.fetch.ConsultationPattern
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

@Configuration
@Profile("dev")
class PreLoadedData @Autowired constructor(
    private val studentRepository: StudentRepository,
    private val teacherRepository: TeacherRepository,
    private val consultationScheduler: ConsultationScheduler,
    private val consultationMaker: ConsultationMaker
) {
    @Bean
    @Order(15)
    fun dataLoader(): CommandLineRunner {
        return CommandLineRunner {
            studentRepository.save(
                Student(
                    "Василий",
                    "Сакович",
                    "Владиславович",
                    "vasilii@gmail.com",
                    "{noop}qwerty",
                    "ИВБ-911"
                )
            )
            studentRepository.save(
                Student(
                    "Георгий",
                    "Васильев",
                    "Анатольевич",
                    "georgii_vas@mail.ru",
                    "{noop}qwerty",
                    "КИБ-113"
                )
            )
            var teacher = Teacher("Сергей", "Иванов", "Александрович", "ivanov@pgups.ru", "{noop}qwerty")
            teacherRepository.save(teacher)
            teacher = Teacher("Александр", "Щёлоков", "Анатольевич", "vasilii@gmail.com", "{noop}qwerty")
            teacherRepository.save(teacher)
            val info = ConsultationInfo(
                LocalDate.now(),
                "1-101",
                LocalTime.now().minusHours(1),
                LocalTime.now().plusMinutes(1)
            )
            val pattern = ConsultationPattern(
                LocalDate.now().plusDays(14),
                LocalDate.now(),
                info,
                teacher
            )
            consultationScheduler.savePattern(pattern)
            for (i in 0..14) {
                val firstName = UUID.randomUUID().toString().substring(0, 5)
                val lastName = "Королевич"
                val patronymic = UUID.randomUUID().toString().substring(0, 5)
                val email = UUID.randomUUID().toString().substring(0, 5) + "_multi@pgups.ru"
                teacher = Teacher(firstName, lastName, patronymic, email, "{noop}qwerty")
                teacherRepository.save(teacher)
            }
        }
    }

    @Bean
    @Order(25)
    fun consultations(): CommandLineRunner {
        return CommandLineRunner { args: Array<String?>? ->
            var teacher = teacherRepository.findByEmail("ivanov@pgups.ru")
            consultationMaker.createConsultation(
                teacherRepository.findById(teacher.id!!).orElseThrow(), DatePeriod(
                    "7-421",
                    LocalDateTime.now().plusDays(2),
                    LocalDateTime.now().plusDays(2).plusMinutes(90)
                )
            )
            teacher = teacherRepository.findByEmail("vasilii@gmail.com")
            consultationMaker.createConsultation(
                teacherRepository.findById(teacher.id!!).orElseThrow(), DatePeriod(
                    "1-204",
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(1)
                )
            )
            consultationMaker.createConsultation(
                teacherRepository.findById(teacher.id!!).orElseThrow(), DatePeriod(
                    "4-301",
                    LocalDateTime.now().plusDays(7),
                    LocalDateTime.now().plusDays(7).plusMinutes(90)
                )
            )
        }
    }
}