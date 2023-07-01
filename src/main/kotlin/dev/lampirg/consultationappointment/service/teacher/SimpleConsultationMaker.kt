package dev.lampirg.consultationappointment.service.teacher

import dev.lampirg.consultationappointment.data.teacher.DatePeriod
import dev.lampirg.consultationappointment.data.teacher.Teacher
import dev.lampirg.consultationappointment.data.teacher.TeacherRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SimpleConsultationMaker @Autowired constructor(val teacherRepository: TeacherRepository) : ConsultationMaker {
    @Transactional
    @Modifying
    override fun createConsultation(teacher: Teacher, datePeriod: DatePeriod) {
        teacher.datePeriods.add(datePeriod)
        teacherRepository.save(teacher)
    }

    @Transactional
    @Modifying
    override fun deleteConsultation(teacher: Teacher, datePeriod: DatePeriod) {
        var teacher = teacher
        teacher = teacherRepository.findById(teacher.id!!).orElseThrow()
        if (!teacher.datePeriods.remove(datePeriod)) {
            throw NoSuchElementException("No datePeriod with id = " + datePeriod + " present in " + teacher + "datePeriods")
        }
        teacherRepository.save(teacher)
    }
}