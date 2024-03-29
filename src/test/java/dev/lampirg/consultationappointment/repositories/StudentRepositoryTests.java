package dev.lampirg.consultationappointment.repositories;

import dev.lampirg.consultationappointment.data.student.Student;
import dev.lampirg.consultationappointment.data.student.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.TransactionSystemException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StudentRepositoryTests {

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    public void clearAndAdd() {
        studentRepository.deleteAll();
//        studentRepository.save(getStudent());
    }

    @Test
    void testIncorrectGroupName() {
        Student student = getStudent();
        student.setGroupName("11");
        assertThrows(TransactionSystemException.class, () -> studentRepository.save(student));
        student.setGroupName("КИБ-112");
        assertDoesNotThrow(() -> studentRepository.save(student));
    }

    @Test
    void testFindById() {
        Student student = studentRepository.save(getStudent());
        Student result = studentRepository.findById(student.getId()).get();
        assertEquals(student.getId(), result.getId());
    }

    @Test
    void testFindAll() {
        Student student = studentRepository.save(getStudent());
        assertEquals(1, studentRepository.findAll().size());
    }
    @Test
    void testSave() {
        Student student = studentRepository.save(getStudent());
        Student found = studentRepository.findById(student.getId()).get();
        found.setFirstName("Константин");
        studentRepository.save(found);
        assertEquals(studentRepository.findById(student.getId()).get().getFirstName(), found.getFirstName());
    }
    @Test
    void testDelete() {
        Student student = studentRepository.save(getStudent());
        studentRepository.delete(student);
        assertEquals(0, studentRepository.findAll().size());
    }
    public static Student getStudent() {
        Student student = new Student();
        student.setFirstName("Василий");
        student.setLastName("Сакович");
        student.setPatronymic("Владиславович");
        student.setEmail("vasilii_sakovich@gmail.com");
        student.setGroupName("ИВБ-911");
        student.setPassword("qwerty");
        return student;
    }
}
