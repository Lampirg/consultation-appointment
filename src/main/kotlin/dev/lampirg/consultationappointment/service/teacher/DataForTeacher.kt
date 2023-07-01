package dev.lampirg.consultationappointment.service.teacher

import dev.lampirg.consultationappointment.data.teacher.DatePeriod
import dev.lampirg.consultationappointment.data.teacher.Teacher
import dev.lampirg.consultationappointment.data.teacher.TeacherRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DataForTeacher @Autowired constructor(private val teacherRepository: TeacherRepository) {
    @Transactional(readOnly = true)
    fun findTeacherById(id: Int): Teacher = teacherRepository.findById(id).orElseThrow()

    @Transactional(readOnly = true)
    fun findDatePeriodById(id: Long?): DatePeriod = teacherRepository.findDatePeriodById(id!!).orElseThrow()
}