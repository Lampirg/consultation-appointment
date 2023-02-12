package dev.lampirg.consultationappointment.repositories;

import dev.lampirg.consultationappointment.data.teacher.DatePeriod;
import dev.lampirg.consultationappointment.data.teacher.Teacher;
import dev.lampirg.consultationappointment.data.teacher.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TeacherRepositoryTests {
    @Autowired
    private TeacherRepository teacherRepository;

    @BeforeEach
    public void clearAndAdd() {
        teacherRepository.deleteAll();
        teacherRepository.save(getTeacher());
    }

    @Test
    public void testDatePeriod() {
        assertEquals(90, getTeacher().getDatePeriod().stream().toList().get(0).getUnoccupiedTime().toMinutes());
    }

    @Test
    public void testFindById() {
        Teacher teacher = teacherRepository.save(getTeacher());
        Teacher result = teacherRepository.findById(teacher.getId()).get();
        assertEquals(teacher.getId(), result.getId());
    }
    @Test
    public void testFindAll() {
        Teacher teacher = teacherRepository.save(getTeacher());
        assertEquals(2, teacherRepository.findAll().size());
    }
    @Test
    public void testSave() {
        Teacher teacher = teacherRepository.save(getTeacher());
        Teacher found = teacherRepository.findById(teacher.getId()).get();
        found.setFirstName("Иннокентий");
        teacherRepository.save(found);
        assertEquals(teacherRepository.findById(teacher.getId()).get().getFirstName(), found.getFirstName());
    }
    @Test
    public void testDelete() {
        Teacher teacher = teacherRepository.save(getTeacher());
        teacherRepository.delete(teacher);
        assertEquals(1, teacherRepository.findAll().size());
    }
    public static Teacher getTeacher() {
        Teacher teacher = new Teacher();
        teacher.setFirstName("Сергей");
        teacher.setLastName("Иванов");
        teacher.setPatronymic("Александрович");
        teacher.setEmail("mahesh@test.com");
        teacher.getDatePeriod().add(new DatePeriod(
                LocalDateTime.of(2005, 6, 25, 13, 0),
                LocalDateTime.of(2005, 6, 25, 14, 30)
        ));
        return teacher;
    }
}
