package dev.lampirg.consultationappointment.service.student

import dev.lampirg.consultationappointment.data.student.Student
import dev.lampirg.consultationappointment.data.student.StudentRepository
import dev.lampirg.consultationappointment.data.teacher.DatePeriod
import dev.lampirg.consultationappointment.data.teacher.Teacher
import dev.lampirg.consultationappointment.data.teacher.TeacherRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DataForStudent @Autowired constructor(
    private val teacherRepository: TeacherRepository,
    private val studentRepository: StudentRepository
) {
    @Transactional(readOnly = true)
    fun findStudentById(id: Int): Student = studentRepository.findById(id).orElseThrow()

    @Transactional(readOnly = true)
    fun findTeacherById(id: Int): Teacher = teacherRepository.findById(id).orElseThrow()

    @Transactional(readOnly = true)
    fun findDatePeriodById(id: Long): DatePeriod = teacherRepository.findDatePeriodById(id).orElseThrow()

    @Transactional(readOnly = true)
    fun findAllTeachers(): List<Teacher> = teacherRepository.findAll()
}