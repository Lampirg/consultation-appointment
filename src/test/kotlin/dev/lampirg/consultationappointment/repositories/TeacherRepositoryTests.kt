package dev.lampirg.consultationappointment.repositories

import dev.lampirg.consultationappointment.data.teacher.DatePeriod
import dev.lampirg.consultationappointment.data.teacher.Teacher
import dev.lampirg.consultationappointment.data.teacher.TeacherRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

@SpringBootTest
class TeacherRepositoryTests {
    @Autowired
    private val teacherRepository: TeacherRepository? = null
    @BeforeEach
    fun clearAndAdd() {
        teacherRepository!!.deleteAll()
    }

    @Test
    fun testDatePeriod() {
        assertEquals(90, teacher.datePeriods.stream().toList()[0].unoccupiedTime.toMinutes())
    }

    @Test
    fun testFindById() {
        val teacher = teacherRepository!!.save(teacher)
        val result = teacherRepository.findById(teacher.id!!).get()
        assertEquals(teacher.id, result.id)
    }

    @Test
    fun testFindAll() {
        val teacher = teacherRepository!!.save(teacher)
        assertEquals(1, teacherRepository.findAll().size)
    }

    @Test
    fun testSave() {
        val teacher = teacherRepository!!.save(teacher)
        val found = teacherRepository.findById(teacher.id!!).get()
        found.firstName = "Иннокентий"
        teacherRepository.save(found)
        assertEquals(teacherRepository.findById(teacher.id!!).get().firstName, found.firstName)
    }

    @Test
    fun testDelete() {
        val teacher = teacherRepository!!.save(teacher)
        teacherRepository.delete(teacher)
        assertEquals(0, teacherRepository.findAll().size)
    }

    companion object {
        val teacher: Teacher
            get() {
                val teacher = Teacher(
                    "Сергей",
                    "Иванов",
                    "Александрович",
                    "mahesh@test.com",
                    "{noop}qwerty"
                )
                teacher.datePeriods.add(
                    DatePeriod(
                        "1-101",
                        LocalDateTime.of(2005, 6, 25, 13, 0),
                        LocalDateTime.of(2005, 6, 25, 14, 30)
                    )
                )
                return teacher
            }
    }
}