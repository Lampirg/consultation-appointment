package dev.lampirg.consultationappointment.data.appointment

import dev.lampirg.consultationappointment.data.appointment.validator.InTimeBorders
import dev.lampirg.consultationappointment.data.student.Student
import dev.lampirg.consultationappointment.data.teacher.DatePeriod
import dev.lampirg.consultationappointment.data.teacher.Teacher
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import lombok.Getter
import lombok.Setter
import org.springframework.data.jpa.domain.AbstractPersistable
import java.time.LocalDateTime

@Entity
@Getter
@Setter
@InTimeBorders
open class Appointment(
    @ManyToOne(cascade = [CascadeType.MERGE])
    @JoinColumn(name = "teacher_id")
    open val teacher: Teacher,
    @ManyToOne(cascade = [CascadeType.MERGE])
    @JoinColumn(name = "student_id")
    open val student: Student,
    @ManyToOne(cascade = [CascadeType.MERGE])
    @JoinColumn(name = "appointmentPeriod_id")
    open val appointmentPeriod: DatePeriod,
    open var startTime: LocalDateTime
) : AbstractPersistable<Long?>()