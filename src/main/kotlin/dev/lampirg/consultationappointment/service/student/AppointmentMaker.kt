package dev.lampirg.consultationappointment.service.student

import dev.lampirg.consultationappointment.data.student.Student
import dev.lampirg.consultationappointment.data.teacher.DatePeriod
import dev.lampirg.consultationappointment.data.teacher.Teacher

interface AppointmentMaker {
    fun makeAppointment(teacher: Teacher, student: Student, datePeriod: DatePeriod)
    fun deleteAppointmentById(id: Long)
    fun isAvailable(datePeriod: DatePeriod): Boolean
    fun isAvailable(teacher: Teacher, student: Student, datePeriod: DatePeriod): Boolean = isAvailable(datePeriod)
}