package dev.lampirg.consultationappointment.service.notification

import dev.lampirg.consultationappointment.data.appointment.Appointment
import dev.lampirg.consultationappointment.data.teacher.DatePeriod
import dev.lampirg.consultationappointment.data.teacher.Teacher
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Aspect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.mail.SimpleMailMessage
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.function.Consumer

@Aspect
@Component
@Profile("email")
@Async
class NotificationAspect @Autowired constructor(private val emailService: EmailService<SimpleMailMessage>) {
    @After(
        "execution(* dev.lampirg.consultationappointment.service.student.AppointmentMaker.makeAppointment(..)) && " +
                "args(teacher,..,datePeriod)"
    )
    @Transactional(readOnly = true)
    fun notifyTeacherAboutAppointment(teacher: Teacher, datePeriod: DatePeriod) {
        if (datePeriod.appointments.isNotEmpty()) return
        val message = SimpleMailMessage()
        message.from = "consultation_service@pgups.example"
        message.subject = "ПГУПС: запись на консультацию"
        message.setTo(teacher.email)
        val text = """
                Здравствуйте, %s %s %s.
                На вашу консультацию, которая состоится %td.%tm с %tR до %tR записался студент.
                """.trimIndent()
        message.text = String.format(
            text,
            teacher.lastName, teacher.firstName, teacher.patronymic,
            datePeriod.startTime.toLocalDate(), datePeriod.startTime.toLocalDate(),
            datePeriod.startTime.toLocalTime(), datePeriod.endTime.toLocalTime()
        )
        emailService.sendEmail(message)
    }

    @After("execution(* dev.lampirg.consultationappointment.service.teacher.ConsultationMaker.deleteConsultation(..))")
    @Transactional(readOnly = true)
    fun notifyStudentsAboutDeletion(joinPoint: JoinPoint) {
        val datePeriod = joinPoint.args[1] as DatePeriod
        if (datePeriod.endTime.isBefore(LocalDateTime.now())) return
        val template = SimpleMailMessage()
        template.from = "consultation_service@pgups.example"
        template.subject = "ПГУПС: запись на консультацию"
        val text = """
                Здравствуйте, %s %s %s.
                К сожалению, консультация на которую вы записались (преподаватель: %s %s %s, дата: %td.%tm, время: с %tR до %tR), была отменена.
                """.trimIndent()
            datePeriod.appointments.forEach(Consumer<Appointment> { appointment: Appointment ->
                val student = appointment.student
                val teacher = appointment.teacher
                template.setTo(student.email)
                template.text = String.format(
                    text,
                    student.lastName, student.firstName, student.patronymic,
                    teacher.lastName, teacher.firstName, teacher.patronymic,
                    datePeriod.startTime.toLocalDate(), datePeriod.startTime.toLocalDate(),
                    datePeriod.startTime.toLocalTime(), datePeriod.endTime.toLocalTime()
                )
                emailService.sendEmail(template)
            })
    }
}