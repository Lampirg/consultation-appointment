package dev.lampirg.consultationappointment.service.teacher

import dev.lampirg.consultationappointment.data.teacher.DatePeriod
import dev.lampirg.consultationappointment.data.teacher.Teacher

interface ConsultationMaker {
    fun createConsultation(teacher: Teacher, datePeriod: DatePeriod)
    fun deleteConsultation(teacher: Teacher, datePeriod: DatePeriod)
}