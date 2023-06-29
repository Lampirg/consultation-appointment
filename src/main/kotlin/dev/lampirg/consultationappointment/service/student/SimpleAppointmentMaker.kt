package dev.lampirg.consultationappointment.service.student

import dev.lampirg.consultationappointment.data.appointment.Appointment
import dev.lampirg.consultationappointment.data.appointment.AppointmentRepository
import dev.lampirg.consultationappointment.data.student.Student
import dev.lampirg.consultationappointment.data.student.StudentRepository
import dev.lampirg.consultationappointment.data.teacher.DatePeriod
import dev.lampirg.consultationappointment.data.teacher.Teacher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.LocalDate

@Service
class SimpleAppointmentMaker @Autowired constructor(
    private val appointmentRepository: AppointmentRepository,
    private val studentRepository: StudentRepository
) :
    AppointmentMaker {
    @Transactional
    override fun makeAppointment(teacher: Teacher, student: Student, datePeriod: DatePeriod) {
        require(isAvailable(teacher, student, datePeriod)) { "Not enough time for new appointment" }
        var appointment = Appointment(teacher, student, datePeriod, datePeriod.startTime)
        datePeriod.unoccupiedTime = datePeriod.unoccupiedTime.minus(INTERVAL)
        appointment = appointmentRepository.save(appointment)
        studentRepository.findById(appointment.student.id!!).orElseThrow().appointments.add(appointment)
    }

    @Transactional
    override fun deleteAppointmentById(id: Long) {
        val appointment = appointmentRepository.findById(id).orElseThrow()!!
        appointment.appointmentPeriod.unoccupiedTime = appointment.appointmentPeriod.unoccupiedTime.plus(INTERVAL)
        appointmentRepository.save(appointment)
        appointmentRepository.delete(appointment)
        studentRepository.findById(appointment.student.id!!).orElseThrow().appointments.remove(appointment)
    }

    override fun isAvailable(datePeriod: DatePeriod): Boolean {
        return !datePeriod.unoccupiedTime.minus(INTERVAL).isNegative &&
                (datePeriod.startTime.toLocalDate().isAfter(LocalDate.now()) ||
                        datePeriod.appointments.isNotEmpty())
    }

    override fun isAvailable(teacher: Teacher, student: Student, datePeriod: DatePeriod): Boolean {
        return if (!isAvailable(datePeriod)) false else student.appointments.stream()
            .map(Appointment::appointmentPeriod)
            .noneMatch { appointmentPeriod: DatePeriod -> appointmentPeriod.equals(datePeriod) }
    }

    companion object {
        val INTERVAL: Duration = Duration.ofMinutes(15)
    }
}