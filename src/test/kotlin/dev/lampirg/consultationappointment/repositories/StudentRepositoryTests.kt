package dev.lampirg.consultationappointment.repositories

import dev.lampirg.consultationappointment.data.student.Student
import dev.lampirg.consultationappointment.data.student.StudentRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.TransactionSystemException

@SpringBootTest
class StudentRepositoryTests @Autowired constructor(val studentRepository: StudentRepository) {
    @BeforeEach
    fun clearAndAdd() {
        studentRepository.deleteAll()
    }

    @Test
    fun testIncorrectGroupName() {
        val student = student
        student.groupName = "11"
        Assertions.assertThrows(TransactionSystemException::class.javaObjectType) { studentRepository.save(student) }
        student.groupName = "КИБ-112"
        Assertions.assertDoesNotThrow<Student> { studentRepository.save(student) }
    }

    @Test
    fun testFindById() {
        val student = studentRepository.save(student)
        val result = studentRepository.findById(student.id!!).get()
        Assertions.assertEquals(student.id, result.id)
    }

    @Test
    fun testFindAll() {
        studentRepository.save(student)
        Assertions.assertEquals(1, studentRepository.findAll().size)
    }

    @Test
    fun testSave() {
        val student = studentRepository.save(student)
        val found = studentRepository.findById(student.id!!).get()
        found.firstName = "Константин"
        studentRepository.save(found)
        Assertions.assertEquals(studentRepository.findById(student.id!!).get().firstName, found.firstName)
    }

    @Test
    fun testDelete() {
        val student = studentRepository.save(student)
        studentRepository.delete(student)
        Assertions.assertEquals(0, studentRepository.findAll().size)
    }

    companion object {
        val student: Student
            get() {
                return Student(
                    "Василий",
                    "Сакович",
                    "Владиславович",
                    "vasilii_sakovich@gmail.com",
                    "{noop}qwerty",
                    "ИВБ-911"
                )
            }
    }
}