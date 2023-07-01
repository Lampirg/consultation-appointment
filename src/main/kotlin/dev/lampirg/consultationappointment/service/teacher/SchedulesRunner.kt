package dev.lampirg.consultationappointment.service.teacher

import dev.lampirg.consultationappointment.data.schedule.PatternSchedule
import dev.lampirg.consultationappointment.data.schedule.PatternScheduleRepository
import dev.lampirg.consultationappointment.data.teacher.DatePeriod
import dev.lampirg.consultationappointment.data.teacher.Teacher
import dev.lampirg.consultationappointment.data.teacher.TeacherRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import java.util.function.Consumer

@Configuration
class SchedulesRunner @Autowired constructor(
    private val patternScheduleRepository: PatternScheduleRepository,
    private val consultationScheduler: ConsultationScheduler,
    private val teacherRepository: TeacherRepository,
    private val deleteConsultationAspect: AutoDeleteConsultationAspect
) {
    @Bean
    @Order(20)
    fun runSchedules(): CommandLineRunner = CommandLineRunner {
        val schedules = patternScheduleRepository.findAll()
        schedules.forEach(Consumer { schedule: PatternSchedule ->
            consultationScheduler.addPattern(
                schedule.consultationPattern
            )
        })
        val teachers = teacherRepository.findAll()
        teachers.forEach(Consumer { teacher: Teacher ->
            teacher.datePeriods.forEach(Consumer { datePeriod: DatePeriod? ->
                deleteConsultationAspect.scheduleConsultationDeletion(
                    teacher,
                    datePeriod!!
                )
            })
        })
    }
}