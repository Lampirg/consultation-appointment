package dev.lampirg.consultationappointment.repositories

import dev.lampirg.consultationappointment.data.appointment.Appointment
import dev.lampirg.consultationappointment.data.appointment.AppointmentRepository
import dev.lampirg.consultationappointment.data.student.Student
import dev.lampirg.consultationappointment.data.student.StudentRepository
import dev.lampirg.consultationappointment.data.teacher.Teacher
import dev.lampirg.consultationappointment.data.teacher.TeacherRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.TransactionSystemException

@SpringBootTest
class IntegrationTest @Autowired constructor(
    private val appointmentRepository: AppointmentRepository,
    private val studentRepository: StudentRepository,
    private val teacherRepository: TeacherRepository
) {
    @BeforeEach
    fun clearAndFill() {
        appointmentRepository.deleteAll()
        studentRepository.deleteAll()
        teacherRepository.deleteAll()
        val student: Student = studentRepository.save(StudentRepositoryTests.student)
        val teacher: Teacher = teacherRepository.save(TeacherRepositoryTests.teacher)
        val appointment = Appointment(
            teacher,
            student,
            teacher.datePeriods.stream().toList()[0],
            teacher.datePeriods.stream().toList()[0].startTime.plusMinutes(15)
        )
        appointmentRepository.save(appointment)
    }

    @Test
    fun test() {
        Assertions.assertEquals(1, studentRepository.findAll().size)
        Assertions.assertEquals(1, teacherRepository.findAll().size)
        Assertions.assertEquals(1, appointmentRepository.findAll().size)
        Assertions.assertEquals(
            studentRepository.findAll()[0].appointments.stream().toList()[0],
            appointmentRepository.findAll()[0]
        )
    }

    @Test
    fun testDelete() {
        Assertions.assertDoesNotThrow {
            teacherRepository.delete(
                teacherRepository.findAll()[0]
            )
        }
        Assertions.assertDoesNotThrow {
            studentRepository.delete(
                studentRepository.findAll()[0]
            )
        }
    }

    @Test
    fun testIncorrectAppointmentBeforeConsultation() {
        val appointment = appointmentRepository.findAll()[0]!!
        appointment.startTime = appointment.appointmentPeriod.startTime.minusMinutes(25)
        Assertions.assertThrows(TransactionSystemException::class.java) { appointmentRepository.save(appointment) }
        appointment.startTime = appointment.appointmentPeriod.startTime
        Assertions.assertDoesNotThrow<Appointment> {
            appointmentRepository.save(
                appointment
            )
        }
    }

    @Test
    fun testIncorrectAppointmentAfterConsultation() {
        val appointment = appointmentRepository.findAll()[0]!!
        appointment.startTime = appointment.appointmentPeriod.endTime.plusMinutes(25)
        Assertions.assertThrows(TransactionSystemException::class.java) { appointmentRepository.save(appointment) }
        appointment.startTime = appointment.appointmentPeriod.endTime
        Assertions.assertThrows(TransactionSystemException::class.java) { appointmentRepository.save(appointment) }
    }

    @Test
    fun testUpdateDatePeriod() {
        val appointment = appointmentRepository.findAll()[0]!!
        Assertions.assertEquals(1, appointmentRepository.findAll()[0]!!.appointmentPeriod.appointments.size)
    }

    @Test
    fun testDeleteDatePeriod() {
        val teacher = teacherRepository.findAll()[0]
        teacher.datePeriods.remove(teacher.datePeriods.stream().toList()[0])
        teacherRepository.save(teacher)
        Assertions.assertTrue(appointmentRepository.findAll().isEmpty())
    }
}